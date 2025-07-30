// src/main/java/com/ahmed/dto/expense/ExpenseSummaryResponse.java
package com.ahmed.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseSummaryResponse {
    private double fuel;
    private double maintenance;
    private double repair;
    private double other;
    private double total;
}
