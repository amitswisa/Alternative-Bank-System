package abs;

public class Investor {

    private BankCustomer investor;
    private int initialInvestment;
    private int currentDebt;
    private int capital;
    private int interest;
    private boolean isSell;

    public Investor(BankCustomer investor, int capital, int interest, int initialInvestment) {

        this.investor = investor;
        this.capital = capital;
        this.interest = interest;
        this.initialInvestment = initialInvestment;
        this.currentDebt = initialInvestment;
        this.isSell = false;
    }

    public BankCustomer getInvestor() {
        return investor;
    }

    public void setInvestor(BankCustomer newInvestor) {
        this.investor = newInvestor;
        this.setIsSell();
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

    public int getMyShareInInvestment(int oneTimeLoanPayment) {
        return (this.getPaymentAmount() * 100 / oneTimeLoanPayment); // 4120 * 100 / 4120
    }

    public boolean getIsSell() {
        return isSell;
    }

    public String setIsSell() {

        this.isSell = !(this.isSell);

        if(this.isSell)
            return "Cancel Sell";

        return "Sell Share";
    }

    public void resetIsSell() {
        this.isSell = false;
    }
}
