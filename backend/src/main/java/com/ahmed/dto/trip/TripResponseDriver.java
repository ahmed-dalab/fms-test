package com.ahmed.dto.trip;
import lombok.*;
import com.ahmed.model.Trip.Status;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripResponseDriver {
    private Long id;
    private String truckPlateNumber;
    private String driverName;
    private String origin;
    private String destination;
    private Double price;
    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}


