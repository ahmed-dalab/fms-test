package com.ahmed.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverProfileUpdateRequest {
    private String name;
    private String email;
    private String phone;
    private String licenseNumber;
}
