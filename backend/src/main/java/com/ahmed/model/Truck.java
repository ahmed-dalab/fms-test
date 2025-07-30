package com.ahmed.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "trucks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate_number", unique = true)
    private String plateNumber;

    private String model;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Double capacity;

    public enum Status {
        ACTIVE, INACTIVE, MAINTENANCE
    }
    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Trip> trips;

    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<FuelRecord> fuelRecords;

    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Expense> expenses;

    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL)
    private Collection<MaintenanceRecord> maintenanceRecords;

    // 👇 NEW: The driver who owns this truck
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = true)
    private Driver owner;
}
