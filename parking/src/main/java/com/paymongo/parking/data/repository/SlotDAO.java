package com.paymongo.parking.data.repository;

import com.paymongo.parking.data.entity.SlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SlotDAO extends JpaRepository<SlotEntity, UUID> {

    boolean existsByCode(String code);
}
