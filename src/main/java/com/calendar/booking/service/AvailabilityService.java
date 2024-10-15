package com.calendar.booking.service;

import com.calendar.booking.data.Availability;
import com.calendar.booking.impl.AvailabilityDAOImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityDAOImpl availabilityDAO;

    public List<Availability> getAllAvailabilitiesForOwner(String ownerId, LocalDate date) {
        return availabilityDAO.findByOwnerIdAndDate(ownerId, date);
    }

    @Transactional
    public Availability createAvailability(Availability availability) {
        validateAvailability(availability);
        return availabilityDAO.save(availability);
    }

    @Transactional
    public void updateAvailability(String id, Availability availability) {
        validateAvailability(availability);
        availabilityDAO.update(id, availability);
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
