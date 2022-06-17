package com.paymongo.parking.error;

import lombok.Getter;

@Getter
public enum Error {
    GENERIC_EXCEPTION("00000", "An error has occurred"),
    RECORD_EXISTS("00001", "Record already exists"),
    RECORD_NOT_EXISTS("00002", "No record found"),
    INVALID_REQUEST_BODY("00003", "Invalid request body"),
    INVALID_VEHICLE_CATEGORY("00004", "Invalid vehicle category"),
    VEHICLE_ALREADY_PARKED("00005", "Vehicle is already parked"),
    VEHICLE_NOT_PARKED("00006", "Vehicle is not parked"),
    NO_AVAILABLE_PARKING("00007", "There are no parking slots available"),
    DATA_ERROR("00008", "Invalid data");

    private final String code;
    private final String description;

    Error(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
