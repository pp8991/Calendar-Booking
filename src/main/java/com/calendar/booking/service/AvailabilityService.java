package com.calendar.booking.service;

import com.calendar.booking.data.Availability;
import com.calendar.booking.impl.AvailabilityDAOImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityDAOImpl availabilityDAO;

    public List<Availability> getAllAvailabilities() {
        return availabilityDAO.findAll();
    }

    public Availability getAvailabilityById(String id) {
        return availabilityDAO.findById(id);
    }

    public List<Availability> getAllAvailabilitiesForOwner(String ownerId) {
        return availabilityDAO.findByOwnerId(ownerId);
    }

    @Transactional
    public Availability createAvailability(Availability availability) {
        validateAvailability(availability);
        return availabilityDAO.save(availability);
    }

    @Transactional
    public Availability updateAvailability(String id, Availability availability) {
        Availability existingAvailability = getAvailabilityById(id);
        existingAvailability.setDayOfWeek(availability.getDayOfWeek());
        existingAvailability.setStartTime(availability.getStartTime());
        existingAvailability.setEndTime(availability.getEndTime());
        return availabilityDAO.save(existingAvailability);
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
                throw new RuntimeException("Availability overlaps with an existing one");
            }
        }
    }
}
