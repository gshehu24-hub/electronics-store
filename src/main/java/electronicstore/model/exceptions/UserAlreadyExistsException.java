package electronicstore.model.exceptions;

public class UserAlreadyExistsException extends Exception {
    private String username;

    public UserAlreadyExistsException(String username) {
        super("User with username " + username + " already exists.");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}