package com.ahmed.repository;

import com.ahmed.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("SELECT SUM(t.price) FROM Trip t WHERE t.status = 'COMPLETED'")
    Double getTotalCompletedTripRevenue();

    @Query("SELECT MONTH(t.startTime) as month, SUM(t.price) as revenue " +
            "FROM Trip t WHERE t.status = com.ahmed.model.Trip.Status.COMPLETED " +
            "GROUP BY MONTH(t.startTime) ORDER BY month")
    List<Object[]> getMonthlyRevenue();
    List<Trip> findByDriver_Id(Long driverId);

}
