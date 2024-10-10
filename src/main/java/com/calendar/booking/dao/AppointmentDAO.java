package com.calendar.booking.dao;

import com.calendar.booking.data.Appointment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentDAO {
    public List<Appointment> findByOwnerIdAndDate(String ownerId, LocalDate date);
    public List<Appointment> findAll();
    public Appointment save(Appointment appointment);
    public void deleteById(String id);
}
