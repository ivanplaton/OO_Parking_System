package com.paymongo.parking.service.helper;

import com.paymongo.parking.data.entity.ComplexSlotMappingEntity;
import com.paymongo.parking.data.repository.ComplexSlotMappingDAO;
import com.paymongo.parking.error.Error;
import com.paymongo.parking.model.ErrorType;
import com.paymongo.parking.model.dto.ParkingSlotPriceDTO;
import com.paymongo.parking.util.ListMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class ComplexSlotMappingHelper {

    @Autowired
    private ComplexSlotMappingDAO complexSlotMappingDAO;
    @Autowired
    private ListMapperUtil listMapperUtil;

    private String[] getAllowedSlots(String vehicleCategory) {
        return switch (vehicleCategory.toUpperCase()) {
            case "S" -> new String[]{"SP", "MP", "LP"};
            case "M" -> new String[]{"MP", "LP"};
            case "L" -> new String[]{"LP"};
            default -> null;
        };
    }

    public void save(List<ComplexSlotMappingEntity> entityList) {
        try {
            complexSlotMappingDAO.saveAll(entityList);
        } catch (Exception e) {
            log.error(e.getMessage());

            ErrorType error = new ErrorType(Error.GENERIC_EXCEPTION.getCode(), Error.GENERIC_EXCEPTION.getDescription());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
        }
    }

    public ComplexSlotMappingEntity findByCode(String code) {
        return complexSlotMappingDAO.findByCode(code);
    }

    public ComplexSlotMappingEntity findById(UUID id) {
        return complexSlotMappingDAO.findById(id).orElse(null);
    }

    public List<ParkingSlotPriceDTO> findByEntrance(String entrance, Pageable pageable) {
        Page<ComplexSlotMappingDAO.ParkingSlotPrice> parkingSlotPriceList = complexSlotMappingDAO.queryFindByEntrance(entrance, pageable);
        return listMapperUtil.mapList(parkingSlotPriceList.getContent(), ParkingSlotPriceDTO.class);
    }

    public double getParkingSlotFee(UUID idComplexSlotMapping) {
        return complexSlotMappingDAO.queryFindParkingSlotFee(idComplexSlotMapping);
    }

    public List<ParkingSlotPriceDTO> getAvailableParkingSlots(String entrance, String vehicleCategory, Pageable pageable) {
        String[] allowedSlots = getAllowedSlots(vehicleCategory);

        Page<ComplexSlotMappingDAO.ParkingSlotPrice> parkingSlotPriceList = complexSlotMappingDAO.queryFindAvailableParkingSlots(entrance, allowedSlots, pageable);
        return listMapperUtil.mapList(parkingSlotPriceList.getContent(), ParkingSlotPriceDTO.class);
    }

    public ComplexSlotMappingEntity getOneParkingSlot(String entrance, String vehicleCategory) {
        String[] allowedSlots = getAllowedSlots(vehicleCategory);

        return complexSlotMappingDAO.queryFindOneParkingSlot(entrance, allowedSlots);
    }

}
