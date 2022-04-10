package abs;

import xmlgenerated.AbsLoan;

import java.util.*;
import java.util.stream.Collectors;

public class BankLoan {



    public enum Status {
        PENDING,
        ACTIVE,
        RISK,
        FINISHED
    }

    private String owner;
    private String loanID;
    private String loanCategory;
    private final int loanAmount; // Loan amount of money.
    private final int loanOpeningTime; // Time in YAZ that customer opened new loan.
    private int loanTotalTime;
    private int loanStartTime; // in yaz - set value when being active.
    private int loanInterestPerPayment;
    private int paymentInterval; // Time in yaz for every customer payment.(ex: every 2 yaz etc...)
    private Map<String, Investor> loanInvestors; // List of investors and their amount of investment;
    private Status loanStatus;
    private List<BankLoanTransaction> transactionList; // hold all transaction's history.

    public BankLoan(AbsLoan absLoan) {
        this.owner = absLoan.getAbsOwner();
        this.loanID = absLoan.getId();
        this.loanCategory = absLoan.getAbsCategory();
        this.loanAmount = absLoan.getAbsCapital();
        this.loanTotalTime = absLoan.getAbsTotalYazTime();
        this.loanOpeningTime = 1;
        this.loanStartTime = 0;
        this.loanInterestPerPayment = absLoan.getAbsIntristPerPayment();
        this.paymentInterval = absLoan.getAbsPaysEveryYaz();
        this.loanInvestors = new HashMap<>();
        this.loanStatus = Status.PENDING;
        transactionList = new ArrayList<>(5);
    }

    // Return next payment time for current loan in Yaz.
    public int getNextPaymentTime(){

       int paymentTime = this.loanStartTime + this.paymentInterval;
       while(paymentTime <= BankSystem.getCurrentYaz())
           paymentTime += this.paymentInterval;

       return paymentTime; // default values for unfound objects after filter.

    }

    public int invest(BankCustomer customer , int InvestmentAmount) {

       int totalInvested = 0;
       if(loanInvestors.get(customer.getName()) != null) {
           totalInvested += loanInvestors.get(customer.getName()).getInitialInvestment();
           loanInvestors.remove(customer.getName());
       }
       int amountToActivate = this.getAmountLeftToActivateLoan();

       // If some investment covers loan's funds so activate it.
      if(InvestmentAmount >= amountToActivate){
          totalInvested += amountToActivate;
          this.addInvestor(customer,totalInvested, amountToActivate);
          //Change status loan
          this.loanStartTime = BankSystem.getCurrentYaz();
          this.loanStatus = Status.ACTIVE;
          return amountToActivate; //return the amount actually success to invest
      }

      totalInvested += InvestmentAmount;
      this.addInvestor(customer,totalInvested, InvestmentAmount);
      return InvestmentAmount;
    }

    private void addInvestor(BankCustomer customer, int totalInvested, int currentInvest) {

        customer.addInvestment(this, totalInvested, currentInvest);

        //Amount of capital the customer get per payment.
        int capital = totalInvested / (this.loanTotalTime / this.paymentInterval);
        //Amount of interest the customer get per payment.
        int interest = (capital * this.loanInterestPerPayment) / 100;

        loanInvestors.put(customer.getName(),new Investor(customer, capital, interest, totalInvested));
    }

    public String getOwner() {
        return owner;
    }

    public String getLoanID() {
        return loanID;
    }

    public String getLoanCategory() {
        return loanCategory;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public int getLoanTotalTime() {
        return loanTotalTime;
    }

    public int getLoanStartTime() {
        return loanStartTime;
    }

    public int getLoanInterestPerPayment() {
        return loanInterestPerPayment;
    }

    public int getPaymentInterval() {
        return paymentInterval;
    }

    public Map<String, Investor> getLoanInvestors() {
        return loanInvestors;
    }

    public Status getLoanStatus() {
        return loanStatus;
    }

    public List<BankLoanTransaction> getTransactionList() {
        return transactionList;
    }

    public int getLoanOpeningTime() {
        return this.loanOpeningTime;
    }

    // Return list of all transactions that already payed before current YAZ.
    public List<BankLoanTransaction> getPayedTransactions() {
        return this.transactionList.stream().filter(s -> s.getTransactionStatus() == BankLoanTransaction.Status.PAYED
                && s.getPaymentTime() <= BankSystem.getCurrentYaz()).collect(Collectors.toList());
    }

    // Return list of all transactions that didnt payed yet before current YAZ.
    public List<BankLoanTransaction> getUnpayedTransactions() {
        return this.transactionList.stream().filter(s -> s.getTransactionStatus() == BankLoanTransaction.Status.NOT_PAYED
                && s.getPaymentTime() <= BankSystem.getCurrentYaz()).collect(Collectors.toList());
    }

    // Return total unpaied transactions amount of money.
    public int getUnpayedTransactionsAmountOfMoney() {
        List<BankLoanTransaction> unPayedTransactions = this.getUnpayedTransactions();

        // if all transactions alreadty paied.
        if(unPayedTransactions == null)
            return 0;

        return unPayedTransactions.stream().mapToInt(e->e.getPaymentValue()+e.getInterestValue()).sum();
    }

    // Return loan interest total value.
    public int getTotalLoanInterest() {
        return ((this.loanAmount*this.getLoanInterestPerPayment())/100);
    }

    // Return total loan amount minus existing invesments money.
    public int getAmountLeftToActivateLoan() {
        return this.getLoanAmount() - loanInvestors.values().stream().mapToInt(e -> e.getInitialInvestment()).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankLoan bankLoan = (BankLoan) o;
        return loanID.equals(bankLoan.loanID);
    }

    @Override
    public int hashCode() {
        return loanID.hashCode();
    }
}
