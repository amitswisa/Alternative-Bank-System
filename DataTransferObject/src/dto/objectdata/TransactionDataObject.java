package dto.objectdata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class TransactionDataObject {

    public enum Status {
        PAYED,
        DEPT_COVERED,
        NOT_PAYED
    }

    private final int paymentTime; // Displayed in yaz.
    private int paymentValue; // Amount of payment.
    private int interestValue; // Amount of interest.
    private int totalPaid;
    private Status transactionStatus;

    public TransactionDataObject(int paymentTime, int paymentValue, int interestValue, Status transactionStatus) {
        this.paymentTime = paymentTime;
        this.paymentValue = paymentValue;
        this.interestValue = interestValue;
        this.transactionStatus = transactionStatus;
        this.totalPaid = 0;
    }

    public int getPaymentTime() {
        return paymentTime;
    }

    public int getPaymentValue() {
        return paymentValue;
    }

    public int getTotalPayment() {
        return this.getPaymentValue() + this.getInterestValue() - totalPaid;
    }

    public int getInterestValue() {
        return interestValue;
    }

    public Status getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Status transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public int getTotalPaid() {
        return this.totalPaid;
    }

    public void addDebt(int capital, int interest) {
        paymentValue += capital;
        interestValue += interest;
    }

    public void pay(int amount) {
        this.totalPaid += amount;
        if(isPaid())
            this.setTransactionStatus(Status.PAYED);
    }

    public boolean isPaid() {
        return (totalPaid >= (this.getPaymentValue() + this.getInterestValue()));
    }

    @Override
    public String toString() {
        return "   Payment time: " + this.paymentTime + "\n   Payment Value: " + this.paymentValue
                + "\n   Payment Interest: " + this.interestValue + "\n   Toal payment: " + (this.paymentValue+this.interestValue);
    }

    public static class TransactionDataObjectAdapter implements JsonSerializer<TransactionDataObject> {

        @Override
        public JsonElement serialize(TransactionDataObject transactionDataObject, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject json = new JsonObject();
            json.addProperty("paymentTime", transactionDataObject.getPaymentTime());
            json.addProperty("paymentValue", transactionDataObject.getPaymentValue());
            json.addProperty("interestValue", transactionDataObject.getInterestValue());
            json.addProperty("totalPaid", transactionDataObject.getTotalPaid());
            json.addProperty("transactionStatus", transactionDataObject.getTransactionStatus().toString());
            return json;
        }
    }
}
