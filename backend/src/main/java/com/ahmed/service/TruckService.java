package com.ahmed.service;

import com.ahmed.dto.FuelRecord.FuelRecordResponse;
import com.ahmed.dto.expense.ExpenseResponse;
import com.ahmed.dto.truck.TruckDetailsResponse;
import com.ahmed.dto.truck.TruckRequest;
import com.ahmed.dto.truck.TruckResponse;
import com.ahmed.model.Driver;
import com.ahmed.model.Trip;
import com.ahmed.model.Truck;
import com.ahmed.repository.DriverRepository;
import com.ahmed.repository.ExpenseRepository;
import com.ahmed.repository.TruckRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TruckService {
    private final TruckRepository truckRepository;
    private final ExpenseRepository expenseRepository;
    private final DriverRepository driverRepository;
    public TruckService(TruckRepository truckRepository, ExpenseRepository expenseRepository,
                        DriverRepository driverRepository) {
        this.truckRepository = truckRepository;
        this.expenseRepository = expenseRepository;
        this.driverRepository = driverRepository;
    }

    // Create a new truck
    public TruckResponse createTruck(TruckRequest request) {
        Driver owner = null;
        if (request.getOwnerId() != null) {
            owner = driverRepository.findById(request.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner driver not found"));
        }

        Truck truck = Truck.builder()
                .plateNumber(request.plateNumber)
                .model(request.model)
                .status(request.status)
                .capacity(request.capacity)
                .owner(owner) // Assign owner here
                .build();

        Truck saved = truckRepository.save(truck);
        return mapToResponse(saved);
    }

    // Get all trucks
    public List<TruckResponse> getAllTrucks() {
        return truckRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get a single truck
    public TruckResponse getTruckById(Long id) {
        Truck truck = truckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Truck not found"));
        return mapToResponse(truck);
    }
    // get truck details
    public TruckDetailsResponse getTruckDetails(Long id) {
        Truck truck = truckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        // Fuel records mapping
        List<FuelRecordResponse> fuelRecords = truck.getFuelRecords()
                .stream()
                .map(record -> FuelRecordResponse.builder()
                        .id(record.getId())
                        .truckId(truck.getId())
                        .truckPlateNumber(truck.getPlateNumber())
                        .tripId(record.getTrip() != null ? record.getTrip().getId() : null)
                        .date(record.getDate())
                        .liters(record.getLiters())
                        .pricePerLiter(record.getPricePerLiter())
                        .totalCost(record.getTotalCost())
                        .fuelStation(record.getFuelStation())
                        .build())
                .toList();

        // Expense records mapping
        List<ExpenseResponse> expenses = expenseRepository.findByTruck_Id(truck.getId())
                .stream()
                .map(e -> ExpenseResponse.builder()
                        .id(e.getId())
                        .truckId(truck.getId())
                        .truckPlateNumber(truck.getPlateNumber())
                        .date(e.getDate())
                        .amount(e.getAmount())
                        .description(e.getDescription())
                        .type(e.getType())
                        .build())
                .toList();

        return TruckDetailsResponse.builder()
                .id(truck.getId())
                .plateNumber(truck.getPlateNumber())
                .model(truck.getModel())
                .status(truck.getStatus())
                .capacity(truck.getCapacity())
                .fuelRecords(fuelRecords)
                .expenses(expenses)
                .build();
    }

    // Update a truck
    public TruckResponse updateTruck(Long id, TruckRequest request) {
        Truck truck = truckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        truck.setPlateNumber(request.plateNumber);
        truck.setModel(request.model);
        truck.setStatus(request.status);
        truck.setCapacity(request.capacity);

        Truck updated = truckRepository.save(truck);
        return mapToResponse(updated);
    }

    // Delete a truck
    public void deleteTruck(Long id) {
        Truck truck = truckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        // Prevent deletion if truck has any active (non-completed) trips
        if (truck.getTrips() != null && !truck.getTrips().isEmpty()) {
            boolean hasActiveTrips = truck.getTrips().stream()
                    .anyMatch(trip -> trip.getStatus() != Trip.Status.COMPLETED);
            if (hasActiveTrips) {
                throw new IllegalStateException("Cannot delete truck with active trips");
            }
        }

        // Optional: Clear completed trips, or leave them if you want history
        if (truck.getTrips() != null) {
            truck.getTrips().clear(); // remove links, does not delete if orphanRemoval=false
        }

        // Clean up child collections
        if (truck.getFuelRecords() != null) truck.getFuelRecords().clear();
        if (truck.getExpenses() != null) truck.getExpenses().clear();
        if (truck.getMaintenanceRecords() != null) truck.getMaintenanceRecords().clear();

        truckRepository.delete(truck);
    }


    // Helper method
    private TruckResponse mapToResponse(Truck truck) {
        return new TruckResponse(
                truck.getId(),
                truck.getPlateNumber(),
                truck.getModel(),
                truck.getStatus(),
                truck.getCapacity(),
                truck.getOwner() != null ? truck.getOwner().getId() : null
        );
    }

}
