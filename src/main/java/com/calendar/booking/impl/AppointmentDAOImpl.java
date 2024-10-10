package com.calendar.booking.impl;

import com.calendar.booking.dao.AppointmentDAO;
import com.calendar.booking.data.Appointment;
import com.calendar.booking.repository.AppointmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class AppointmentDAOImpl implements AppointmentDAO {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Appointment> findByOwnerIdAndDate(String ownerId, LocalDate date) {
        String jpql = "SELECT a FROM Appointment a WHERE a.timeSlot.owner.id = :ownerId AND FUNCTION('DATE', a.createdAt) = :date";
        TypedQuery<Appointment> query = entityManager.createQuery(jpql, Appointment.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("date", date);
        return query.getResultList();
    }


    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public void deleteById(String id) {
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> findAllByOwnerId(String ownerId) {
    }

    public Appointment findById(int appointmentId) {
    }
}
