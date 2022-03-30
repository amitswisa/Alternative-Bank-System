package dto;

public class DataTransferObject extends Throwable {

    private final String message;
    private final int timeInYaz;

    public DataTransferObject(String message) {
        this.message = message;
        this.timeInYaz = 0;
    }

    public DataTransferObject(String message, int timeInYaz) {
        this.message = message;
        this.timeInYaz = timeInYaz;
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
