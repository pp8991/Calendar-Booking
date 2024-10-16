package com.calendar.booking.service;

import com.calendar.booking.data.Availability;
import com.calendar.booking.data.AvailabilityRequest;
import com.calendar.booking.data.User;
import com.calendar.booking.exceptions.OverlappingAvailabilityException;
import com.calendar.booking.impl.AvailabilityDAOImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityDAOImpl availabilityDAO;
    @Autowired
    private UserService userService;

    public List<Availability> getAllAvailabilitiesForOwner(String ownerEmail, LocalDate date) {
        User owner = userService.findOrCreateUserByEmail(ownerEmail);
        return availabilityDAO.findByOwnerIdAndDate(owner.getId(), date);
    }

    @Transactional
    public void createAvailability(AvailabilityRequest request) {
        User owner = userService.findOrCreateUserByEmail(request.getOwnerEmail());
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(request.getDayOfWeek().toUpperCase());

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("[hh:mm a][HH:mm]");
        LocalTime startTime = LocalTime.parse(request.getStartTime(), timeFormatter);
        LocalTime endTime = LocalTime.parse(request.getEndTime(), timeFormatter);

        Availability availability = new Availability();
        availability.setOwner(owner);
        availability.setDayOfWeek(dayOfWeek);
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);

        validateAndCombineAvailability(availability);
    }

    @Transactional
    public void updateAvailability(AvailabilityRequest request) {
        User owner = userService.findOrCreateUserByEmail(request.getOwnerEmail());
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(request.getDayOfWeek().toUpperCase());

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("[hh:mm a][HH:mm]");
        LocalTime startTime = LocalTime.parse(request.getStartTime(), timeFormatter);
        LocalTime endTime = LocalTime.parse(request.getEndTime(), timeFormatter);

        Availability newAvailability = new Availability();
        newAvailability.setOwner(owner);
        newAvailability.setDayOfWeek(dayOfWeek);
        newAvailability.setStartTime(startTime);
        newAvailability.setEndTime(endTime);

        List<Availability> existingAvailabilities = availabilityDAO.findByOwnerId(owner.getId());
        List<Availability> overlappingAvailabilities = new ArrayList<>();

        for (Availability availability : existingAvailabilities) {
            if (availability.getDayOfWeek().equals(dayOfWeek) &&
                    (newAvailability.getStartTime().isBefore(availability.getEndTime()) &&
                            newAvailability.getEndTime().isAfter(availability.getStartTime()))) {
                overlappingAvailabilities.add(availability);
            }
        }

        if (!overlappingAvailabilities.isEmpty()) {
            for (Availability availability : overlappingAvailabilities) {
                availabilityDAO.deleteById(availability.getId());
            }
        }

        availabilityDAO.save(newAvailability);
    }

    @Transactional
    public void deleteAvailability(String id) {
        availabilityDAO.deleteById(id);
    }

    private void validateAndCombineAvailability(Availability newAvailability) {
        List<Availability> existingAvailabilities = availabilityDAO.findByOwnerId(newAvailability.getOwner().getId());

        for (Availability availability : existingAvailabilities) {
            if (availability.getDayOfWeek().equals(newAvailability.getDayOfWeek()) &&
                    (newAvailability.getStartTime().isBefore(availability.getEndTime()) &&
                            newAvailability.getEndTime().isAfter(availability.getStartTime()))) {
                throw new OverlappingAvailabilityException("OverLapping Availability Found Please Update the Availability");
            }
        }
        availabilityDAO.save(newAvailability);
    }

}
