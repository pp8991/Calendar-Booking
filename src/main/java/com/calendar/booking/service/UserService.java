package com.calendar.booking.service;

import com.calendar.booking.data.User;
import com.calendar.booking.data.UserRequest;
import com.calendar.booking.exceptions.UserAlreadyExistsException;
import com.calendar.booking.impl.UserDAOImpl;
import com.calendar.booking.util.AppUtil;
import io.micrometer.common.util.StringUtils;
import io.netty.util.internal.StringUtil;
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

    public User createUser(UserRequest userRequest) {
        if (userDAO.findAll().stream().anyMatch(u -> u.getEmail().equals(userRequest.getEmail()))) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUserName());
        user.setPassword(userRequest.getPassword());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setMobile(userRequest.getMobile());
        return userDAO.save(user);
    }

    public User updateUser(String id, UserRequest userRequest) {
        User existingUser = getUserById(id);
        if(!StringUtils.isBlank(userRequest.getFirstName())){
            existingUser.setFirstName(userRequest.getFirstName());
        }
        if(!StringUtils.isBlank(userRequest.getLastName())){
            existingUser.setLastName(userRequest.getLastName());
        }
        if(!StringUtils.isBlank(userRequest.getMobile())){
            existingUser.setMobile(userRequest.getMobile());
        }
        if(!StringUtils.isBlank(userRequest.getPassword())){
            existingUser.setPassword(userRequest.getPassword());
        }
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
