package dto.infodata;

import abs.BankSystem;

public class DataTransferObject extends Throwable {

    private final String message;
    private final int timeInYaz;

    public DataTransferObject() {
        this.message = "";
        this.timeInYaz = BankSystem.getCurrentYaz();
    }

    public DataTransferObject(String message) {
        this.message = message;
        this.timeInYaz = BankSystem.getCurrentYaz();
    }

    public DataTransferObject(String message, int timeInYaz) {
        this.message = message;
        this.timeInYaz = timeInYaz;
    }

    // Get Data Transfer Object message.
    public String getMessage() {
        return this.message;
    }

    public int getTimeInYaz() {
        return this.timeInYaz;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
