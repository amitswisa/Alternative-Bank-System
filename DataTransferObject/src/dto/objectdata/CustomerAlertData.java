package dto.objectdata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dto.infodata.DataTransferObject;

import java.lang.reflect.Type;

public class CustomerAlertData extends DataTransferObject {

    public enum Type {
        CONFIRMATION, MESSAGE, INFORMATION, ALERT;
    }

    private final Type alertType;
    private int readStatus;
    private final String headline;

    public CustomerAlertData(String headline, String message, int yazTime, Type alertType)
    {
        super(message, yazTime);
        this.alertType = alertType;
        this.readStatus = 1;
        this.headline = headline;
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

    public String getHeadline() {
        return this.headline;
    }

    // Adapter for json serialize.
    public static class CustomerAlertDataAdapter implements JsonSerializer<CustomerAlertData> {

        @Override
        public JsonElement serialize(CustomerAlertData customerAlertData, java.lang.reflect.Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("alertType", customerAlertData.getAlertType().toString());
            jsonObject.addProperty("readStatus", customerAlertData.getReadStatus());
            jsonObject.addProperty("headline", customerAlertData.getHeadline());
            return jsonObject;
        }
    }
}
