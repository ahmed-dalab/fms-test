package com.ahmed.dto.admin;

import com.ahmed.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminProfileResponse {
    private Long id;
    private String name;
    private String email;
    private User.Role role;
}
