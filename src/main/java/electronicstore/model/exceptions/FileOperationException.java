package electronicstore.model.exceptions;

public class FileOperationException extends Exception {
    private String filename;
    private String operation;

    public FileOperationException(String filename, String operation, String message) {
        super("File operation failed: " + operation + " on " + filename + ". " + message);
        this.filename = filename;
        this.operation = operation;
    }

    public String getFilename() {
        return filename;
    }

    public String getOperation() {
        return operation;
    }
}