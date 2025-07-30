package com.ahmed.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FuelRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Truck truck;

    private LocalDate date;
    private Double liters;
    private Double pricePerLiter;
    private Double totalCost;
    private String fuelStation;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
