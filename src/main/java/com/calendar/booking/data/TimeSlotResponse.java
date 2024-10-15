package com.calendar.booking.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotResponse {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean booked;
}
