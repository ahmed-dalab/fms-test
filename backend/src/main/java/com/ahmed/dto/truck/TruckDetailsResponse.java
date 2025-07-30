package com.ahmed.dto.truck;

import com.ahmed.dto.FuelRecord.FuelRecordResponse;
import com.ahmed.dto.expense.ExpenseResponse;
import com.ahmed.model.Truck;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TruckDetailsResponse {
    private Long id;
    private String plateNumber;
    private String model;
    private Truck.Status status;
    private Double capacity;
    private List<FuelRecordResponse> fuelRecords;
    private List<ExpenseResponse> expenses;
}
