package dto;

public class AbsDataTransferObject {

    private final String message;

    public AbsDataTransferObject(String message) {
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
