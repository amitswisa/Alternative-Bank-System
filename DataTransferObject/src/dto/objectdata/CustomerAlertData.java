package dto.objectdata;

import com.google.gson.*;
import dto.infodata.DataTransferObject;

public class CustomerAlertData extends DataTransferObject {

    private int readStatus;
    private final String headline;

    public CustomerAlertData(String headline, String message, int yazTime)
    {
        super(message, yazTime);
        this.readStatus = 1;
        this.headline = headline;
    }

    public void markAsRead() {
        this.readStatus = 0;
    }

    public boolean isAlertGotRead() {
        return (this.readStatus == 0);
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
            // Fetch only if not fetched before.
            if(customerAlertData.getReadStatus() == 1) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("timeInYaz", customerAlertData.getTimeInYaz());
                jsonObject.addProperty("readStatus", customerAlertData.getReadStatus());
                jsonObject.addProperty("headline", customerAlertData.getHeadline());
                return jsonObject;
            }

            return null;
        }
    }
}
