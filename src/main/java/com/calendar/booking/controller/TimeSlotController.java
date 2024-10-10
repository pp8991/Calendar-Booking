package com.calendar.booking.controller;

import com.calendar.booking.data.TimeSlot;
import com.calendar.booking.service.TimeSlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-slots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    // Get all available time slots for a specific user (Calendar Owner)
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<TimeSlot>> getTimeSlotsByOwnerId(@PathVariable Long ownerId) {
        return ResponseEntity.ok(timeSlotService.getTimeSlotsByOwnerId(ownerId));
    }

    // Create a new time slot for an owner
    @PostMapping("/owner/{ownerId}")
    public ResponseEntity<TimeSlot> createTimeSlot(@PathVariable Long ownerId, @RequestBody TimeSlot timeSlot) {
        return ResponseEntity.ok(timeSlotService.createTimeSlot(ownerId, timeSlot));
    }

    // Update a time slot (for example, to mark it as booked)
    @PutMapping("/{id}")
    public ResponseEntity<TimeSlot> updateTimeSlot(@PathVariable Long id, @RequestBody TimeSlot timeSlot) {
        return ResponseEntity.ok(timeSlotService.updateTimeSlot(id, timeSlot));
    }

    // Delete a time slot
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable Long id) {
        timeSlotService.deleteTimeSlot(id);
        return ResponseEntity.noContent().build();
    }
}

