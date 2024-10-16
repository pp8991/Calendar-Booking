package com.calendar.booking.dao;

import com.calendar.booking.data.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotDAO {
    List<TimeSlot> findAll();
    Optional<TimeSlot> findById(String id);
    TimeSlot save(TimeSlot timeSlot);
    void deleteById(String id);
    TimeSlot findByOwnerIdAndStartTimeAndEndTime(String ownerId, LocalDateTime startTime, LocalDateTime endTime);

    List<TimeSlot> findByOwnerIdAndDate(String ownerId, LocalDate date);
}
