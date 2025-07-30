package com.ahmed.dto.maintenanceRecord;

import com.ahmed.model.MaintenanceRecord.Status;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRecordResponse {
    private Long id;
    private Long truckId;
    private String truckPlateNumber;
    private LocalDate date;
    private String description;
    private Double cost;
    private Status status;
}
