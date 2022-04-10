package abs;

public class Investor {

    private BankCustomer investor;
    private int initialInvestment;
    private int currentDebt;
    private int capital;
    private int interest;

    public Investor(BankCustomer investor, int capital, int interest, int initialInvestment) {

        this.investor = investor;
        this.capital = capital;
        this.interest = interest;
        this.initialInvestment = initialInvestment;
        this.currentDebt = initialInvestment;
    }

    public BankCustomer getInvestor() {
        return investor;
    }

    public int getCapital() {
        return capital;
    }

    public int getInterest() {
        return interest;
    }

    public int getInitialInvestment() {
        return initialInvestment;
    }

    public void setInitialInvestment(int initialInvestment) {
        this.initialInvestment = initialInvestment;
    }
}
