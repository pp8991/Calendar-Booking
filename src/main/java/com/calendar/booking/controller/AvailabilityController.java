package com.calendar.booking.controller;

import com.calendar.booking.data.Availability;
import com.calendar.booking.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/availabilities")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Availability>> getAvailabilitiesForOwner(@PathVariable("ownerId") String ownerId) {
        List<Availability> availabilities = availabilityService.getAllAvailabilitiesForOwner(ownerId);
        if (availabilities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(availabilities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Availability> getAvailabilityById(@PathVariable("id") String id) {
        Optional<Availability> availability = availabilityService.getAvailabilityById(id);
        return availability.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Availability> createAvailability(@RequestBody Availability availability) {
        try {
            Availability createdAvailability = availabilityService.createAvailability(availability);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAvailability);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Availability> updateAvailability(
            @PathVariable("id") String id,
            @RequestBody Availability availability) {
        try {
            availabilityService.updateAvailability(id, availability);
            return ResponseEntity.ok(availability);
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