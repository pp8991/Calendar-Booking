package com.calendar.booking.repository;

import com.calendar.booking.data.Appointment;
import com.calendar.booking.data.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
}
