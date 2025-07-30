package com.ahmed.model;

import com.ahmed.enums.Role;
import com.ahmed.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign Key to Users
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = true, insertable = true, updatable = true)
    private Role role;
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips = new ArrayList<>();;
    // Trucks this driver owns (even if others drive them)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Truck> ownedTrucks = new ArrayList<>();
}
