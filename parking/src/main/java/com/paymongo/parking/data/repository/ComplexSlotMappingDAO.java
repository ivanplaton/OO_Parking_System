package com.paymongo.parking.data.repository;

import com.paymongo.parking.data.entity.ComplexSlotMappingEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ComplexSlotMappingDAO extends JpaRepository<ComplexSlotMappingEntity, UUID> {

    ComplexSlotMappingEntity findByCode(String code);

    @Query(value = "SELECT s.price FROM complex_slot_mapping csm " +
            "INNER JOIN slot s " +
            "ON s.id = csm.slot_id " +
            "WHERE csm.id = :complex_slot_mapping_id ", nativeQuery = true)
    double queryFindParkingSlotFee(@Param("complex_slot_mapping_id") UUID idComplexSlotMapping);

    @Query(value = "SELECT csm.* FROM complex_slot_mapping csm " +

            "INNER JOIN parking_complex pc " +
            "ON pc.id = csm.parking_complex_id " +

            "INNER JOIN slot s " +
            "ON s.id = csm.slot_id " +
            "AND s.code IN (:allowed_slots) " +

            "WHERE pc.parking = :entrance " +
            "AND csm.id NOT IN (SELECT p.complex_slot_mapping_id FROM parking p " +

            "                   INNER JOIN complex_slot_mapping csm " +
            "                   ON csm.id = p.complex_slot_mapping_id " +

            "                   INNER JOIN parking_complex pc " +
            "                   ON pc.id = csm.parking_complex_id " +

            "                   WHERE p.status = 'PARKED' " +
            "                   AND pc.parking = :entrance) " +
            "ORDER BY csm.code DESC " +
            "LIMIT 1 ", nativeQuery = true)
    ComplexSlotMappingEntity queryFindOneParkingSlot(@Param("entrance") String entrance,
                                                     @Param("allowed_slots") String[] allowedSlots);

    @Query(value = "SELECT csm.code as parking_code, pc.parking as parking_entrance, s.name, s.price " +
            "FROM complex_slot_mapping csm " +
            "INNER JOIN parking_complex pc " +
            "ON pc.id = csm.parking_complex_id " +
            "INNER JOIN slot s " +
            "ON s.id = csm.slot_id " +
            "WHERE pc.parking = :entrance ", nativeQuery = true)
    Page<ParkingSlotPrice> queryFindByEntrance(@Param("entrance") String entrance,
                                               Pageable pageable);

    @Query(value = "SELECT csm.code as parking_code, pc.parking as parking_entrance, s.name, s.price " +
            "FROM complex_slot_mapping csm " +

            "INNER JOIN parking_complex pc " +
            "ON pc.id = csm.parking_complex_id " +

            "INNER JOIN slot s " +
            "ON s.id = csm.slot_id " +
            "AND s.code IN (:allowed_slots) " +

            "WHERE pc.parking = :entrance " +
            "AND csm.id NOT IN (SELECT p.complex_slot_mapping_id FROM parking p " +

            "                   INNER JOIN complex_slot_mapping csm " +
            "                   ON csm.id = p.complex_slot_mapping_id " +

            "                   INNER JOIN parking_complex pc " +
            "                   ON pc.id = csm.parking_complex_id " +

            "                   WHERE p.status = 'PARKED' " +
            "                   AND pc.parking = :entrance) ", nativeQuery = true)
    Page<ParkingSlotPrice> queryFindAvailableParkingSlots(@Param("entrance") String entrance,
                                                          @Param("allowed_slots") String[] allowedSlots,
                                                          Pageable pageable);

    public interface ParkingSlotPrice {
        @Value("#{target.parking_code}")
        String getParkingCode();
        @Value("#{target.parking_entrance}")
        String getParkingEntrance();
        @Value("#{target.name}")
        String getName();
        @Value("#{target.price}")
        Double getPrice();
    }
}
