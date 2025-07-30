package com.ahmed.dto.driver;

import com.ahmed.enums.Status;
import com.ahmed.model.Driver;
import com.ahmed.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverProfileResponse {
    private Long id;
    private String name;
    private String email;
    private User.Role role;

    private String phone;
    private String licenseNumber;
    private Status status;
}
