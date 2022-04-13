package abs;

import java.util.ArrayList;
import java.util.List;

public class Investor {

    private BankCustomer investor;
    private int initialInvestment;
    private int currentDebt;
    private int capital;
    private int interest;
    List<Payment> listOfPayments;

    public Investor(BankCustomer investor, int capital, int interest, int initialInvestment) {
        this.investor = investor;
        this.capital = capital;
        this.interest = interest;
        this.initialInvestment = initialInvestment;
        this.currentDebt = initialInvestment;
        listOfPayments = new ArrayList<>();
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

    // Algoritem to divide payments equally as possible.
    public void initialPaymentList(BankLoan loan) {

        int lengthOfPayment = loan.getLoanNumberOfPayments();

        // Calculate initial interet and payment leftovers.
        int reminderPerPayment = (this.getInitialInvestment() % lengthOfPayment) / lengthOfPayment;
        int reminderOfInterest = (this.getInitialInvestment() % lengthOfPayment) / lengthOfPayment;

        for(int i = 0;i<lengthOfPayment;i++) {

            // Make sure the whole amount will be paied and if neccessry add leftover to last payment.
            int paymentValue = this.getInitialInvestment() / lengthOfPayment;
            int paymentInterest = (paymentValue * loan.getLoanInterestPerPayment()) / 100;

            // If too small for current amount of left payment
            // -> keep calculating until find number of payment to divide it between them equally.
            if(leftOverFromPaymentValue == 0)
                leftOverFromPaymentValue = (loan.getLoanAmount() % lengthOfPayment) / (lengthOfPayment-i);

            if(leftOverFromInterestValue == 0)
                leftOverFromInterestValue = (loan.getTotalLoanInterestInMoney() % lengthOfPayment) / (lengthOfPayment-i);

            // Divide equally leftovers from payment.
            paymentValue += leftOverFromPaymentValue;
            paymentInterest += leftOverFromInterestValue;


            loan.getTransactionList().add(i,
                    new BankLoanTransaction(loan.getLoanStartTime() + (loan.getPaymentInterval()*(i+1)),paymentValue, paymentInterest, BankLoanTransaction.Status.NOT_PAYED));
        }
    }
}
