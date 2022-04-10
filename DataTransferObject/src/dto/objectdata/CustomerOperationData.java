package dto.objectdata;

import dto.infodata.DataTransferObject;

public class CustomerOperationData extends DataTransferObject {

    private final String operationName;
    private final int currentOptAmount; // Represents current transaction value, (should be displayed as abs value).
    private final int balanceBefore;
    private final int balanceAfter;

    public CustomerOperationData(String operationName, String message, int balance, int currentOptAmount) {
        super(message);
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
    public String toString() {
        return "      Operation date: " + this.getTimeInYaz() +".\n      Amount: "
                +Math.abs(getCurrentOptAmount())+".\n      Operation: " + getOperationName()
                        + ".\n      Balance before operation: " + this.getBalanceBefore()
                             + ".\n      Balance after operation: "+this.getBalanceAfter();
    }
}
