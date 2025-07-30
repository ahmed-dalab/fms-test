package com.ahmed.dto.driver;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateDriverRequest {
    private String name;
    private String email;
    private String password;
    private String licenseNumber;
    private String phone;
    private String role; // "OWNER" or "HIRED"
}
