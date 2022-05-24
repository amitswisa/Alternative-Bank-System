package abs;

import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerAlertData;
import dto.objectdata.CustomerOperationData;
import dto.objectdata.LoanDataObject;
import xmlgenerated.AbsCustomer;

import java.util.*;

public class BankCustomer {

    private final String name;
    private int balance;
    private List<CustomerOperationData> customerLog;
    private Map<Integer, Set<BankLoan>> loansTaken; // List of all loans current customer took.
    private Map<Integer, Set<BankLoan>> loansInvested; // List of all loans current customer invest in.
    private List<CustomerAlertData> listOfAlerts; // List of notifications send to customer.

    public BankCustomer(AbsCustomer customer){
        this.name = customer.getName();
        this.balance = customer.getAbsBalance();
        customerLog = new ArrayList<>();
        loansTaken = new HashMap<>();
        loansInvested = new HashMap<>();
        listOfAlerts = new ArrayList<>();
        listOfAlerts.add(new CustomerAlertData("check", BankSystem.getCurrentYaz(), CustomerAlertData.Type.CONFIRMATION));
        listOfAlerts.add(new CustomerAlertData("check 2", BankSystem.getCurrentYaz(), CustomerAlertData.Type.CONFIRMATION));
        listOfAlerts.add(new CustomerAlertData("check 3", BankSystem.getCurrentYaz(), CustomerAlertData.Type.CONFIRMATION));
        listOfAlerts.add(new CustomerAlertData("check 4", BankSystem.getCurrentYaz(), CustomerAlertData.Type.CONFIRMATION));
    }

    // addLoan -> function adding loan to specific customer.
    // if key doesnt exist, create new pair and insert into map.
    public void addLoan(BankLoan loan) {
        Set<BankLoan> loans;
        if(loansTaken.containsKey(BankSystem.getCurrentYaz()))
            loans = loansTaken.get(BankSystem.getCurrentYaz());
        else
            loans = new HashSet<>();

        loans.add(loan);
        loansTaken.put(BankSystem.getCurrentYaz(), loans);
    }

    public List<LoanDataObject> getListOfLoans() {
        List<LoanDataObject> myListOfLoans = new ArrayList<>();
        for(Map.Entry<Integer, Set<BankLoan>> loan : loansTaken.entrySet())
           for(BankLoan bankLoan : loan.getValue())
                myListOfLoans.add(new LoanDataObject(bankLoan.getOwner(), bankLoan.getLoanID(), bankLoan.getLoanCategory(), bankLoan.getLoanAmount(),
                        bankLoan.getLoanOpeningTime(), bankLoan.getLoanTotalTime(), bankLoan.getLoanStartTime(), bankLoan.getLoanEndTime(),
                        bankLoan.getLoanInterestPerPayment(), bankLoan.getPaymentInterval(), bankLoan.getLoanStatus(), bankLoan.getAmountLeftToActivateLoan()));
        return myListOfLoans;
    }

    // deposit money to customer account's balace.
    public void deposite(int amountOfMoney) {
        this.addOperationToCustomerLog(
                new CustomerOperationData("Deposit"
                        , "Deposit made successfully.", this.balance, amountOfMoney));

        this.balance += amountOfMoney;
    }

    // withdraw money from customer account's balace.
    public void withdraw(int amountOfMoney) throws DataTransferObject {
        if(this.balance < amountOfMoney)
            throw new DataTransferObject(this.name + " doesnt have enough money to make this withdrawal.", BankSystem.getCurrentYaz());

        this.addOperationToCustomerLog(
                new CustomerOperationData("Withdraw", "Withdrawal made successfully."
                        , this.balance, amountOfMoney*-1));
        this.balance -= amountOfMoney;
    }

