package com.ahmed.dto.truck;

import com.ahmed.model.Truck;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TruckRequest {
    public String plateNumber;
    public String model;
    public Truck.Status status;
    public Double capacity;
    private Long ownerId; // New field to associate a driver owner
}
