package com.ahmed.service;

import com.ahmed.dto.maintenanceRecord.MaintenanceRecordRequest;
import com.ahmed.dto.maintenanceRecord.MaintenanceRecordResponse;
import com.ahmed.model.Expense;
import com.ahmed.model.MaintenanceRecord;
import com.ahmed.model.Truck;
import com.ahmed.repository.ExpenseRepository;
import com.ahmed.repository.MaintenanceRecordRepository;
import com.ahmed.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaintenanceRecordService {

    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final TruckRepository truckRepository;
    private final ExpenseRepository expenseRepository;


    public MaintenanceRecordResponse create(MaintenanceRecordRequest request) {
        Truck truck = truckRepository.findById(request.getTruckId())
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        MaintenanceRecord record = MaintenanceRecord.builder()
                .truck(truck)
                .date(request.getDate() != null ? request.getDate() : LocalDate.now())
                .description(request.getDescription())
                .cost(request.getCost())
                .status(request.getStatus())
                .build();

        MaintenanceRecord saved = maintenanceRecordRepository.save(record);

        // ✅ Auto-create an Expense
        expenseRepository.save(Expense.builder()
                .truck(truck)
                .date(saved.getDate())
                .amount(saved.getCost())
                .description("Maintenance: " + saved.getDescription())
                .type(Expense.Type.MAINTENANCE)
                .build());

        return toResponse(saved);
    }

    public List<MaintenanceRecordResponse> getAll() {
        return maintenanceRecordRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MaintenanceRecordResponse getById(Long id) {
        MaintenanceRecord record = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance record not found"));
        return toResponse(record);
    }

    public MaintenanceRecordResponse update(Long id, MaintenanceRecordRequest request) {
        MaintenanceRecord record = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance record not found"));

        Truck truck = truckRepository.findById(request.getTruckId())
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        // First, update the record with new request values
        record.setTruck(truck);
        record.setDate(request.getDate());
        record.setDescription(request.getDescription());
        record.setCost(request.getCost());
        record.setStatus(request.getStatus());

        MaintenanceRecord updated = maintenanceRecordRepository.save(record);

        // Now find and update the Expense
        Optional<Expense> existing = expenseRepository.findByTruckAndDateAndType(
                truck, updated.getDate(), Expense.Type.MAINTENANCE
        );

        existing.ifPresent(e -> {
            e.setAmount(updated.getCost());
            e.setDescription("Maintenance: " + updated.getDescription());
            e.setDate(updated.getDate());
            expenseRepository.save(e);
        });

        return toResponse(updated);
    }

    public void delete(Long id) {
        MaintenanceRecord record = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance record not found"));
        maintenanceRecordRepository.delete(record);
    }

    private MaintenanceRecordResponse toResponse(MaintenanceRecord record) {
        return MaintenanceRecordResponse.builder()
                .id(record.getId())
                .truckId(record.getTruck().getId())
                .truckPlateNumber(record.getTruck().getPlateNumber())
                .date(record.getDate())
                .description(record.getDescription())
                .cost(record.getCost())
                .status(record.getStatus())
                .build();
    }
}
