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
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentDAOImpl appointmentDAO;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private UserService userService;

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
        Optional<TimeSlot> timeSlot1 = timeSlotService.findTimeSlotById(timeSlotId);
        if (!timeSlot1.isPresent()) throw new RuntimeException("Invalid Time Slot Id");
        TimeSlot timeSlot = timeSlot1.get();
        if (timeSlot.isBooked()) {
            throw new RuntimeException("Time slot is not available.");
        }

        timeSlotService.markTimeSlotAsBooked(timeSlotId);

        Optional<User> invitee = userService.findById(inviteeId);
        if (!invitee.isPresent()) {
            throw new RuntimeException("Invitee not found.");
        }

        // Create and save the appointment
        Appointment appointment = new Appointment();
        appointment.setInvitee(invitee.get());
        appointment.setTimeSlot(timeSlot);
        appointment.setCreatedAt(LocalDate.now().atStartOfDay());
        return appointmentDAO.save(appointment);
    }

    @Transactional
    public void cancelAppointment(String appointmentId) {
        Optional<Appointment> appointment = appointmentDAO.findById(appointmentId);
        if (!appointment.isPresent()) {
            throw new RuntimeException("Appointment not found.");
        }

        // Free the associated time slot
        TimeSlot timeSlot = appointment.get().getTimeSlot();
        timeSlotService.unmarkTimeSlotAsBooked(timeSlot.getId());

        // Delete the appointment
        appointmentDAO.deleteById(appointmentId);
    }
}
