package com.paymongo.parking.controller;

import com.paymongo.parking.data.entity.ParkingComplexEntity;
import com.paymongo.parking.data.entity.SlotEntity;
import com.paymongo.parking.model.dto.ParkingSlotPriceDTO;
import com.paymongo.parking.model.request.ParkingComplexRequest;
import com.paymongo.parking.model.request.ComplexSlotMappingRequest;
import com.paymongo.parking.model.request.SlotRequest;
import com.paymongo.parking.service.ParkingComplexService;
import com.paymongo.parking.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/parking-complex/")
public class ParkingComplexController {

    @Autowired
    private ParkingComplexService parkingComplexService;
    @Autowired
    private SlotService slotService;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> addParkingComplex(@RequestBody ParkingComplexRequest request) {
        return new ResponseEntity<>(parkingComplexService.addParkingComplex(request), HttpStatus.OK);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> updateParkingComplex(@RequestBody ParkingComplexRequest request) {
        return new ResponseEntity<>(parkingComplexService.updateParkingComplex(request), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParkingComplexEntity>> findAll() {
        return new ResponseEntity<>(parkingComplexService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParkingComplexEntity> findById(@PathVariable(value="id") final UUID id) {
        return new ResponseEntity<>(parkingComplexService.findById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/slot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> addParkingSlot(@RequestBody SlotRequest request) {
        return new ResponseEntity<>(slotService.addParkingSlot(request), HttpStatus.OK);
    }

    @GetMapping(value = "/slot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SlotEntity>> findAllSlot() {
        return new ResponseEntity<>(slotService.findAllSlot(), HttpStatus.OK);
    }

    @GetMapping(value = "/parking-slots", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParkingSlotPriceDTO>> getParkingComplexSlots(@RequestParam(name = "entrance") String entrance,
                                                                            @RequestParam(name = "sort_field") String sortField,
                                                                            @RequestParam(name = "sort_order") String sortOrder,
                                                                            @RequestParam(name = "size") int size,
                                                                            @RequestParam(name = "page") int page) {
        return new ResponseEntity<>(slotService.findByEntrance(entrance, sortField, sortOrder, size, page), HttpStatus.OK);
    }

    @GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParkingSlotPriceDTO>> getAvailableParkingSlots(@RequestParam(name = "entrance") String entrance,
                                                                              @RequestParam(name = "vehicle_category") String vehicleCategory,
                                                                              @RequestParam(name = "sort_field") String sortField,
                                                                              @RequestParam(name = "sort_order") String sortOrder,
                                                                              @RequestParam(name = "size") int size,
                                                                              @RequestParam(name = "page") int page) {
        return new ResponseEntity<>(slotService.getAvailableParkingSlots(entrance, vehicleCategory, sortField, sortOrder, size, page), HttpStatus.OK);
    }

    @PostMapping(value = "/map", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> addParkingComplexSlotMapping(@RequestBody ComplexSlotMappingRequest request) {
        return new ResponseEntity<>(slotService.mapParkingSlot(request), HttpStatus.OK);
    }

}
