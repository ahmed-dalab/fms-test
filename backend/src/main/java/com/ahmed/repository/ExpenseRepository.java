package com.ahmed.repository;

import com.ahmed.model.Expense;
import com.ahmed.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByTruck_Id(Long truckId);
    List<Expense> findByType(Expense.Type type);

    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT e FROM Expense e WHERE " +
            "(:truckId IS NULL OR e.truck.id = :truckId) AND " +
            "(:type IS NULL OR e.type = :type) AND " +
            "(:start IS NULL OR :end IS NULL OR e.date BETWEEN :start AND :end)")
    List<Expense> filter(Long truckId, Expense.Type type, LocalDate start, LocalDate end);


    Optional<Expense> findByTruckAndDateAndType(Truck truck, LocalDate date, Expense.Type type);
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.type = :type")
    Optional<Double> sumByType(@Param("type") Expense.Type type);

}
