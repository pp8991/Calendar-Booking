package com.calendar.booking.service;

import com.calendar.booking.data.Appointment;
import com.calendar.booking.data.TimeSlot;
import com.calendar.booking.data.User;
import com.calendar.booking.impl.AppointmentDAOImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentDAOImpl appointmentDAO;

    @Autowired
    private TimeSlotService timeSlotService;

    @Transactional
    public List<Appointment> getAppointmentsForOwner(String ownerId) {
        return appointmentDAO.findAllByOwnerId(ownerId);
    }

    @Transactional
    public List<Appointment> getAppointmentsForOwnerByDate(String ownerId, LocalDate date) {
        return appointmentDAO.findByOwnerIdAndDate(ownerId, date);
    }

    @Transactional
    public Appointment bookAppointment(String inviteeId, String timeSlotId) {
        TimeSlot timeSlot = timeSlotService.findTimeSlotById(timeSlotId);

        if (timeSlot == null || timeSlot.isBooked()) {
            throw new RuntimeException("Time slot is not available.");
        }

        timeSlotService.markTimeSlotAsBooked(timeSlotId);

        // Create and save the appointment
        Appointment appointment = new Appointment();
        appointment.setInvitee(new User(inviteeId));
        appointment.setTimeSlot(timeSlot);
        appointment.setCreatedAt(LocalDate.now().atStartOfDay());
        return appointmentDAO.save(appointment);
    }

    @Transactional
    public void cancelAppointment(int appointmentId) {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("Appointment not found.");
        }

        // Free the associated time slot
        TimeSlot timeSlot = appointment.getTimeSlot();
        timeSlotService.unmarkTimeSlotAsBooked(timeSlot.getId());

        // Delete the appointment
        appointmentDAO.deleteById(appointmentId);
    }
}
