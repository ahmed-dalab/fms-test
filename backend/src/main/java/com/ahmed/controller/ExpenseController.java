package com.ahmed.controller;

import com.ahmed.dto.expense.ExpenseRequest;
import com.ahmed.dto.expense.ExpenseResponse;
import com.ahmed.dto.expense.ExpenseSummaryResponse;
import com.ahmed.model.Expense;
import com.ahmed.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.create(request));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAll() {
        return ResponseEntity.ok(expenseService.getAll());
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(@PathVariable Long id, @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.update(id, request));
    }
    // create fuel record
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseResponse>> filter(
            @RequestParam(required = false) Long truckId,
            @RequestParam(required = false) Expense.Type type,
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end
    ) {
        System.out.println("🔍 Filtering with -> truckId: " + truckId + ", type: " + type + ", start: " + start + ", end: " + end);
        return ResponseEntity.ok(expenseService.filter(truckId, type, start, end));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/summary")
    public ResponseEntity<ExpenseSummaryResponse> getExpenseSummary() {
        return ResponseEntity.ok(expenseService.getExpenseSummary());
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
