package com.ahmed.service;

import com.ahmed.Exception.EmailAlreadyExistsException;
import com.ahmed.dto.driver.CreateDriverRequest;
import com.ahmed.dto.driver.DriverResponse;
import com.ahmed.dto.shared.DriverInfo;
import com.ahmed.dto.shared.TruckInfo;
import com.ahmed.dto.trip.TripResponse;
import com.ahmed.dto.driver.UpdateDriverRequest;
import com.ahmed.dto.truck.TruckResponse;
import com.ahmed.enums.Status;
import com.ahmed.enums.Role;
import com.ahmed.model.Driver;
import com.ahmed.model.User;
import com.ahmed.repository.DriverRepository;
import com.ahmed.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DriverService(DriverRepository driverRepository, UserRepository userRepository) {
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    // here i will write all the service of the driver.
    // Create driver
    // Add @Transactional to all methods that modify data
    @Transactional
    public Driver createDriver(CreateDriverRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        User user = userRepository.save(User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(User.Role.DRIVER)
                .build());

        Driver driver = driverRepository.save(Driver.builder()
                .user(user)
                .licenseNumber(req.getLicenseNumber())
                .phone(req.getPhone())
                .status(Status.ACTIVE)
                .role(Role.valueOf(req.getRole()))
                .build());

        return driver;
    }

    // get all drivers
    // Add @Transactional(readOnly = true) to all read-only methods
    @Transactional(readOnly = true)
    public List<DriverResponse> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(this::mapToDriverResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DriverResponse getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        return mapToDriverResponse(driver);
    }

    @Transactional
    public DriverResponse updateDriver(Long id, UpdateDriverRequest req) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        if (req.getPhone() != null) driver.setPhone(req.getPhone());
        if (req.getLicenseNumber() != null) driver.setLicenseNumber(req.getLicenseNumber());
        if (req.getStatus() != null) driver.setStatus(req.getStatus());

        if (req.getName() != null) driver.getUser().setName(req.getName());
        if (req.getEmail() != null) driver.getUser().setEmail(req.getEmail());

        return mapToDriverResponse(driverRepository.save(driver));
    }

    @Transactional
    public void deleteDriver(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        driverRepository.delete(driver);
        userRepository.delete(driver.getUser());
    }
    public DriverResponse mapToDriverResponse(Driver driver) {
        User user = driver.getUser();

        List<TripResponse> tripResponses = driver.getTrips() == null ? List.of() :
                driver.getTrips().stream()
                        .map(trip -> TripResponse.builder()
                                .id(trip.getId())
                                .origin(trip.getOrigin())
                                .destination(trip.getDestination())
                                .status(trip.getStatus())
                                .startTime(trip.getStartTime())
                                .endTime(trip.getEndTime())
                                .driver(new DriverInfo(
                                        trip.getDriver().getId(),
                                        trip.getDriver().getUser().getName()
                                ))
                                .truck(new TruckInfo(
                                        trip.getTruck().getId(),
                                        trip.getTruck().getModel()
                                ))
                                .build())
                        .toList();


        return DriverResponse.builder()
                .id(driver.getId())
                .name(user.getName())
                .email(user.getEmail())
                .licenseNumber(driver.getLicenseNumber())
                .phone(driver.getPhone())
                .status(driver.getStatus())
                .role(String.valueOf(driver.getRole()))
                .trips(tripResponses)
                .ownedTrucks(driver.getOwnedTrucks() == null ? List.of() :
                        driver.getOwnedTrucks().stream()
                                .map(truck -> new TruckResponse(
                                        truck.getId(),
                                        truck.getPlateNumber(),
                                        truck.getModel(),
                                        truck.getStatus(),
                                        truck.getCapacity(),
                                        driver.getId()
                                )).toList())
                .build();
    }


}
