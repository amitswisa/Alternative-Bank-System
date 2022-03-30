package dto;

public class DataTransferObject extends Throwable {

    private final String message;

    public DataTransferObject(String message) {
        this.message = message;
    }

    // Get Data Transfer Object message.
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
