package dto.infoholder;

import dto.DataTransferObject;

public class CustomerOperationData extends DataTransferObject {

    private final int currentOptAmount; // Represents current transaction value, (should be displayed as abs value).
    private final int balanceBefore;
    private final int balanceAfter;

    public CustomerOperationData(int timeInYaz, String message, int balance, int currentOptAmount) {
        super(message, timeInYaz);
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
}
