package electronicstore.model.exceptions;

public class UnauthorizedAccessException extends Exception {
    private String requiredLevel;

    public UnauthorizedAccessException(String requiredLevel) {
        super("Unauthorized access. Required level: " + requiredLevel);
        this.requiredLevel = requiredLevel;
    }

    public String getRequiredLevel() {
        return requiredLevel;
    }
}