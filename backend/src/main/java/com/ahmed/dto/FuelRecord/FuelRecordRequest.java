package com.ahmed.dto.FuelRecord;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelRecordRequest {
    private Long truckId;
    private Long tripId; // optional
    private LocalDate date;
    private Double liters;
    private Double pricePerLiter;
    private String fuelStation;
}
