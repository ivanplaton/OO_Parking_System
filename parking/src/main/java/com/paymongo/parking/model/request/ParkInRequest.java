package com.paymongo.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ParkInRequest {

    @JsonProperty(value = "entrance_code")
    @NotNull
    private String entranceCode;

    @JsonProperty(value = "plate_number")
    @NotNull
    private String plateNumber;

    @JsonProperty(value = "category")
    @NotNull
    private String category;

    @JsonProperty(value = "park_in_time")
    @NotNull
    private LocalDateTime parkInTime;

}
