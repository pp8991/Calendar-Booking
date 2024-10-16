package com.calendar.booking.service;

import com.calendar.booking.data.*;
import com.calendar.booking.impl.AppointmentDAOImpl;
import com.calendar.booking.impl.TimeSlotDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotDAOImpl timeSlotDAO;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private AppointmentDAOImpl appointmentDAO;

    @Autowired
    private UserService userService;

    @Transactional
    public List<TimeSlotResponse> getAvailableTimeSlots(String ownerEmail, LocalDate date) {
        User owner = userService.findOrCreateUserByEmail(ownerEmail);
        List<AvailabilityRequest> availabilities = availabilityService.getAllAvailabilitiesForOwner(ownerEmail, date);

        List<TimeSlot> timeSlots = availabilities.isEmpty() ? generateDefaultTimeSlots(date) : generateTimeSlots(availabilities, date);

        List<Appointment> appointments = appointmentDAO.findByOwnerIdAndDate(owner.getId(), date);
        List<TimeSlot> bookedSlots = appointments.stream()
                .map(Appointment::getTimeSlot)
                .toList();

        Set<TimeSlot> bookedSlotsSet = new HashSet<>(bookedSlots);
        timeSlots.forEach(slot -> slot.setBooked(bookedSlotsSet.contains(slot)));
        return timeSlots.stream()
                .map(slot -> new TimeSlotResponse(slot.getStartTime(), slot.getEndTime(), slot.isBooked()))
                .toList();
    }

    private List<TimeSlot> generateDefaultTimeSlots(LocalDate date) {
        List<TimeSlot> defaultSlots = new ArrayList<>();
        LocalDateTime startTime = date.atTime(9, 0);
        LocalDateTime endTime = date.atTime(17, 0);

        while (startTime.isBefore(endTime)) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(startTime);
            slot.setEndTime(startTime.plusHours(1));
            defaultSlots.add(slot);
            startTime = startTime.plusHours(1);
        }

        return defaultSlots;
    }

    private List<TimeSlot> generateTimeSlots(List<AvailabilityRequest> availabilities, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return availabilities.stream()
                .filter(availability -> availability.getDayOfWeek().equals(dayOfWeek.toString()))
                .flatMap(availability -> {
                    LocalTime startTime = LocalTime.parse(availability.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime endTime = LocalTime.parse(availability.getEndTime(), DateTimeFormatter.ofPattern("HH:mm"));
                    return generateSlotsForDay(startTime, endTime, date).stream();
                })
                .toList();
    }


    private List<TimeSlot> generateSlotsForDay(LocalTime startTime, LocalTime endTime, LocalDate date) {
        List<TimeSlot> slots = new ArrayList<>();
        LocalDateTime currentSlotStart = date.atTime(startTime);
        while (currentSlotStart.toLocalTime().isBefore(endTime)) {
            TimeSlot slot = new TimeSlot();
            slot.setStartTime(currentSlotStart);
            slot.setEndTime(currentSlotStart.plusHours(1));
            slots.add(slot);
            currentSlotStart = currentSlotStart.plusHours(1);
        }
        return slots;
    }

    @Transactional
    public TimeSlot createTimeSlot(TimeSlotResponse timeSlotResponse) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setBooked(timeSlotResponse.isBooked());
        timeSlot.setStartTime(timeSlotResponse.getStartTime());
        timeSlot.setEndTime(timeSlotResponse.getEndTime());
        return timeSlotDAO.save(timeSlot);
    }

    @Transactional
    public void markTimeSlotAsBooked(String timeSlotId) {
        TimeSlot timeSlot = timeSlotDAO.findById(timeSlotId).orElseThrow(() -> new RuntimeException("Time slot not found"));
        timeSlot.setBooked(true);
        timeSlotDAO.save(timeSlot);
    }

    public Optional<TimeSlot> findTimeSlotById(String timeSlotId) {
        return timeSlotDAO.findById(timeSlotId);
    }

    @Transactional
    public void unmarkTimeSlotAsBooked(String id) {
        TimeSlot timeSlot = timeSlotDAO.findById(id).orElseThrow(() -> new RuntimeException("TimeSlot not found or is not booked"));
        if (timeSlot.isBooked()) {
            timeSlot.setBooked(false);
            timeSlotDAO.save(timeSlot);
        } else {
            throw new RuntimeException("TimeSlot is not booked");
        }
    }
}

