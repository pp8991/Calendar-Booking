package com.calendar.booking.controller.admin;

import com.calendar.booking.data.Availability;
import com.calendar.booking.data.AvailabilityRequest;
import com.calendar.booking.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/availabilities")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @GetMapping("/owner/{ownerEmail}")
    public ResponseEntity<List<Availability>> getAvailabilitiesForOwner(@PathVariable("ownerEmail") String ownerEmail,
                                                                        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        List<Availability> availabilities = availabilityService.getAllAvailabilitiesForOwner(ownerEmail, date);
        if (availabilities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(availabilities);
    }

    @PostMapping("/create")
    public ResponseEntity<Availability> createAvailability(@RequestBody AvailabilityRequest availabilityRequest) {
        try {
            availabilityService.createAvailability(availabilityRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<AvailabilityRequest> updateAvailability(@RequestBody AvailabilityRequest availabilityRequest) {
        try {
            availabilityService.updateAvailability(availabilityRequest);
            return ResponseEntity.ok(availabilityRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable("id") String id) {
        try {
            availabilityService.deleteAvailability(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}