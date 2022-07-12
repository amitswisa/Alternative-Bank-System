package dto.objectdata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dto.infodata.DataTransferObject;

import java.lang.reflect.Type;

public class CustomerOperationData extends DataTransferObject {

    private final String operationName;
    private final int currentOptAmount; // Represents current transaction value, (should be displayed as abs value).
    private final int balanceBefore;
    private final int balanceAfter;

    public CustomerOperationData(String operationName, String message, int balance, int currentOptAmount) {
        super();
        this.operationName = operationName;
        this.currentOptAmount = currentOptAmount;
        this.balanceAfter = currentOptAmount + balance;
        this.balanceBefore = balance;
    }

    public int getCurrentOptAmount() {
        return currentOptAmount;
    }

    public int getBalanceBefore() {
        return balanceBefore;
    }

    public int getBalanceAfter() {
        return balanceAfter;
    }

    public String getOperationName() {
        return this.operationName;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return "      Operation date: " + this.getTimeInYaz() +".\n      Amount: "
                +Math.abs(getCurrentOptAmount())+".\n      Operation: " + getOperationName()
                        + ".\n      Balance before operation: " + this.getBalanceBefore()
                             + ".\n      Balance after operation: "+this.getBalanceAfter();
    }

    // Adapter for serialize this object into json object.
    public static class CustomerOperationDataAdapter implements JsonSerializer<CustomerOperationData> {

        @Override
        public JsonElement serialize(CustomerOperationData customerOperationData, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("operationName", customerOperationData.getOperationName());
            jsonObject.addProperty("currentOptAmount", customerOperationData.getCurrentOptAmount());
            jsonObject.addProperty("balanceBefore", customerOperationData.getBalanceBefore());
            jsonObject.addProperty("balanceAfter", customerOperationData.getBalanceAfter());
            return jsonObject;
        }
    }
}
