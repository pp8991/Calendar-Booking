//package com.calendar.booking.data;
//
//import jakarta.persistence.*;
//import org.apache.catalina.User;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//
//@Entity
//public class CalendarEvent {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "owner_id", nullable = false)
//    private User calendarOwner;
//
//    private String eventName;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
//
//    @ManyToMany(mappedBy = "invitedEvents")
//    private Set<User> invitees;
//}
