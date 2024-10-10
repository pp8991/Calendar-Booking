package com.calendar.booking.dao;

import com.calendar.booking.data.Availability;

import java.util.List;
import java.util.Optional;

public interface AvailabilityDAO {
    List<Availability> findAll();
    Optional<Availability> findById(String id);
    Availability save(Availability availability);
    void deleteById(String id);
    List<Availability> findByOwnerId(String ownerId);
}
