package com.calendar.booking.service;

import com.calendar.booking.constants.Keys;
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

import static com.calendar.booking.constants.Keys.TIME_FORMATTER;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityDAOImpl availabilityDAO;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveAvailability(Availability availability){
        availabilityDAO.save(availability);
    }

    public List<AvailabilityRequest> getAllAvailabilitiesForOwner(String ownerEmail, LocalDate date) {
        User owner = userService.findOrCreateUserByEmail(ownerEmail);
        List<Availability> availabilities = availabilityDAO.findByOwnerIdAndDate(owner.getId(), date);
        return availabilities.stream()
                .map(availability -> new AvailabilityRequest(
                        availability.getOwner().getEmail(),
                        availability.getDayOfWeek().toString(),
                        availability.getStartTime().toString(),
                        availability.getEndTime().toString()
                ))
                .toList();
    }

    public List<Availability> getOwnerAvailabilityOnDate(String ownerEmail, LocalDate date){
        User owner = userService.findOrCreateUserByEmail(ownerEmail);
        return availabilityDAO.findByOwnerIdAndDate(owner.getId(), date);
    }

    @Transactional
    public void createAvailability(AvailabilityRequest request) {
        User owner = userService.findOrCreateUserByEmail(request.getOwnerEmail());
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(request.getDayOfWeek().toUpperCase());

        LocalTime startTime = LocalTime.parse(request.getStartTime(), TIME_FORMATTER);
        LocalTime endTime = LocalTime.parse(request.getEndTime(), TIME_FORMATTER);

        Availability availability = new Availability();
        availability.setOwner(owner);
        availability.setDayOfWeek(dayOfWeek);
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);

        validateAvailability(availability);
    }

    @Transactional
    public void updateAvailability(AvailabilityRequest request) {
        User owner = userService.findOrCreateUserByEmail(request.getOwnerEmail());
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(request.getDayOfWeek().toUpperCase());

        LocalTime startTime = LocalTime.parse(request.getStartTime(), TIME_FORMATTER);
        LocalTime endTime = LocalTime.parse(request.getEndTime(), TIME_FORMATTER);

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

    private void validateAvailability(Availability newAvailability) {
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
