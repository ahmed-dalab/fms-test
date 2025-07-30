package com.ahmed.dto.trip;

import com.ahmed.model.Trip.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TripRequest {
    private Long driverId;
    private Long truckId;
    private String origin;
    private Double price;

    private String destination;
    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
