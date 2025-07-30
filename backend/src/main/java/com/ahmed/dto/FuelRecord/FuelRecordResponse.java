package com.ahmed.dto.FuelRecord;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelRecordResponse {
    private Long id;
    private Long truckId;
    private String truckPlateNumber;
    private Long tripId;
    private LocalDate date;
    private Double liters;
    private Double pricePerLiter;
    private Double totalCost;
    private String fuelStation;
}
