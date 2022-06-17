package com.paymongo.parking.controller;

import com.paymongo.parking.model.request.ParkInRequest;
import com.paymongo.parking.model.request.ParkOutRequest;
import com.paymongo.parking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/api/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @PostMapping(value = "/park", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, String>> park(@RequestBody ParkInRequest request) {
        return new ResponseEntity<>(parkingService.park(request), HttpStatus.OK);
    }

    @PostMapping(value = "/leave", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, String>> leaveParking(@RequestBody ParkOutRequest request) {
        return new ResponseEntity<>(parkingService.leaveParking(request), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> leaveParkingTest() {
        return new ResponseEntity<>(parkingService.testFee(), HttpStatus.OK);
    }

}
