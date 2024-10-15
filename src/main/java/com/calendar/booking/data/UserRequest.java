package com.calendar.booking.data;

import lombok.Data;

@Data
public class UserRequest {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
}
