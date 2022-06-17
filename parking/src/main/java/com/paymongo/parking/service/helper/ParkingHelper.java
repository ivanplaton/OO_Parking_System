package com.paymongo.parking.service.helper;

import com.paymongo.parking.data.entity.ParkingEntity;
import com.paymongo.parking.data.repository.ParkingDAO;
import com.paymongo.parking.error.Error;
import com.paymongo.parking.model.ErrorType;
import com.paymongo.parking.model.request.ParkOutRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
@Slf4j
public class ParkingHelper {

    @Autowired
    private ParkingDAO parkingDAO;

    public ParkingEntity findByPlateNumberAndStatus(String plateNumber) {
        return parkingDAO.findByPlateNumberAndStatus(plateNumber, "PARKED");
    }

    public void save(ParkingEntity entity) {
        try {
            parkingDAO.save(entity);
        } catch (Exception e) {
            log.error(e.getMessage());

            ErrorType error = new ErrorType(Error.GENERIC_EXCEPTION.getCode(), Error.GENERIC_EXCEPTION.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }
    }

    public boolean isAlreadyParked(String plateNumber) {
        return parkingDAO.existsByPlateNumberAndStatus(plateNumber, "PARKED");
    }

    public ParkingEntity validatePreviousParking(String plateNumber, LocalDateTime parkIn) {
        ParkingEntity entity = parkingDAO.queryFindPreviousParking(plateNumber);
        if (Objects.nonNull(entity)) {
            long minutesDiff = ChronoUnit.MINUTES.between(entity.getParkOut(), parkIn);
            if (minutesDiff <= 60) {
                entity.setStatus("CONTINUED");
                parkingDAO.save(entity);

                return entity;
            }
        }
        return null;
    }

}
