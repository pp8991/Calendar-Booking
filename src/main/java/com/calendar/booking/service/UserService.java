package com.calendar.booking.service;

import com.calendar.booking.data.User;
import com.calendar.booking.impl.UserDAOImpl;
import com.calendar.booking.util.AppUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final String DEFAULT_PASSWORD = "";

    @Autowired
    private UserDAOImpl userDAO;

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


    public boolean deleteUser(String id) {
        Optional<User> userOptional = userDAO.findById(id);
        if (userOptional.isPresent()) {
            userDAO.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<User> findById(String userId){
        return userDAO.findById(userId);
    }

    @Transactional
    public User findOrCreateUserByEmail(String email) {
        Optional<User> existingUser = userDAO.findByEmail(email);

        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setPassword(AppUtil.getCustomDefaultPassword(email));
            return userDAO.save(newUser);
        }
    }
}
