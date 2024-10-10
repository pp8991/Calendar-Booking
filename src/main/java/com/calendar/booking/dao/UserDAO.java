package com.calendar.booking.dao;

import com.calendar.booking.data.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> findAll();
    Optional<User> findById(String id);
    User save(User user);
    void deleteById(String id);
}
