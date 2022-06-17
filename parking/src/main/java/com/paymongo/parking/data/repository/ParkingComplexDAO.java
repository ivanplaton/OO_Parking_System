package com.paymongo.parking.data.repository;

import com.paymongo.parking.data.entity.ParkingComplexEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParkingComplexDAO extends JpaRepository<ParkingComplexEntity, UUID> {

    ParkingComplexEntity findByParking(String parking);
    boolean existsByParking(String parking);
}
