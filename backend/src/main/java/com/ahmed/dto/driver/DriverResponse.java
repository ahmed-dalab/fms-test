package com.ahmed.dto.driver;

import com.ahmed.dto.trip.TripResponse;
import com.ahmed.dto.truck.TruckResponse;
import com.ahmed.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DriverResponse {
    private Long id;
    private String name;
    private String email;
    private String licenseNumber;
    private String phone;
    private Status status;
    private String role;
    private List<TripResponse> trips;
    private List<TruckResponse> ownedTrucks; // Only filled if role == OWNER
}
