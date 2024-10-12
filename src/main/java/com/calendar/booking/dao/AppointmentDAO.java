package com.calendar.booking.dao;

import com.calendar.booking.data.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentDAO {
    public List<Appointment> findByOwnerIdAndDate(String ownerId, LocalDate date);
    public List<Appointment> findAll();
    public Appointment save(Appointment appointment);
    public void deleteById(String id);
    public List<Appointment> findAllByOwnerId(String ownerId);
    public Optional<Appointment> findById(String appointmentId);
}
