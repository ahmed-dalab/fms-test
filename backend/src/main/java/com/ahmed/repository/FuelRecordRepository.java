package com.ahmed.repository;

import com.ahmed.model.FuelRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelRecordRepository extends JpaRepository<FuelRecord, Long> {
}
