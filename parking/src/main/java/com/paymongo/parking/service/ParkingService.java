package com.paymongo.parking.service;

import com.paymongo.parking.data.entity.ComplexSlotMappingEntity;
import com.paymongo.parking.data.entity.ParkingComplexEntity;
import com.paymongo.parking.data.entity.ParkingEntity;
import com.paymongo.parking.error.Error;
import com.paymongo.parking.model.ErrorType;
import com.paymongo.parking.model.request.ParkInRequest;
import com.paymongo.parking.model.request.ParkOutRequest;
import com.paymongo.parking.service.helper.ComplexSlotMappingHelper;
import com.paymongo.parking.service.helper.ParkingComplexHelper;
import com.paymongo.parking.service.helper.ParkingHelper;
import com.paymongo.parking.util.FeeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Objects;

@Service
public class ParkingService {

    @Autowired
    private ParkingHelper parkingHelper;
    @Autowired
    private ComplexSlotMappingHelper complexSlotMappingHelper;
    @Autowired
    private ParkingComplexHelper parkingComplexHelper;

    @Autowired
    private FeeUtil feeUtil;

    @Transactional(rollbackOn = Exception.class)
    public HashMap<String, String> park(ParkInRequest request) {
        HashMap<String, String> response = new HashMap<>();

        LocalDateTime today = request.getParkInTime();

        /* check if entrance_code is valid */
        ParkingComplexEntity pcEntity = parkingComplexHelper.findByEntrance(request.getEntranceCode());
        if (Objects.isNull(pcEntity)) {
            ErrorType error = new ErrorType(Error.RECORD_NOT_EXISTS.getCode(), Error.RECORD_NOT_EXISTS.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }

        /* check if vehicle plate number is not yet parked */
        if (parkingHelper.isAlreadyParked(request.getPlateNumber())) {
            ErrorType error = new ErrorType(Error.VEHICLE_ALREADY_PARKED.getCode(), Error.VEHICLE_ALREADY_PARKED.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }

        /* get available parking lot */
        ComplexSlotMappingEntity csmEntity = complexSlotMappingHelper.getOneParkingSlot(pcEntity.getParking(), request.getCategory());
        if (Objects.isNull(csmEntity)) {
            ErrorType error = new ErrorType(Error.NO_AVAILABLE_PARKING.getCode(), Error.NO_AVAILABLE_PARKING.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }

        ParkingEntity parkingEntity = new ParkingEntity();

        /* validate if continuous parking */
        ParkingEntity prevParkingEntity = parkingHelper.validatePreviousParking(request.getPlateNumber(), today);
        if (Objects.nonNull(prevParkingEntity)) {
            parkingEntity.setParkIn(prevParkingEntity.getParkIn());
        } else {
            parkingEntity.setParkIn(today);
        }

        parkingEntity.setIdComplexSlotMapping(csmEntity.getId());
        parkingEntity.setPlateNumber(request.getPlateNumber());
        parkingEntity.setCategory(request.getCategory());
        parkingEntity.setStatus("PARKED");
        parkingHelper.save(parkingEntity);

        response.put("plate_number", request.getPlateNumber());
        response.put("parking_slot", csmEntity.getCode());
        response.put("park_in_time", parkingEntity.getParkIn().toString());
        response.put("status", "parked");
        return response;
    }

    @Transactional(rollbackOn = Exception.class)
    public HashMap<String, String> leaveParking(ParkOutRequest request) {
        HashMap<String, String> response = new HashMap<>();

        ParkingEntity parkingEntity = parkingHelper.findByPlateNumberAndStatus(request.getPlateNumber());
        if (Objects.isNull(parkingEntity)) {
            ErrorType error = new ErrorType(Error.RECORD_NOT_EXISTS.getCode(), Error.RECORD_NOT_EXISTS.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }

        if (!parkingEntity.getStatus().equalsIgnoreCase("PARKED")) {
            ErrorType error = new ErrorType(Error.VEHICLE_NOT_PARKED.getCode(), Error.VEHICLE_NOT_PARKED.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }

        long time_diff = ChronoUnit.SECONDS.between(parkingEntity.getParkIn(), request.getParkOutTime());
        if (time_diff < 0) {
            ErrorType error = new ErrorType(Error.DATA_ERROR.getCode(), Error.DATA_ERROR.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }

        parkingEntity.setParkOut(request.getParkOutTime());
        parkingEntity.setStatus("DONE");
        parkingHelper.save(parkingEntity);

        /* create response */
        ComplexSlotMappingEntity csmEntity = complexSlotMappingHelper.findById(parkingEntity.getIdComplexSlotMapping());
        if (Objects.nonNull(csmEntity)) {
            response.put("parking_slot", csmEntity.getCode());
        }

        /* get parking slot fee */
        double parkingSlotFee = complexSlotMappingHelper.getParkingSlotFee(parkingEntity.getIdComplexSlotMapping());

        /* get fees & total hours parked */
        HashMap<String, String> fees = feeUtil.compute(parkingEntity.getParkIn(), parkingEntity.getParkOut(), parkingSlotFee);

        response.put("plate_number", parkingEntity.getPlateNumber());
        response.put("park_in", parkingEntity.getParkIn().toString());
        response.put("park_out", parkingEntity.getParkOut().toString());
        response.put("total_fee", fees.get("total_fee"));
        response.put("total_hours_parked", fees.get("total_hours_parked"));

        return response;
    }

    public String testFee() {
        LocalDateTime start = LocalDateTime.parse("2022-06-17T01:00:00");
        LocalDateTime end = LocalDateTime.parse("2022-06-17T05:00:00");

        return feeUtil.compute(start, end, 60).toString();
    }
}
