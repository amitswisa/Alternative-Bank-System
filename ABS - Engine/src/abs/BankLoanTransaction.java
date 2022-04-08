package abs;

public class BankLoanTransaction {

    enum Status {
        PAYED,
        NOT_PAYED
    }

    private final int paymentTime; // Displayed in yaz.
    private final int paymentValue; // Amount of payment.
    private final int interestValue; // Amount of interest.
    private Status transactionStatus;

    public BankLoanTransaction(int paymentTime, int paymentValue, int interestValue, Status transactionStatus) {
        this.paymentTime = paymentTime;
        this.paymentValue = paymentValue;
        this.interestValue = interestValue;
        this.transactionStatus = transactionStatus;
    }

    public int getPaymentTime() {
        return paymentTime;
    }

    public int getPaymentValue() {
        return paymentValue;
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

    @Override
    public String toString() {
        return "Payment time: " + this.paymentTime + "\nPayment Value: " + this.paymentValue
                + "\nPayment Interest: " + this.interestValue + "\nToal payment: " + (this.paymentValue+this.interestValue);
    }
}
