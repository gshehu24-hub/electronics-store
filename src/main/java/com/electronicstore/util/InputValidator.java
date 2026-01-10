package com.electronicstore.util;

public class InputValidator {
    public static boolean isValidUsername(String u) { return u != null && !u.isBlank(); }
}
