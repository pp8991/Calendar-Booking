package com.calendar.booking.repository;

import com.calendar.booking.data.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, String> {
    List<Availability> findByOwnerId(String ownerId);

    @Transactional
    @Modifying
    @Query("delete from Availability a")
    int deleteFirstBy();
}

