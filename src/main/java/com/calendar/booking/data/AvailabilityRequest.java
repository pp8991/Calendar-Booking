package com.calendar.booking.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityRequest {
    private String ownerEmail;
    private String dayOfWeek;
    private String startTime;
    private String endTime;

}
