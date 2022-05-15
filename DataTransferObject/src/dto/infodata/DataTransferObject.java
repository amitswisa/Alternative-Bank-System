package dto.infodata;

import abs.BankSystem;

public class DataTransferObject extends Throwable {

    private final String message;
    private final int timeInYaz;
    private final boolean isSuccess;

    public DataTransferObject() {
        this.message = "";
        this.timeInYaz = BankSystem.getCurrentYaz();
        this.isSuccess = false;
    }

    public DataTransferObject(String message) {
        this.message = message;
        this.timeInYaz = BankSystem.getCurrentYaz();
        this.isSuccess = false;
    }

    public DataTransferObject(String message, boolean isSuccess) {
        this.message = message;
        this.timeInYaz = BankSystem.getCurrentYaz();
        this.isSuccess = isSuccess;

    }

    public DataTransferObject(String message, int timeInYaz) {
        this.message = message;
        this.timeInYaz = timeInYaz;
        this.isSuccess = false;
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

    public boolean isSuccess() {
        return isSuccess;
    }
}
