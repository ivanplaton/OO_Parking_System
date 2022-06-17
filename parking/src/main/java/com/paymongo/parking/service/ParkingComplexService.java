package com.paymongo.parking.service;

import com.paymongo.parking.data.entity.ParkingComplexEntity;
import com.paymongo.parking.error.Error;
import com.paymongo.parking.model.ErrorType;
import com.paymongo.parking.model.request.ParkingComplexRequest;
import com.paymongo.parking.service.helper.ParkingComplexHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ParkingComplexService {

    @Autowired
    private ParkingComplexHelper parkingComplexHelper;
    @Transactional(rollbackOn = Exception.class)
    public Map<String, String> addParkingComplex(ParkingComplexRequest request) {
        Map<String, String> response = new HashMap<>();

        if (parkingComplexHelper.existsByEntrance(request.getParking())) {
            ErrorType error = new ErrorType(Error.RECORD_EXISTS.getCode(), Error.RECORD_EXISTS.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }

        ParkingComplexEntity entity = new ParkingComplexEntity();
        entity.setParking(request.getParking());
        entity.setEntryPoint(request.getEntryPoint());
        LocalDateTime today = LocalDateTime.now();
        entity.setCreatedAt(today);
        entity.setUpdatedAt(today);

        response.put("id", parkingComplexHelper.saveMapping(entity));
        return response;
    }
    @Transactional(rollbackOn = Exception.class)
    public Map<String, String> updateParkingComplex(ParkingComplexRequest request) {
        Map<String, String> response = new HashMap<>();

        ParkingComplexEntity entity = parkingComplexHelper.findById(request.getId());
        if (Objects.isNull(entity)) {
            ErrorType error = new ErrorType(Error.RECORD_NOT_EXISTS.getCode(), Error.RECORD_NOT_EXISTS.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }

        entity.setParking(request.getParking());
        entity.setEntryPoint(request.getEntryPoint());
        entity.setUpdatedAt(LocalDateTime.now());

        response.put("id", parkingComplexHelper.saveMapping(entity));
        return response;
    }


    public List<ParkingComplexEntity> findAll() {
        return parkingComplexHelper.findAll();
    }

    public ParkingComplexEntity findById(UUID id) {
        ParkingComplexEntity entity = parkingComplexHelper.findById(id);
        if (Objects.isNull(entity)) {
            ErrorType error = new ErrorType(Error.RECORD_NOT_EXISTS.getCode(), Error.RECORD_NOT_EXISTS.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }
        return entity;
    }
}
