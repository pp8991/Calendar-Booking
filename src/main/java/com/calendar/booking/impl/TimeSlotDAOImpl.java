package com.calendar.booking.impl;

import com.calendar.booking.dao.TimeSlotDAO;
import com.calendar.booking.data.TimeSlot;
import com.calendar.booking.repository.TimeSlotRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TimeSlotDAOImpl implements TimeSlotDAO {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TimeSlot> findAll() {
        TypedQuery<TimeSlot> query = entityManager.createQuery("SELECT t FROM TimeSlot t", TimeSlot.class);
        return query.getResultList();
    }

    @Override
    public Optional<TimeSlot> findById(String id) {
        return timeSlotRepository.findById(id);
    }

    @Override
    public TimeSlot save(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }

    @Override
    public void deleteById(String id) {
       timeSlotRepository.deleteById(id);
    }

    @Override
    public TimeSlot findByOwnerIdAndStartTimeAndEndTime(String ownerId, LocalDateTime startTime, LocalDateTime endTime) {
        TypedQuery<TimeSlot> query = entityManager.createQuery(
                "SELECT t FROM TimeSlot t WHERE t.owner.id = :ownerId AND t.startTime = :startTime AND t.endTime = :endTime",
                TimeSlot.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        List<TimeSlot> result = query.getResultList();
        return result.isEmpty() ? null : result.getFirst();
    }

    @Override
    public List<TimeSlot> findByOwnerIdAndDate(String ownerId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        TypedQuery<TimeSlot> query = entityManager.createQuery(
                "SELECT ts FROM TimeSlot ts WHERE ts.owner.id = :ownerId AND ts.startTime BETWEEN :startOfDay AND :endOfDay",
                TimeSlot.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("startOfDay", startOfDay);
        query.setParameter("endOfDay", endOfDay);

        return query.getResultList();
    }
}
