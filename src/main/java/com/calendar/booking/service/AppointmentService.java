package com.calendar.booking.service;

import com.calendar.booking.data.Appointment;
import com.calendar.booking.data.Availability;
import com.calendar.booking.data.TimeSlot;
import com.calendar.booking.data.User;
import com.calendar.booking.impl.AppointmentDAOImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private AvailabilityService availabilityService;

    @Transactional
    public List<Appointment> getAppointmentsForOwner(String ownerId) {
        return appointmentDAO.findAllByOwnerId(ownerId);
    }

    @Transactional
    public List<Appointment> getAppointmentsForOwnerByDate(String ownerId, LocalDate date) {
        return appointmentDAO.findByOwnerIdAndDate(ownerId, date);
    }

    @Transactional
    public Appointment createAppointment(String ownerEmail,
                                         String startTime,
                                         String endTime,
                                         List<String> inviteeEmails,
                                         LocalDate date) {
        User owner = userService.findOrCreateUserByEmail(ownerEmail);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start = LocalTime.parse(startTime, timeFormatter);
        LocalTime end = LocalTime.parse(endTime, timeFormatter);

        LocalDateTime startDateTime = date.atTime(start);
        LocalDateTime endDateTime = date.atTime(end);

        TimeSlot timeSlot = saveTimeSlot(owner, startDateTime, endDateTime);
        Appointment appointment = createOwnerAppointment(inviteeEmails, owner, timeSlot);
        return appointmentDAO.save(appointment);
    }

    private Appointment createOwnerAppointment(List<String> inviteeEmails, User owner, TimeSlot timeSlot) {
        Appointment appointment = new Appointment();
        appointment.setOwner(owner);
        appointment.setTimeSlot(timeSlot);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setStatus("Scheduled");

        List<User> invitees = inviteeEmails.stream()
                .map(email -> userService.findOrCreateUserByEmail(email))
                .toList();

        appointment.setInvitees(invitees);
        return appointment;
    }

    private TimeSlot saveTimeSlot(User owner, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        TimeSlot existingTimeSlot = timeSlotService.findByOwnerAndTimeRange(owner.getId(), startDateTime, endDateTime);

        TimeSlot timeSlot;
        if (existingTimeSlot == null) {
            timeSlot = new TimeSlot();
            timeSlot.setOwner(owner);
            timeSlot.setStartTime(startDateTime);
            timeSlot.setEndTime(endDateTime);
            timeSlot.setBooked(false);

            timeSlotService.saveTimeSlot(timeSlot);
        } else {
            timeSlot = existingTimeSlot;
        }


        if (timeSlot.isBooked()) {
            throw new RuntimeException("Time slot is already booked.");
        }

        timeSlot.setBooked(true);
        timeSlotService.saveTimeSlot(timeSlot);
        return timeSlot;
    }

    private void saveAvailability(LocalDate date, User owner, LocalTime start, LocalTime end) {
        Availability availability = new Availability();
        availability.setOwner(owner);
        availability.setDayOfWeek(date.getDayOfWeek());
        availability.setStartTime(start);
        availability.setEndTime(end);
        availabilityService.saveAvailability(availability);
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
