package com.ahmed.dto.driver;

import com.ahmed.enums.Status;
import lombok.Data;

@Data
public class UpdateDriverRequest {
    private String name;
    private String email;
    private String phone;
    private String licenseNumber;
    private Status status;
    private String role;
}
