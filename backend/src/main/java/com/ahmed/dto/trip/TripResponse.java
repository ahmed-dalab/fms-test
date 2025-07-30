package com.ahmed.dto.trip;

import com.ahmed.dto.shared.DriverInfo;
import com.ahmed.dto.shared.TruckInfo;
import com.ahmed.model.Trip.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class TripResponse {
    private Long id;
    private String origin;
    private String destination;
    private Double price;

    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;

    private DriverInfo driver;
    private TruckInfo truck;
}
