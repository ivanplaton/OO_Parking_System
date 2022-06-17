package com.paymongo.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ComplexSlotMappingRequest {

    @JsonProperty(value = "mapping")
    private List<ComplexSlotMapping> complexSlotMapping;

    @Getter @Setter @ToString
    public static class ComplexSlotMapping {
        @JsonProperty(value = "parking_complex_id")
        private UUID idParkingComplex;

        @JsonProperty(value = "slot_id")
        private UUID idSlot;

        @JsonProperty(value = "code")
        @NotNull
        private String code;
    }
}
