package com.ahmed.dto.FuelRecord;

import com.ahmed.model.Trip;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class FuelRecordDetailsResponse {
    private Long id;

    // simple record details
    private LocalDate date;
    private Double liters;
    private Double pricePerLiter;
    private Double totalCost;
    private String fuelStation;

    // embedded Truck info
    private TruckInfo truck;

    // embedded Trip info (maybe null)
    private TripInfo trip;

    @Builder
    @Getter
    public static class TruckInfo {
        Long id;
        String plateNumber;
        String model;
    }

    @Builder
    @Getter
    public static class TripInfo {
        Long id;
        String origin;
        String destination;
        Trip.Status status;
    }
}

