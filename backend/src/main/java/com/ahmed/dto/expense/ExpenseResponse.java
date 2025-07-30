package com.ahmed.dto.expense;

import com.ahmed.model.Expense.Type;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponse {
    private Long id;
    private Long truckId;
    private String truckPlateNumber;
    private LocalDate date;
    private Double amount;
    private String description;
    private Type type;
}
