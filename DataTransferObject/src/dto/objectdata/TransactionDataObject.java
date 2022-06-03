package dto.objectdata;

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
}
