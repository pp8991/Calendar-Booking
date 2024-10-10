package com.calendar.booking.impl;

import com.calendar.booking.dao.AvailabilityDAO;
import com.calendar.booking.data.Availability;
import com.calendar.booking.repository.AvailabilityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class AvailabilityDAOImpl implements AvailabilityDAO {
    @Autowired
    private AvailabilityRepository availabilityRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Availability> findAll() {
        return availabilityRepository.findAll();
    }

    @Override
    public Optional<Availability> findById(String id) {
        return availabilityRepository.findById(id);
    }

    @Override
    public Availability save(Availability availability) {
        return availabilityRepository.save(availability);
    }

    @Override
    public void deleteById(String id) {
        availabilityRepository.deleteById(id);
    }

    @Override
    public List<Availability> findByOwnerId(String ownerId) {
        TypedQuery<Availability> query = entityManager.createQuery("SELECT a FROM Availability a WHERE a.owner.id = :ownerId", Availability.class);
        query.setParameter("ownerId", ownerId);
        return query.getResultList();
    }
}
