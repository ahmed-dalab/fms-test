package com.ahmed.dto.expense;

import com.ahmed.model.Expense.Type;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequest {
    private Long truckId;
    private LocalDate date; // optional, defaults to LocalDate.now()
    private Double amount;
    private String description;
    private Type type;
}
