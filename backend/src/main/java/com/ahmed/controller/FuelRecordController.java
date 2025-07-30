package com.ahmed.controller;

import com.ahmed.dto.FuelRecord.FuelRecordDetailsResponse;
import com.ahmed.dto.FuelRecord.FuelRecordRequest;
import com.ahmed.dto.FuelRecord.FuelRecordResponse;
import com.ahmed.service.FuelRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuel-records")
@RequiredArgsConstructor
public class FuelRecordController {

    private final FuelRecordService fuelRecordService;
    // create fuel record
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<FuelRecordResponse> createFuelRecord(@RequestBody FuelRecordRequest request) {
        return ResponseEntity.ok(fuelRecordService.create(request));
    }

    // get all fuel records
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<FuelRecordResponse>> getAllFuelRecords() {
        return ResponseEntity.ok(fuelRecordService.getAll());
    }
    // get single fuel record
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<FuelRecordDetailsResponse> getFuelRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(fuelRecordService.getById(id));
    }
    // update fuel record
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<FuelRecordResponse> updateFuelRecord(
            @PathVariable Long id,
            @RequestBody FuelRecordRequest request) {
        return ResponseEntity.ok(fuelRecordService.update(id, request));
    }
    // delete fuel record
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuelRecord(@PathVariable Long id) {
        fuelRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
