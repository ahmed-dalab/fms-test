package com.ahmed.controller;

import com.ahmed.dto.truck.TruckDetailsResponse;
import com.ahmed.dto.truck.TruckRequest;
import com.ahmed.dto.truck.TruckResponse;
import com.ahmed.service.TruckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/trucks")
public class TruckController {

    private final TruckService truckService;

    public TruckController(TruckService truckService) {
        this.truckService = truckService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<TruckResponse> create(@RequestBody TruckRequest request) {
        TruckResponse response = truckService.createTruck(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<TruckResponse>> getAll() {
        return ResponseEntity.ok(truckService.getAllTrucks());
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/{id}/details")
    public ResponseEntity<TruckDetailsResponse> getTruckDetails(@PathVariable Long id) {
        return ResponseEntity.ok(truckService.getTruckDetails(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TruckResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(truckService.getTruckById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TruckResponse> update(@PathVariable Long id, @RequestBody TruckRequest request,
                                                Principal principal) {
        System.out.println("Current user: " + principal.getName());
        return ResponseEntity.ok(truckService.updateTruck(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTruck(@PathVariable Long id) {
        try {
            truckService.deleteTruck(id);
            return ResponseEntity.ok().body("Truck deleted successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

}
