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

    public int getPaymentAmount() {
        return this.getCapital() + this.getInterest();
    }

    public void addFundsToInvestment(int investment, int capital, int interest) {
        this.initialInvestment += investment;
        this.currentDebt += investment;
        this.capital += capital;
        this.interest += interest;
    }

    public void setInitialInvestment(int initialInvestment) {
        this.initialInvestment = initialInvestment;
    }

    public int getMyShareInInvestment(int originalLoanAmount) {
        return (this.getPaymentAmount() * 100 / originalLoanAmount);
    }

}
