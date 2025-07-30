package com.ahmed.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "truck_id", nullable = false)
    private Truck truck;

    private LocalDate date;

    private Double amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        FUEL, MAINTENANCE, REPAIR, OTHER
    }
}
