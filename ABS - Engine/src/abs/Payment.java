package abs;

public class Payment {

    public enum Status {
        UN_PAID,
        UN_PAID_CASUE_RISK,
        PAID
    }

    private int capital;
    private int interest;
    private Status status;

    public Payment(int capital, int interest) {
        this.capital = capital;
        this.interest = interest;
        this.status = Status.UN_PAID;
    }

    public int getCapital() {
        return capital;
    }

    public int getInterest() {
        return interest;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
