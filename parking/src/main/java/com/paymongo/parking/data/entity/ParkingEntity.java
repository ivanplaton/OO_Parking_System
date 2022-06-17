package com.paymongo.parking.data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "parking")
public class ParkingEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "complex_slot_mapping_id", nullable = false)
    private UUID idComplexSlotMapping;

    @Column(name = "plate_number", nullable = false)
    private String plateNumber;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "park_in")
    private LocalDateTime parkIn;

    @Column(name = "park_out")
    private LocalDateTime ParkOut;

    @Column(name = "status", nullable = false)
    private String status;

}
