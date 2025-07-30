package com.ahmed.controller;

import com.ahmed.dto.maintenanceRecord.MaintenanceRecordRequest;
import com.ahmed.dto.maintenanceRecord.MaintenanceRecordResponse;
import com.ahmed.service.MaintenanceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance-records")
@RequiredArgsConstructor
public class MaintenanceRecordController {

    private final MaintenanceRecordService maintenanceRecordService;
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<MaintenanceRecordResponse> create(@RequestBody MaintenanceRecordRequest request) {
        return ResponseEntity.ok(maintenanceRecordService.create(request));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<MaintenanceRecordResponse>> getAll() {
        return ResponseEntity.ok(maintenanceRecordService.getAll());
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecordResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceRecordService.getById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRecordResponse> update(@PathVariable Long id, @RequestBody MaintenanceRecordRequest request) {
        return ResponseEntity.ok(maintenanceRecordService.update(id, request));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        maintenanceRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
