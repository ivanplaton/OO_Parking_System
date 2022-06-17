package com.paymongo.parking.service;

import com.paymongo.parking.data.entity.ComplexSlotMappingEntity;
import com.paymongo.parking.data.entity.SlotEntity;
import com.paymongo.parking.error.Error;
import com.paymongo.parking.model.ErrorType;
import com.paymongo.parking.model.dto.ParkingSlotPriceDTO;
import com.paymongo.parking.model.request.ComplexSlotMappingRequest;
import com.paymongo.parking.model.request.SlotRequest;
import com.paymongo.parking.service.helper.ComplexSlotMappingHelper;
import com.paymongo.parking.service.helper.SlotHelper;
import com.paymongo.parking.util.ListMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SlotService {

    @Autowired
    private SlotHelper slotHelper;
    @Autowired
    private ComplexSlotMappingHelper complexSlotMappingHelper;

    @Autowired
    private ListMapperUtil listMapperUtil;

    @Transactional(rollbackOn = Exception.class)
    public Map<String, String> addParkingSlot(SlotRequest request) {
        Map<String, String> response = new HashMap<>();

        if (slotHelper.existsByCode(request.getCode())) {
            ErrorType error = new ErrorType(Error.RECORD_EXISTS.getCode(), Error.RECORD_EXISTS.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }

        SlotEntity entity = new SlotEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setPrice(request.getPrice());
        LocalDateTime today = LocalDateTime.now();
        entity.setCreatedAt(today);
        entity.setUpdatedAt(today);

        response.put("id", slotHelper.save(entity));

        return response;
    }
    @Transactional(rollbackOn = Exception.class)
    public Map<String, String> mapParkingSlot(ComplexSlotMappingRequest request) {
        Map<String, String> response = new HashMap<>();

        if (Objects.isNull(request.getComplexSlotMapping())) {
            ErrorType error = new ErrorType(Error.INVALID_REQUEST_BODY.getCode(), Error.INVALID_REQUEST_BODY.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }

        complexSlotMappingHelper.save(listMapperUtil.mapList(request.getComplexSlotMapping(), ComplexSlotMappingEntity.class));

        response.put("message", "mapping success");
        return response;
    }

    public List<SlotEntity> findAllSlot() {
        return slotHelper.findAll();
    }

    public List<ParkingSlotPriceDTO> findByEntrance(String entrance, String sortField, String sortOrder, int size, int page) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()),sortField));

        return complexSlotMappingHelper.findByEntrance(entrance, pageable);
    }

    public List<ParkingSlotPriceDTO> getAvailableParkingSlots(String entrance, String vehicleCategory, String sortField, String sortOrder, int size, int page) {
        if (!vehicleCategory.equalsIgnoreCase("S") && !vehicleCategory.equalsIgnoreCase("M") && !vehicleCategory.equalsIgnoreCase("L")) {
            ErrorType error = new ErrorType(Error.INVALID_VEHICLE_CATEGORY.getCode(), Error.INVALID_VEHICLE_CATEGORY.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()),sortField));

        return complexSlotMappingHelper.getAvailableParkingSlots(entrance, vehicleCategory, pageable);
    }

}
