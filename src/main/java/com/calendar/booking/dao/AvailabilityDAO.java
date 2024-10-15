package com.calendar.booking.dao;

import com.calendar.booking.data.Appointment;
import com.calendar.booking.data.Availability;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AvailabilityDAO {
    List<Availability> findAll();
    Optional<Availability> findById(String id);
    Availability save(Availability availability);
    void deleteById(String id);
    List<Availability> findByOwnerIdAndDate(String ownerId, LocalDate date);
    void update(String id, Availability availability);
    List<Availability> findByOwnerId(String ownerId);
}
