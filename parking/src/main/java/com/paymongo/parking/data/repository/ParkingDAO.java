package com.paymongo.parking.data.repository;

import com.paymongo.parking.data.entity.ParkingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ParkingDAO extends JpaRepository<ParkingEntity, UUID> {

    ParkingEntity findByPlateNumberAndStatus(String plateNumber, String status);
    boolean existsByPlateNumberAndStatus(String plateNumber, String status);

    @Query(value = "SELECT * FROM parking " +
            "WHERE plate_number = 'IVN-525' " +
            "AND status = 'DONE' " +
            "ORDER BY park_out DESC LIMIT 1 ", nativeQuery = true)
    ParkingEntity queryFindPreviousParking(@Param("plate_number") String plateNumber);
}
