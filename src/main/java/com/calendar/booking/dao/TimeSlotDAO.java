package com.calendar.booking.dao;

import com.calendar.booking.data.TimeSlot;

import java.util.List;
import java.util.Optional;

public interface TimeSlotDAO {
    List<TimeSlot> findAll();
    Optional<TimeSlot> findById(String id);
    TimeSlot save(TimeSlot timeSlot);
    void deleteById(String id);
}
