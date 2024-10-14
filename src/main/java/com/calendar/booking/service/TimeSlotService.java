package com.calendar.booking.service;

import com.calendar.booking.data.Appointment;
import com.calendar.booking.data.Availability;
import com.calendar.booking.data.TimeSlot;
import com.calendar.booking.data.User;
import com.calendar.booking.impl.AppointmentDAOImpl;
import com.calendar.booking.impl.TimeSlotDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotDAOImpl timeSlotDAO;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private AppointmentDAOImpl appointmentDAO;

    @Transactional
    public List<TimeSlot> getAvailableTimeSlots(String ownerId, LocalDateTime date) {
        List<Availability> availabilities = availabilityService.getAllAvailabilitiesForOwner(ownerId);

        List<TimeSlot> timeSlots = generateTimeSlots(availabilities, date);

        // Filter out booked time slots
        List<Appointment> appointments = appointmentDAO.findByOwnerIdAndDate(ownerId, date.toLocalDate());
        List<TimeSlot> bookedSlots = appointments.stream()
                .map(Appointment::getTimeSlot)
                .collect(Collectors.toList());

        return timeSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }

    private List<TimeSlot> generateTimeSlots(List<Availability> availabilities, LocalDateTime date) {
        // Generate time slots based on availability and interval rules (e.g., 60-minute intervals)
        return availabilities.stream()
                .filter(availability -> availability.getDayOfWeek().equals(date.getDayOfWeek().name()))
                .flatMap(availability -> {
                    LocalTime startTime = LocalTime.from(availability.getStartTime());
                    LocalTime endTime = LocalTime.from(availability.getEndTime());
                    return generateSlotsForDay(startTime, endTime, date).stream();
                })
                .collect(Collectors.toList());
    }

    private List<TimeSlot> generateSlotsForDay(LocalTime startTime, LocalTime endTime, LocalDateTime date) {
        List<TimeSlot> slots = new ArrayList<>();
        LocalDateTime currentSlotStart = date.with(startTime);
        while (currentSlotStart.toLocalTime().isBefore(endTime)) {
            LocalDateTime currentSlotEnd = currentSlotStart.plusHours(1);
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(currentSlotStart);
            slot.setEndTime(currentSlotEnd);
            slots.add(slot);
            currentSlotStart = currentSlotEnd;
        }
        return slots;
    }

    @Transactional
    public TimeSlot createTimeSlot(TimeSlot timeSlot) {
        return timeSlotDAO.save(timeSlot);
    }

    @Transactional
    public void markTimeSlotAsBooked(String timeSlotId) {
        Optional<TimeSlot> timeSlot = timeSlotDAO.findById(timeSlotId);
        if (!timeSlot.isPresent()) {
            throw new RuntimeException("Time slot not found");
        }
        timeSlot.get().setBooked(true);
        timeSlotDAO.save(timeSlot.get());
    }


    public Optional<TimeSlot> findTimeSlotById(String timeSlotId) {
        return timeSlotDAO.findById(timeSlotId);
    }

    public void unmarkTimeSlotAsBooked(String id) {
        Optional<TimeSlot> timeSlot = timeSlotDAO.findById(id);
        if (timeSlot.isPresent() && timeSlot.get().isBooked()) {
            timeSlot.get().setBooked(false);
            timeSlotDAO.save(timeSlot.get());
        } else {
            throw new RuntimeException("TimeSlot not found or is not booked");
        }
    }
}

