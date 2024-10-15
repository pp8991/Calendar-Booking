package com.calendar.booking.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "time_slots")
@Getter
@Setter
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_booked", nullable = false)
    private boolean isBooked = false;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}

