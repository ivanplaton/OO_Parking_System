package com.paymongo.parking.service.helper;

import com.paymongo.parking.data.entity.SlotEntity;
import com.paymongo.parking.data.repository.SlotDAO;
import com.paymongo.parking.error.Error;
import com.paymongo.parking.model.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@Slf4j
public class SlotHelper {

    @Autowired
    private SlotDAO slotDAO;

    public List<SlotEntity> findAll() {
        return slotDAO.findAll();
    }

    public List<SlotEntity> findByEntrance(String entrance) {
        return slotDAO.findAll();
    }

    public boolean existsByCode(String code) {
        return slotDAO.existsByCode(code);
    }

    public String save(SlotEntity entity) {
        try {
            return slotDAO.save(entity).getId().toString();
        } catch (Exception e) {
            log.error(e.getMessage());

            ErrorType error = new ErrorType(Error.GENERIC_EXCEPTION.getCode(), Error.GENERIC_EXCEPTION.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }
    }
}
