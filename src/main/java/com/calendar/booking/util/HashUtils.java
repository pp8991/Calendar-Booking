package com.calendar.booking.util;

import org.apache.commons.codec.binary.Base64;

public class HashUtils {

    public static String encode(String stringToEncode) {
        return Base64.encodeBase64String(stringToEncode.getBytes());
    }

    public static String decode(String encodedString) {
        byte[] decodedBytes = Base64.decodeBase64(encodedString);
        return new String(decodedBytes);
    }
}