    //Add log into customer log.
    private void addOperationToCustomerLog(CustomerOperationData logData) {
        this.customerLog.add(logData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankCustomer that = (BankCustomer) o;
        return this.name.equals(that.name);
    } // Equals if addresses or names are equal.

    @Override
    public int hashCode() {
        return name.hashCode(); // Name is unique per customer, return name hashcode.
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void addInvestmentMoneyToBalance(String payerName, String loanName, int investmentMoney) {
        customerLog.add(new CustomerOperationData("Investment Payment", "You received payment from " + payerName + ", from loan: " + loanName, this.getBalance(), investmentMoney));
        this.balance += investmentMoney;
    }

    public List<CustomerOperationData> getCustomerLog() {
        return customerLog;
    }

    public List<LoanDataObject> getLoansTaken() {
        if(this.loansTaken.size() <= 0)
            return null;

        List<LoanDataObject> loansTakenList = new ArrayList<>();
        for( Map.Entry<Integer,Set<BankLoan>> loan: loansTaken.entrySet())
            for (BankLoan currentBankLoan : loan.getValue())
                loansTakenList.add(new LoanDataObject(currentBankLoan.getOwner(), currentBankLoan.getLoanID(), currentBankLoan.getLoanCategory(), currentBankLoan.getLoanAmount(),
                        currentBankLoan.getLoanOpeningTime(), currentBankLoan.getLoanTotalTime(), currentBankLoan.getLoanStartTime(), currentBankLoan.getLoanEndTime(),
                        currentBankLoan.getLoanInterestPerPayment(), currentBankLoan.getPaymentInterval(), currentBankLoan.getLoanStatus(), currentBankLoan.getAmountLeftToActivateLoan()));

        return loansTakenList;
    }

    public List<LoanDataObject> getLoansInvested() {
        if(this.loansInvested.size() <= 0)
            return null;

        List<LoanDataObject> loansInvestedList = new ArrayList<>();
        for( Map.Entry<Integer,Set<BankLoan>> loan: loansInvested.entrySet())
            for (BankLoan currentBankLoan : loan.getValue())
                loansInvestedList.add(new LoanDataObject(currentBankLoan.getOwner(), currentBankLoan.getLoanID(), currentBankLoan.getLoanCategory(), currentBankLoan.getLoanAmount(),
                        currentBankLoan.getLoanOpeningTime(), currentBankLoan.getLoanTotalTime(), currentBankLoan.getLoanStartTime(), currentBankLoan.getLoanEndTime(),
                        currentBankLoan.getLoanInterestPerPayment(), currentBankLoan.getPaymentInterval(), currentBankLoan.getLoanStatus(), currentBankLoan.getAmountLeftToActivateLoan()));

        return loansInvestedList;
    }

    // Get loan name and opening date (Yaz time) and return it.
    public BankLoan getLoanByNameAndYaz(String loanName, Integer openingTime) {
        for(BankLoan oneLoan : loansTaken.get(openingTime))
            if(oneLoan.getLoanID().equals(loanName))
                return oneLoan;

        return null;
    }

    public void addInvestment(BankLoan bankLoan, int investment) {

        //add an investment to customer.
        if(loansInvested.get(bankLoan.getLoanOpeningTime()) == null)
            loansInvested.put(bankLoan.getLoanOpeningTime(), new HashSet<BankLoan>());

        Set<BankLoan> setOfLoans = loansInvested.get(bankLoan.getLoanOpeningTime());
        if(!setOfLoans.contains(bankLoan))
            loansInvested.get(bankLoan.getLoanOpeningTime()).add(bankLoan);

        //add the investment to customer log.
        customerLog.add(new CustomerOperationData("investment","Invested " + investment + " in " + bankLoan.getLoanID(),this.balance, investment*(-1)));
        this.balance -= investment;
    }

    // Section 7 - pay loans.
    public void payCustomerTakenLoans() {
        List<BankLoan> loansToPay = new ArrayList<>();
        for(Set<BankLoan> setLoans : loansTaken.values()) {
            setLoans.forEach(loan -> {
                if((loan.getLoanStatus() == LoanDataObject.Status.ACTIVE || loan.getLoanStatus() == LoanDataObject.Status.RISK) && loan.getNextPaymentTime() == BankSystem.getCurrentYaz())
                    loansToPay.add(loan);
            });
        }

        // make each loan payment.
        loansToPay.forEach(loan ->  {
            this.balance -= loan.makePayment(this);
        });

    }

    public void addAlert(String msg, CustomerAlertData.Type status) {
        listOfAlerts.add(new CustomerAlertData(msg, BankSystem.getCurrentYaz(), status));
    }

    public List<CustomerAlertData> getListOfAlerts() {
        return this.listOfAlerts;
    }
}
