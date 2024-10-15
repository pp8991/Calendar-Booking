package com.calendar.booking.service;

import com.calendar.booking.data.Appointment;
import com.calendar.booking.data.TimeSlot;
import com.calendar.booking.data.User;
import com.calendar.booking.impl.AppointmentDAOImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Appointment createAppointment(String ownerEmail, String timeSlotId, List<String> inviteeEmails) {
        TimeSlot timeSlot = timeSlotService.findTimeSlotById(timeSlotId)
                .orElseThrow(() -> new RuntimeException("Invalid Time Slot Id"));
        if (timeSlot.isBooked()) {
            throw new RuntimeException("Time slot is not available.");
        }

        timeSlotService.markTimeSlotAsBooked(timeSlotId);
        User owner = userService.findOrCreateUserByEmail(ownerEmail);

        Appointment appointment = new Appointment();
        appointment.setOwner(owner);
        appointment.setTimeSlot(timeSlot);
        appointment.setCreatedAt(LocalDateTime.now());

        List<User> invitees = inviteeEmails.stream()
                .map(email -> userService.findOrCreateUserByEmail(email))
                .collect(Collectors.toList());

        appointment.setInvitees(invitees);
        appointment.setStatus("Scheduled");

        return appointmentDAO.save(appointment);
    }

    @Transactional
    public void addInviteesToAppointment(String appointmentId, List<String> inviteeEmails) {
        Appointment appointment = appointmentDAO.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found."));

        List<User> newInvitees = inviteeEmails.stream()
                .map(email -> userService.findOrCreateUserByEmail(email))
                .toList();

        appointment.getInvitees().addAll(newInvitees);

        appointmentDAO.save(appointment);
    }

    @Transactional
    public void cancelAppointment(String appointmentId) {
        Appointment appointment = appointmentDAO.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found."));
        TimeSlot timeSlot = appointment.getTimeSlot();
        timeSlotService.unmarkTimeSlotAsBooked(timeSlot.getId());
        appointment.setStatus("Canceled");
        appointment.setCanceledAt(LocalDateTime.now());
        appointmentDAO.save(appointment);
    }

    public Optional<Appointment> findById(String appointmentId) {
        return appointmentDAO.findById(appointmentId);
    }
}
