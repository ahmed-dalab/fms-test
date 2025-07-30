package com.ahmed.dto.truck;

import com.ahmed.model.Truck;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckResponse {
    private Long id;
    private String plateNumber;
    private String model;
    private Truck.Status status;
    private Double capacity;
    private Long ownerId; // Optional for frontend
}
