package com.ahmed.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    private String origin;
    private String destination;

    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @CreationTimestamp // Hibernate annotation
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum Status {
        PENDING, IN_PROGRESS, STARTED, COMPLETED, SCHEDULED
    }

    // Fuel of each trip usage
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<FuelRecord> fuelRecords;

}

