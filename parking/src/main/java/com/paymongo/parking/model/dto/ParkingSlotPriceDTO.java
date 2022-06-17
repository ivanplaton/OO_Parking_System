package com.paymongo.parking.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ParkingSlotPriceDTO {

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "entrance")
    private String entrance;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "price")
    private BigDecimal price;

}
