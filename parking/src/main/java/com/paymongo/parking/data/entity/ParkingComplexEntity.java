package com.paymongo.parking.data.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@TypeDefs({
        @TypeDef(
                name = "string-array",
                typeClass = StringArrayType.class
        )
})

@Getter
@Setter
@ToString
@Entity
@Table(name = "parking_complex")
public class ParkingComplexEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "parking", nullable = false)
    private String parking;

    @Type(type = "string-array")
    @Column(
            name = "entry_point",
            columnDefinition = "varchar(255)[]"
    )
    private String[] entryPoint;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
