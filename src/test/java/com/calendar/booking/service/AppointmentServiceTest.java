package com.calendar.booking.service;

import com.calendar.booking.data.Appointment;
import com.calendar.booking.data.TimeSlot;
import com.calendar.booking.data.User;
import com.calendar.booking.impl.AppointmentDAOImpl;
import com.calendar.booking.impl.UserDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentDAOImpl appointmentDAO;

    @Mock
    private TimeSlotService timeSlotService;

    @Mock
    private UserService userService;

    @Mock
    private AvailabilityService availabilityService;

    @InjectMocks
    private AppointmentService appointmentService;
    @Mock
    private UserDAOImpl userDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAppointmentsForOwner() {
        String ownerId = "owner123";
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();
        List<Appointment> mockAppointments = Arrays.asList(appointment1, appointment2);

        when(appointmentDAO.findAllByOwnerId(ownerId)).thenReturn(mockAppointments);
        List<Appointment> result = appointmentService.getAppointmentsForOwner(ownerId);
        assertEquals(2, result.size());
        verify(appointmentDAO, times(1)).findAllByOwnerId(ownerId);
    }

    @Test
    void testCreateAppointment() {
        String ownerEmail = "prakharpandey@gmail.com";
        String startTime = "09:00";
        String endTime = "10:00";
        List<String> inviteeEmails = Arrays.asList("maggarwal@dealmeridian.com", "pp8991@gmail.com");
        LocalDate date = LocalDate.of(2024, 10, 18);

        User owner = new User();
        owner.setEmail(ownerEmail);
        when(userService.findOrCreateUserByEmail(ownerEmail)).thenReturn(owner);

        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.of(9, 0));
        LocalDateTime endDateTime = LocalDateTime.of(date, LocalTime.of(10, 0));
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(startDateTime);
        timeSlot.setEndTime(endDateTime);
        timeSlot.setOwner(owner);
        timeSlot.setBooked(false);
        when(timeSlotService.findByOwnerAndTimeRange(anyString(), any(), any())).thenReturn(null);
        when(timeSlotService.saveTimeSlot(any(TimeSlot.class))).thenReturn(timeSlot);

        Appointment appointment = new Appointment();
        appointment.setOwner(owner);
        when(appointmentDAO.save(any(Appointment.class))).thenReturn(appointment);
        Appointment result = appointmentService.createAppointment(ownerEmail, startTime, endTime, inviteeEmails, date);
        assertNotNull(result);
        assertEquals(owner, result.getOwner());
        verify(userService, times(1)).findOrCreateUserByEmail(ownerEmail);
        verify(timeSlotService, times(2)).saveTimeSlot(any(TimeSlot.class));
        verify(appointmentDAO, times(1)).save(any(Appointment.class));
    }

    @Test
    void testCancelAppointment() {
        String appointmentId = "appointment123";
        Appointment appointment = new Appointment();
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId("timeslot123");
        appointment.setTimeSlot(timeSlot);

        when(appointmentDAO.findById(appointmentId)).thenReturn(Optional.of(appointment));
        appointmentService.cancelAppointment(appointmentId);
        assertEquals("Canceled", appointment.getStatus());
        verify(timeSlotService, times(1)).unmarkTimeSlotAsBooked(timeSlot.getId());
        verify(appointmentDAO, times(1)).save(appointment);
    }

    @Test
    void testAddInviteesToAppointment() {
        String appointmentId = "appointment123";
        Appointment appointment = new Appointment();
        User existingInvitee = new User();
        existingInvitee.setEmail("prakharpandey96@gmail.com");
        appointment.setInvitees(List.of(existingInvitee));

        List<String> newInviteesEmails = Arrays.asList("maggarwal@dealmeridian.com", "pp8991@gmail.com");

        User newInvitee1 = new User();
        newInvitee1.setEmail("maggarwal@dealmeridian.com");
        User newInvitee2 = new User();
        newInvitee2.setEmail("pp8991@gmail.com");

        when(userDAO.findByEmail("prakharpandey96@gmail.com")).thenReturn(Optional.of(existingInvitee));
        when(appointmentDAO.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(userService.findOrCreateUserByEmail("maggarwal@dealmeridian.com")).thenReturn(newInvitee1);
        when(userService.findOrCreateUserByEmail("pp8991@gmail.com")).thenReturn(newInvitee2);
        appointmentService.addInviteesToAppointment(appointmentId, newInviteesEmails);
        assertEquals(3, appointment.getInvitees().size());
        verify(appointmentDAO, times(1)).save(appointment);
    }

    @Test
    void testFindById() {
        String appointmentId = "appointment123";
        Appointment appointment = new Appointment();
        when(appointmentDAO.findById(appointmentId)).thenReturn(Optional.of(appointment));
        Optional<Appointment> result = appointmentService.findById(appointmentId);
        assertTrue(result.isPresent());
        assertEquals(appointment, result.get());
        verify(appointmentDAO, times(1)).findById(appointmentId);
    }
}