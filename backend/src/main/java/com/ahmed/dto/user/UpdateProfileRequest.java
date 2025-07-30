package com.ahmed.dto.user;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String email;

    // Only used by DRIVER
    private String phone;
    private String licenseNumber;
}


