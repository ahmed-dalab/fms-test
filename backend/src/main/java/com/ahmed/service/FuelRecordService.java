package com.ahmed.service;

import com.ahmed.dto.FuelRecord.FuelRecordDetailsResponse;
import com.ahmed.dto.FuelRecord.FuelRecordRequest;
import com.ahmed.dto.FuelRecord.FuelRecordResponse;
import com.ahmed.model.Expense;
import com.ahmed.model.FuelRecord;
import com.ahmed.model.Trip;
import com.ahmed.model.Truck;
import com.ahmed.repository.ExpenseRepository;
import com.ahmed.repository.FuelRecordRepository;
import com.ahmed.repository.TripRepository;
import com.ahmed.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuelRecordService {

    private final FuelRecordRepository fuelRecordRepository;
    private final TruckRepository truckRepository;
    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    // create fuel record
    public FuelRecordResponse create(FuelRecordRequest request) {
        Truck truck = truckRepository.findById(request.getTruckId())
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        Trip trip = null;
        if (request.getTripId() != null) {
            trip = tripRepository.findById(request.getTripId())
                    .orElseThrow(() -> new RuntimeException("Trip not found"));
        }

        FuelRecord fuel = FuelRecord.builder()
                .truck(truck)
                .trip(trip)
                .date(request.getDate() != null ? request.getDate() : LocalDate.now())
                .liters(request.getLiters())
                .pricePerLiter(request.getPricePerLiter())
                .totalCost(request.getLiters() * request.getPricePerLiter())
                .fuelStation(request.getFuelStation())
                .build();

        FuelRecord saved = fuelRecordRepository.save(fuel);

        // ✅ Auto-create an Expense
        expenseRepository.save(Expense.builder()
                .truck(truck)
                .date(fuel.getDate())
                .amount(fuel.getTotalCost())
                .description("Fuel at " + fuel.getFuelStation())
                .type(Expense.Type.FUEL)
                .build());

        return toResponse(saved);
    }

    // get all fuel record
    public List<FuelRecordResponse> getAll() {
        return fuelRecordRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    // get single fuel record
    public FuelRecordDetailsResponse getById(Long id) {
        FuelRecord record = fuelRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FuelRecord not found"));
        return toResponseDetails(record);
    }

    // update fuel record
    @Transactional
    public FuelRecordResponse update(Long id, FuelRecordRequest request) {
        FuelRecord fuel = fuelRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FuelRecord not found"));

        Truck truck = truckRepository.findById(request.getTruckId())
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        Trip trip = null;
        if (request.getTripId() != null) {
            trip = tripRepository.findById(request.getTripId())
                    .orElseThrow(() -> new RuntimeException("Trip not found"));
        }

        // First, update fuel with request data
        fuel.setTruck(truck);
        fuel.setTrip(trip);
        fuel.setDate(request.getDate());
        fuel.setLiters(request.getLiters());
        fuel.setPricePerLiter(request.getPricePerLiter());
        fuel.setTotalCost(request.getLiters() * request.getPricePerLiter());
        fuel.setFuelStation(request.getFuelStation());

        FuelRecord updated = fuelRecordRepository.save(fuel);

        // Now update the expense
        Optional<Expense> existing = expenseRepository.findByTruckAndDateAndType(
                truck, updated.getDate(), Expense.Type.FUEL
        );

        existing.ifPresent(e -> {
            e.setAmount(updated.getTotalCost());
            e.setDescription("Fuel at " + updated.getFuelStation());
            e.setDate(updated.getDate());
            expenseRepository.save(e);
        });

        return toResponse(updated);
    }

    // delete fuel record
    public void delete(Long id) {
        FuelRecord fuel = fuelRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FuelRecord not found"));
        fuelRecordRepository.delete(fuel);
    }

    private FuelRecordResponse toResponse(FuelRecord record) {
        return FuelRecordResponse.builder()
                .id(record.getId())
                .truckId(record.getTruck().getId())
                .truckPlateNumber(record.getTruck().getPlateNumber())
                .tripId(record.getTrip() != null ? record.getTrip().getId() : null)
                .date(record.getDate())
                .liters(record.getLiters())
                .pricePerLiter(record.getPricePerLiter())
                .totalCost(record.getTotalCost())
                .fuelStation(record.getFuelStation())
                .build();
    }

    private FuelRecordDetailsResponse toResponseDetails(FuelRecord r) {
        FuelRecordDetailsResponse.TruckInfo truckInfo = FuelRecordDetailsResponse.TruckInfo.builder()
                .id(r.getTruck().getId())
                .plateNumber(r.getTruck().getPlateNumber())
                .model(r.getTruck().getModel())
                .build();

        FuelRecordDetailsResponse.TripInfo tripInfo = null;
        if (r.getTrip() != null) {
            tripInfo = FuelRecordDetailsResponse.TripInfo.builder()
                    .id(r.getTrip().getId())
                    .origin(r.getTrip().getOrigin())
                    .destination(r.getTrip().getDestination())
                    .status(r.getTrip().getStatus())
                    .build();
        }

        return FuelRecordDetailsResponse.builder()
                .id(r.getId())
                .date(r.getDate())
                .liters(r.getLiters())
                .pricePerLiter(r.getPricePerLiter())
                .totalCost(r.getTotalCost())
                .fuelStation(r.getFuelStation())
                .truck(truckInfo)
                .trip(tripInfo)
                .build();
    }

}
