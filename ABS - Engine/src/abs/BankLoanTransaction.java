package abs;

public class BankLoanTransaction {

    enum Status {
        PAYED,
        DEPT_COVERED,
        NOT_PAYED
    }

    private final int paymentTime; // Displayed in yaz.
    private int paymentValue; // Amount of payment.
    private int interestValue; // Amount of interest.
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

    public void addDebt(int capital, int interest) {
        paymentValue += capital;
        interestValue += interest;
    }

    @Override
    public String toString() {
        return "Payment time: " + this.paymentTime + "\nPayment Value: " + this.paymentValue
                + "\nPayment Interest: " + this.interestValue + "\nToal payment: " + (this.paymentValue+this.interestValue);
    }
}
