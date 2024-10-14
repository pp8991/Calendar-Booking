package com.calendar.booking.util;

import org.springframework.stereotype.Component;

@Component
public class AppUtil {

    public static String getCustomDefaultPassword(String userEmail){
        return HashUtils.encode(userEmail);
    }
}
