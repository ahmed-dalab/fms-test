package com.ahmed.service;

import com.ahmed.dto.shared.DriverInfo;
import com.ahmed.dto.shared.TruckInfo;
import com.ahmed.dto.trip.TripRequest;
import com.ahmed.dto.trip.TripResponse;
import com.ahmed.dto.trip.TripResponseDriver;
import com.ahmed.dto.trip.UpdateTripStatusRequest;
import com.ahmed.model.Driver;
import com.ahmed.model.Trip;
import com.ahmed.model.Truck;
import com.ahmed.model.User;
import com.ahmed.repository.DriverRepository;
import com.ahmed.repository.TripRepository;
import com.ahmed.repository.TruckRepository;
import com.ahmed.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final TruckRepository truckRepository;

    public TripService(
            TripRepository tripRepository,
            DriverRepository driverRepository,
            TruckRepository truckRepository,
            UserRepository userRepository
    ) {
        this.tripRepository = tripRepository;
        this.driverRepository = driverRepository;
        this.truckRepository = truckRepository;
        this.userRepository = userRepository;
    }

    public List<TripResponse> getAllTrips() {
        return tripRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TripResponse getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        return mapToResponse(trip);
    }
    public List<TripResponseDriver> getTripsByDriverId(Long driverId) {
        Optional<User> user = userRepository.findById(driverId);
        Optional<Driver> driver = driverRepository.findByUser(user);
        List<Trip> trips = tripRepository.findByDriver_Id(driver.get().getId());
        return trips.stream().map(this::mapToTripsDriverResponse).toList();
    }
    @Transactional
    public TripResponse createTrip(TripRequest request) {
        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Truck truck = truckRepository.findById(request.getTruckId())
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        Trip trip = Trip.builder()
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .price(request.getPrice())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(request.getStatus())
                .driver(driver)
                .truck(truck)
                .build();

        return mapToResponse(tripRepository.save(trip));
    }

    public TripResponse updateStatusOnly(Long id, UpdateTripStatusRequest request) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        trip.setStatus(request.getStatus());

        Trip updated = tripRepository.save(trip);

        return mapToResponse(updated);
    }

    @Transactional
    public TripResponse updateTrip(Long id, TripRequest request) {
        System.out.println("Updating trip....... in service");
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        System.out.println("getting trip....... in service");
        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        System.out.println("getting driver....... in service");
        Truck truck = truckRepository.findById(request.getTruckId())
                .orElseThrow(() -> new RuntimeException("Truck not found"));
        System.out.println("getting truck....... in service");
        trip.setOrigin(request.getOrigin());
        trip.setDestination(request.getDestination());
        trip.setPrice(request.getPrice());
        trip.setStartTime(request.getStartTime());
        trip.setEndTime(request.getEndTime());
        trip.setStatus(request.getStatus());
        trip.setDriver(driver);
        trip.setTruck(truck);

        return mapToResponse(tripRepository.save(trip));
    }

    public long getTripCount() {
        return tripRepository.count();
    }
    public double getTotalRevenue() {
        return tripRepository.getTotalCompletedTripRevenue() != null
                ? tripRepository.getTotalCompletedTripRevenue()
                : 0.0;
    }

    // get monthly service
    public List<Map<String, Object>> getMonthlyRevenue() {
        List<Object[]> results = tripRepository.getMonthlyRevenue();
        String[] months = { "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec" };

        return results.stream().map(obj -> {
            int monthIndex = (int) obj[0];
            Double revenue = (Double) obj[1];
            Map<String, Object> map = new HashMap<>();
            map.put("month", months[monthIndex]);
            map.put("revenue", revenue);
            return map;
        }).collect(Collectors.toList());
    }


    @Transactional
    public void deleteTrip(Long id) {
        if (!tripRepository.existsById(id)) {
            throw new RuntimeException("Trip not found");
        }
        tripRepository.deleteById(id);
    }
    private TripResponse mapToResponse(Trip trip) {
        return TripResponse.builder()
                .id(trip.getId())
                .origin(trip.getOrigin())
                .destination(trip.getDestination())
                .price(trip.getPrice())
                .status(trip.getStatus())
                .startTime(trip.getStartTime())
                .endTime(trip.getEndTime())
                .createdAt(trip.getCreatedAt())
                .driver(
                        DriverInfo.builder()
                                .id(trip.getDriver().getId())
                                .driverName(trip.getDriver().getUser().getName())
                                .build()
                )
                .truck(
                        TruckInfo.builder()
                                .id(trip.getTruck().getId())
                                .truckModel(trip.getTruck().getModel())
                                .build()
                )
                .build();
    }

    private TripResponseDriver mapToTripsDriverResponse(Trip trip) {
        return TripResponseDriver.builder()
                .id(trip.getId())
                .truckPlateNumber(trip.getTruck().getPlateNumber())
                .driverName(trip.getDriver().getUser().getName())
                .origin(trip.getOrigin())
                .destination(trip.getDestination())
                .price(trip.getPrice())
                .status(trip.getStatus())
                .startTime(trip.getStartTime())
                .endTime(trip.getEndTime())
                .build();
    }

}
