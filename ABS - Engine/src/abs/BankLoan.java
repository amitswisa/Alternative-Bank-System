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

    private String loanID;
    private String loanCategory;
    private final int loanAmount; // Loan amount of money.
    private int loanTotalTime;
    private int loanStartTime; // in yaz - set value when being active.
    private int loanInterestPerPayment;
    private int paymentInterval; // Time in yaz for every customer payment.(ex: every 2 yaz etc...)
    private Map<String, Integer> loanInvestors; // List of investors and their amount of investment;
    private Status loanStatus;
    private List<BankLoanTransaction> transactionList; // hold all transaction's history.

    public BankLoan(AbsLoan absLoan) {
        this.loanID = absLoan.getId();
        this.loanCategory = absLoan.getAbsCategory();
        this.loanAmount = absLoan.getAbsCapital();
        this.loanTotalTime = absLoan.getAbsTotalYazTime();
        this.loanStartTime = 0;
        this.loanInterestPerPayment = absLoan.getAbsIntristPerPayment();
        this.paymentInterval = absLoan.getAbsPaysEveryYaz();
        this.loanInvestors = new HashMap<>();
        this.loanStatus = Status.PENDING;
        transactionList = new ArrayList<>(5);
    }

    // Return next payment time for current loan in Yaz.
    public int getNextPaymentTime(){

        // filter list and return new list with objects that their nextPayment is greater then current Yaz and still not payed.
       List<BankLoanTransaction> tempList = (List<BankLoanTransaction>) transactionList.stream().filter(s -> s.getTransactionStatus()== BankLoanTransaction.Status.NOT_PAYED
               && s.getPaymentTime() >= BankSystem.getCurrentYaz());

       if(tempList.get(0) != null)
           return tempList.get(0).getPaymentTime();

       return 0; // default values for unfound objects after filter.

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

    public Map<String, Integer> getLoanInvestors() {
        return loanInvestors;
    }

    public Status getLoanStatus() {
        return loanStatus;
    }

    public List<BankLoanTransaction> getTransactionList() {
        return transactionList;
    }

    // Return list of all transactions that already payed before current YAZ.
    public List<BankLoanTransaction> getPayedTransactions() {
        return (List<BankLoanTransaction>) this.transactionList.stream().filter(s -> s.getTransactionStatus() == BankLoanTransaction.Status.PAYED
                && s.getPaymentTime() <= BankSystem.getCurrentYaz());
    }

    // Return list of all transactions that didnt payed yet before current YAZ.
    public List<BankLoanTransaction> getUnpayedTransactions() {
        return (List<BankLoanTransaction>) this.transactionList.stream().filter(s -> s.getTransactionStatus() == BankLoanTransaction.Status.NOT_PAYED
                && s.getPaymentTime() <= BankSystem.getCurrentYaz());
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
        return (this.getLoanTotalTime()/this.getPaymentInterval())*this.getLoanInterestPerPayment();
    }

    // Return total loan amount minus existing invesments money.
    public int getAmountLeftToActivateLoan() {
        return this.getLoanAmount() - loanInvestors.values().stream().mapToInt(e -> e).sum();
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
