package com.paymongo.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ParkOutRequest {

    @JsonProperty(value = "plate_number")
    @NotNull
    private String plateNumber;

    @JsonProperty(value = "park_out_time")
    @NotNull
    private LocalDateTime parkOutTime;
}
