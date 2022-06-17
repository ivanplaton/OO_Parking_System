package com.paymongo.parking.service.helper;

import com.paymongo.parking.data.entity.ParkingComplexEntity;
import com.paymongo.parking.data.repository.ParkingComplexDAO;
import com.paymongo.parking.error.Error;
import com.paymongo.parking.model.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class ParkingComplexHelper {

    @Autowired
    private ParkingComplexDAO parkingComplexDAO;

    public List<ParkingComplexEntity> findAll() {
        return parkingComplexDAO.findAll();
    }

    public ParkingComplexEntity findById(UUID id) {
        return parkingComplexDAO.findById(id).orElse(null);
    }

    public ParkingComplexEntity findByEntrance(String entrance) {
        return parkingComplexDAO.findByParking(entrance);
    }

    public boolean existsByEntrance(String entrance) {
        return parkingComplexDAO.existsByParking(entrance);
    }

    @Transactional(rollbackOn = DataAccessException.class)
    public String saveMapping(ParkingComplexEntity entity) {
        try {
            return parkingComplexDAO.save(entity).getId().toString();
        } catch (Exception e) {
            log.error(e.getMessage());

            ErrorType error = new ErrorType(Error.GENERIC_EXCEPTION.getCode(), Error.GENERIC_EXCEPTION.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }
    }
}
