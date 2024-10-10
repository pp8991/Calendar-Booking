package com.calendar.booking.service;

import com.calendar.booking.dao.UserDAO;
import com.calendar.booking.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public User getUserById(String id) {
        Optional<User> user = userDAO.findById(id);
        if(user.isEmpty()){
            throw new RuntimeException("User Not Found");
        }
        return user.get();
    }

    public User createUser(User user) {
        if (userDAO.findAll().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new RuntimeException("User with this email already exists");
        }
        return userDAO.save(user);
    }

    public User updateUser(String id, User user) {
        User existingUser = getUserById(id);
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setMobile(user.getMobile());
        return userDAO.save(existingUser);
    }

    public void deleteUser(String id) {
        userDAO.deleteById(id);
    }
}
