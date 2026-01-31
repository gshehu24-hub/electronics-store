package electronicstore.model.utilities;

import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");

    public static boolean validateEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    public static boolean validatePassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean validateUsername(String username) {
        return username != null && username.length() >= 4 && username.matches("[A-Za-z0-9]+");
    }

    public static boolean validatePositiveNumber(double number) {
        return number > 0;
    }

    public static boolean validateQuantity(int quantity) {
        return quantity >= 0;
    }
}