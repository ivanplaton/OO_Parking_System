package com.paymongo.parking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
public class SlotRequest {

    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "code")
    @NotNull
    private String code;

    @JsonProperty(value = "name")
    @NotNull
    private String name;

    @JsonProperty(value = "price")
    @NotNull
    private BigDecimal price;

}
