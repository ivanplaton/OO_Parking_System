package com.paymongo.parking.data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "complex_slot_mapping")
public class ComplexSlotMappingEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "parking_complex_id", nullable = false)
    private UUID idParkingComplex;

    @Column(name = "slot_id", nullable = false)
    private UUID idSlot;

    @Column(name = "code", nullable = false)
    private String code;

}
