package com.ahmed.service;

import com.ahmed.dto.admin.AdminProfileResponse;
import com.ahmed.dto.driver.DriverProfileResponse;
import com.ahmed.dto.user.UpdateProfileRequest;
import com.ahmed.model.Driver;
import com.ahmed.model.User;
import com.ahmed.repository.DriverRepository;
import com.ahmed.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final DriverService driverService;

    public UserService(UserRepository userRepository, DriverService driverService,DriverRepository driverRepository) {
        this.userRepository = userRepository;
        this.driverService = driverService;
        this.driverRepository = driverRepository;

    }

    // here i will write all functions of the service.
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    user.setRole(updatedUser.getRole());
                    // Don't update password unless needed
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Transactional(readOnly = true)
    public Object getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == User.Role.DRIVER) {
            Driver driver = driverRepository.findByUser(Optional.of(user))
                    .orElseThrow(() -> new RuntimeException("Driver not found"));

            return DriverProfileResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .phone(driver.getPhone())
                    .licenseNumber(driver.getLicenseNumber())
                    .status(driver.getStatus())
                    .build();
        }

        return AdminProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Transactional
    public AdminProfileResponse updateAdminProfile(String email, UpdateProfileRequest request) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    user.setName(request.getName());
                    user.setEmail(request.getEmail());
                    User saved = userRepository.save(user);

                    return AdminProfileResponse.builder()
                            .id(saved.getId())
                            .name(saved.getName())
                            .email(saved.getEmail())
                            .role(saved.getRole())
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public DriverProfileResponse updateDriverProfile(String email, UpdateProfileRequest request) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    user.setName(request.getName());
                    user.setEmail(request.getEmail());
                    User savedUser = userRepository.save(user);

                    Driver driver = driverRepository.findByUser(Optional.of(savedUser))
                            .orElseThrow(() -> new RuntimeException("Driver profile not found"));

                    driver.setPhone(request.getPhone());
                    driver.setLicenseNumber(request.getLicenseNumber());
                    Driver savedDriver = driverRepository.save(driver);

                    return DriverProfileResponse.builder()
                            .id(savedUser.getId())
                            .name(savedUser.getName())
                            .email(savedUser.getEmail())
                            .phone(savedDriver.getPhone())
                            .licenseNumber(savedDriver.getLicenseNumber())
                            .role(savedUser.getRole())
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    // 7. Validate user credentials (for login)
    // public boolean validateUser(String email, String password);
    //
    // // 8. Assign roles (admin, driver, etc.)
    // public User assignRole(Long userId, String role);

}
