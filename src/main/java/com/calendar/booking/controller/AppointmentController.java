package com.calendar.booking.controller;

import com.calendar.booking.data.Appointment;
import com.calendar.booking.data.Role;
import com.calendar.booking.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    // Get all appointments for an invitee
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByInviteeId(@PathVariable String inviteeId,
                                                                        @PathVariable Role role) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByInviteeId(inviteeId, role));
    }

    // Create a new appointment
    @PostMapping("/invitee/{inviteeId}/time-slot/{timeSlotId}")
    public ResponseEntity<Appointment> createAppointment(@PathVariable String inviteeId,
                                                         @PathVariable String timeSlotId,
                                                         @RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.createAppointment(inviteeId, timeSlotId, appointment));
    }

    // Update appointment status
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, appointment));
    }

    // Delete an appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
