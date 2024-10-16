package com.calendar.booking.exceptions;

public class OverlappingAvailabilityException extends RuntimeException{
    public OverlappingAvailabilityException(String message) {
        super(message);
    }
}
