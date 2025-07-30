package com.ahmed.service;

import com.ahmed.dto.expense.ExpenseRequest;
import com.ahmed.dto.expense.ExpenseResponse;
import com.ahmed.dto.expense.ExpenseSummaryResponse;
import com.ahmed.model.Expense;
import com.ahmed.model.Truck;
import com.ahmed.repository.ExpenseRepository;
import com.ahmed.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TruckRepository truckRepository;

    public ExpenseResponse create(ExpenseRequest request) {
        Truck truck = truckRepository.findById(request.getTruckId())
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        Expense expense = Expense.builder()
                .truck(truck)
                .date(request.getDate() != null ? request.getDate() : LocalDate.now())
                .amount(request.getAmount())
                .description(request.getDescription())
                .type(request.getType())
                .build();

        return toResponse(expenseRepository.save(expense));
    }

    public List<ExpenseResponse> getAll() {
        return expenseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ExpenseResponse getById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return toResponse(expense);
    }

    public ExpenseResponse update(Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        Truck truck = truckRepository.findById(request.getTruckId())
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        expense.setTruck(truck);
        expense.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setType(request.getType());

        return toResponse(expenseRepository.save(expense));
    }
    public List<ExpenseResponse> filter(Long truckId, Expense.Type type, LocalDate start, LocalDate end) {
        System.out.println("🔍 Inside the service Filtering with -> truckId: " + truckId + ", type: " + type + ", start: " + start + ", end: " + end);
        return expenseRepository.filter(truckId, type, start, end)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    public ExpenseSummaryResponse getExpenseSummary() {
        double fuel = expenseRepository.sumByType(Expense.Type.FUEL).orElse(0.0);
        double maintenance = expenseRepository.sumByType(Expense.Type.MAINTENANCE).orElse(0.0);
        double repair = expenseRepository.sumByType(Expense.Type.REPAIR).orElse(0.0);
        double other = expenseRepository.sumByType(Expense.Type.OTHER).orElse(0.0);

        double total = fuel + maintenance + repair + other;

        return new ExpenseSummaryResponse(fuel, maintenance, repair, other, total);
    }


    public void delete(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        expenseRepository.delete(expense);
    }

    private ExpenseResponse toResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .truckId(expense.getTruck().getId())
                .truckPlateNumber(expense.getTruck().getPlateNumber())
                .date(expense.getDate())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .type(expense.getType())
                .build();
    }
}
