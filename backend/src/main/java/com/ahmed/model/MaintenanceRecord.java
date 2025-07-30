package com.ahmed.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "maintenance_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "truck_id", nullable = false)
    private Truck truck;

    private LocalDate date;

    private String description;

    private Double cost;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        COMPLETED, SCHEDULED
    }
}
