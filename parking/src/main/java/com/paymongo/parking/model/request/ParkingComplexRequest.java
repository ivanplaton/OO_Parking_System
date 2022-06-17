package com.paymongo.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class ParkingComplexRequest {

    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "parking")
    @NotNull
    private String parking;

    @JsonProperty(value = "entry_point")
    @NotNull
    private String[] entryPoint;
}
