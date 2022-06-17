package com.paymongo.parking.model;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public class ErrorType {
    @NonNull
    private final String code;
    @NonNull
    private final String message;
}