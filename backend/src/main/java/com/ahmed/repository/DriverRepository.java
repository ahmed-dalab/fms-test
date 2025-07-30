package com.ahmed.repository;

import com.ahmed.model.Driver;
import com.ahmed.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByUser(Optional<User> user);
}
