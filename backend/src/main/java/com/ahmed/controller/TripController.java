package com.ahmed.controller;

import com.ahmed.dto.trip.TripRequest;
import com.ahmed.dto.trip.TripResponse;
import com.ahmed.dto.trip.TripResponseDriver;
import com.ahmed.dto.trip.UpdateTripStatusRequest;
import com.ahmed.service.TripService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<TripResponse>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTrip(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTripById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','DRIVER')")
    @GetMapping("/driver/{id}")
    public ResponseEntity<List<TripResponseDriver>> getTripsByDriver(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTripsByDriverId(id));
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@RequestBody TripRequest request) {
        TripResponse response = tripService.createTrip(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/report/trip-count")
    public ResponseEntity<Long> getTripCount() {
        return ResponseEntity.ok(tripService.getTripCount());
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/report/revenue-summary")
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(tripService.getTotalRevenue());
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/report/revenue-by-month")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRevenue() {
        return ResponseEntity.ok(tripService.getMonthlyRevenue());
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','DRIVER')")
    @PutMapping("/driver/{id}")
    public ResponseEntity<TripResponse> updateTripStatusOnly(
            @PathVariable Long id,
            @RequestBody UpdateTripStatusRequest request) {
        return ResponseEntity.ok(tripService.updateStatusOnly(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TripResponse> updateTrip(@PathVariable Long id, @RequestBody TripRequest request) {

        return ResponseEntity.ok(tripService.updateTrip(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}
