package com.calendar.booking.controller.admin;

import com.calendar.booking.data.Appointment;
import com.calendar.booking.service.AppointmentService;
import com.calendar.booking.service.TimeSlotService;
import com.calendar.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TimeSlotService timeSlotService;

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Appointment>> getAppointmentsForOwner(@PathVariable String ownerId) {
        List<Appointment> appointments = appointmentService.getAppointmentsForOwner(ownerId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}/date/{date}")
    public ResponseEntity<List<Appointment>> getAppointmentsForOwnerByDate(@PathVariable String ownerId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Appointment> appointments = appointmentService.getAppointmentsForOwnerByDate(ownerId, date);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }


    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestParam String ownerEmail, @RequestParam String timeSlotId, @RequestBody List<String> inviteeEmails) {
        Appointment appointment = appointmentService.createAppointment(ownerEmail, timeSlotId, inviteeEmails);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @DeleteMapping("/cancel/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable String appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{appointmentId}/invitees")
    public ResponseEntity<Object> addInviteesToAppointment(@PathVariable String appointmentId, @RequestBody List<String> inviteeEmails) {
        appointmentService.addInviteesToAppointment(appointmentId, inviteeEmails);
        return new ResponseEntity<>("Invitee Added", HttpStatus.OK);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable String appointmentId) {
        Optional<Appointment> appointment = appointmentService.findById(appointmentId);
        return appointment.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}