package dto.infodata;


public class DataTransferObject extends Throwable {

    private final String message;
    private final int timeInYaz;
    private final boolean isSuccess;

//TODO check with amit that is correct logic
    public DataTransferObject() {
        this.message = "";
        this.timeInYaz = 0;
        this.isSuccess = false;
    }

    public DataTransferObject(int currentYaz) {
        this.message = "";
        this.timeInYaz = currentYaz;
        this.isSuccess = false;
    }

    public DataTransferObject(String message, int currentYaz) {
        this.message = message;
        this.timeInYaz = currentYaz;
        this.isSuccess = false;
    }

    public DataTransferObject(String message, boolean isSuccess, int currentYaz) {
        this.message = message;
        this.timeInYaz = currentYaz;
        this.isSuccess = isSuccess;

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
