package dto.objectdata;

import dto.infodata.DataTransferObject;

public class CustomerAlertData extends DataTransferObject {

    public enum Type {
        CONFIRMATION, MESSAGE, INFORMATION, ALERT;
    }

    private final Type alertType;
    private int readStatus;

    public CustomerAlertData(String message, int yazTime, Type alertType)
    {
        super(message, yazTime);
        this.alertType = alertType;
        this.readStatus = 1;
    }

    public void markAsRead() {
        this.readStatus = 0;
    }

    public boolean isAlertGotRead() {
        return (this.readStatus == 0);
    }

    public Type getAlertType() {
        return alertType;
    }

    public int getReadStatus() {
        return readStatus;
    }
}