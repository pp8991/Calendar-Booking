package com.calendar.booking.controller.admin;

import com.calendar.booking.data.TimeSlot;
import com.calendar.booking.data.TimeSlotResponse;
import com.calendar.booking.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;

    @GetMapping("/owner/{ownerEmail}/available")
    public ResponseEntity<List<TimeSlotResponse>> getAvailableTimeSlots(
            @PathVariable("ownerEmail") String ownerEmail,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        return ResponseEntity.ok(timeSlotService.getAvailableTimeSlots(ownerEmail, date));
    }

    @PostMapping("/create")
    public ResponseEntity<TimeSlot> createTimeSlot(@RequestBody TimeSlotResponse timeSlotResponse) {
        try {
            TimeSlot createdSlot = timeSlotService.createTimeSlot(timeSlotResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSlot);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{timeSlotId}/book")
    public ResponseEntity<Void> markTimeSlotAsBooked(@PathVariable("timeSlotId") String timeSlotId) {
        try {
            timeSlotService.markTimeSlotAsBooked(timeSlotId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{timeSlotId}/unbook")
    public ResponseEntity<Void> unmarkTimeSlotAsBooked(@PathVariable("timeSlotId") String timeSlotId) {
        try {
            timeSlotService.unmarkTimeSlotAsBooked(timeSlotId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

