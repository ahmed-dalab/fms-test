package com.ahmed.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private Double amount;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    public enum Type {
        DRIVER_PAYMENT, CLIENT_PAYMENT
    }
}
