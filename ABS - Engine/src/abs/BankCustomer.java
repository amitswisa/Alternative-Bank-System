package abs;

import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerAlertData;
import dto.objectdata.CustomerOperationData;
import dto.objectdata.LoanDataObject;

import java.util.*;

public class BankCustomer {

    private final String name;
    private int balance;
    private List<CustomerOperationData> customerLog;
    private Map<Integer, Set<BankLoan>> loansTaken; // List of all loans current customer took.
    private Map<Integer, Set<BankLoan>> loansInvested; // List of all loans current customer invest in.
    private List<CustomerAlertData> listOfAlerts; // List of notifications send to customer.

    public BankCustomer(String customer){
        this.name = customer;
        this.balance = 0 ;
        this.customerLog = new ArrayList<>();
        this.loansTaken = new HashMap<>();
        this.loansInvested = new HashMap<>();
        this.listOfAlerts = new ArrayList<>();
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
                        bankLoan.getLoanInterestPerPayment(), bankLoan.getPaymentInterval(), bankLoan.getLoanStatus(),
                        bankLoan.getAmountLeftToActivateLoan(), bankLoan.getTransactionList(), bankLoan.getLoanInvestorsToView(), bankLoan.getInvestorsSellStatusMap()));
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
        customerLog.add(new CustomerOperationData("Investment payment", "You received payment from " + payerName + ", from loan: " + loanName, this.getBalance(), investmentMoney));
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
                        currentBankLoan.getLoanInterestPerPayment(), currentBankLoan.getPaymentInterval(), currentBankLoan.getLoanStatus(),
                        currentBankLoan.getAmountLeftToActivateLoan(),currentBankLoan.getTransactionList(), currentBankLoan.getLoanInvestorsToView(), currentBankLoan.getInvestorsSellStatusMap()));

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
                        currentBankLoan.getLoanInterestPerPayment(), currentBankLoan.getPaymentInterval(), currentBankLoan.getLoanStatus(),
                        currentBankLoan.getAmountLeftToActivateLoan(), currentBankLoan.getTransactionList(), currentBankLoan.getLoanInvestorsToView(), currentBankLoan.getInvestorsSellStatusMap()));

        return loansInvestedList;
    }

    // Get loan name and opening date (Yaz time) and return it.
    public BankLoan getLoanByNameAndYaz(String loanName, Integer openingTime) {
        System.out.println(openingTime);
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

    // Loan's payments.

    // pay for specific loan.
    public void payCustomerTakenLoan(LoanDataObject loan, int amountToPay) {
        BankLoan currentLoanToPay = this.getLoanByNameAndYaz(loan.getLoanID(), loan.getLoanOpeningTime());
        int res = currentLoanToPay.makePayment(this, amountToPay);
        this.pay(res);
    }

    public void updateCustomerLoansStatus() {
        List<BankLoan> loansToCheck = this.getNeedToPayLoans();
        loansToCheck.forEach(e -> e.updateLoanStatus(this));
    }

    public List<BankLoan> getNeedToPayLoans() {
        List<BankLoan> loansToPay = new ArrayList<>();
        for(Set<BankLoan> setLoans : loansTaken.values()) {
            setLoans.forEach(loan -> {
                if((loan.getLoanStatus() == LoanDataObject.Status.ACTIVE
                        || loan.getLoanStatus() == LoanDataObject.Status.RISK)
                            && loan.getNextPaymentTime() < BankSystem.getCurrentYaz()+1)
                    loansToPay.add(loan);
            });
        }
        return loansToPay;
    }

    // Notifications
    public void addAlert(String headline, String msg) {
        listOfAlerts.add(new CustomerAlertData(headline, msg, BankSystem.getCurrentYaz()));
    }

    // Mark all user messages as read.
    public void markCustomerMessagesAsRead() {
        this.listOfAlerts.forEach(CustomerAlertData::markAsRead);
    }

    public List<CustomerAlertData> getListOfAlerts() {
        return this.listOfAlerts;
    }

    public void payLoanAllDebt(LoanDataObject e) {
        BankLoan loan = this.getLoanByNameAndYaz(e.getLoanID(), e.getLoanOpeningTime());
        loan.payLoanAllDebt(this, e);
        this.addAlert(loan.getLoanID(), "Finished pay the loan!");
    }

    public void pay(int amount) {
        this.addOperationToCustomerLog(
                new CustomerOperationData("Loan payment"
                        , "", this.balance, -1*amount));
        this.balance -= amount;
    }

    // Change loan investor selling status.
    public String changeLoanSellStatus(String sellerName, String loanName) {

        BankLoan loan = getLoan(loanName); // Get loan from loan's list.

        if(loan == null)
            return "Loan doesnt exist.";

        return loan.getLoanInvestors().get(sellerName).setIsSell();
    }

    // Get loan from customer loan's list by name.
    private BankLoan getLoan(String name) {
        BankLoan temp = null;

        for(Set<BankLoan> loans : loansTaken.values())
        {
            for(BankLoan loan : loans) {
                if(loan.getLoanID().equals(name)) {
                    temp = loan;
                    break;
                }
            }
        }

        return temp;
    }

    private BankLoan getLoanByName(String loanName) throws DataTransferObject {

        for(Map.Entry<Integer, Set<BankLoan>> loan : loansTaken.entrySet())
            for(BankLoan bankLoan : loan.getValue())
                if(bankLoan.getLoanID().equals(loanName))
                    return bankLoan;

        throw new DataTransferObject("Error: loan doesnt exist.", BankSystem.getCurrentYaz());
    }

    public void changeShareOwner(BankCustomer seller, BankCustomer buyer, String loanName) throws DataTransferObject {

        BankLoan loan = this.getLoanByName(loanName);

        if(buyer.getBalance() < loan.getInvestorLeftShare(seller.getName()))
            throw new DataTransferObject("Error: you dont have enough money to buy this loan.", BankSystem.getCurrentYaz());

        loan.changeShareOwner(seller, buyer);
    }

    public void addLoanAfterBuy(BankLoan loan) {
        if(this.loansInvested.get(loan.getLoanOpeningTime()) == null)
            this.loansInvested.put(loan.getLoanOpeningTime(), new HashSet<BankLoan>());

        this.loansInvested.get(loan.getLoanOpeningTime()).add(loan);
    }

    public void removeLoanAfterSell(BankLoan loan) {
        this.loansInvested.get(loan.getLoanOpeningTime()).remove(loan);
    }

    public void getMoneyOfShareSell(int amount, String loanID) {
        this.addOperationToCustomerLog(
                new CustomerOperationData("Loan sale"
                        , "The loan " + loanID + " sold!", this.balance, amount));
        this.addAlert("Loan " + loanID + " sold!", "You received " + amount + " for selling this loan.");
        this.balance += amount;
    }

    public void payMoneyOfShareBuying(int amount, String loanID) {
        this.addOperationToCustomerLog(
                new CustomerOperationData("Loan buy"
                        , "You bought '" + loanID + "' loan!", this.balance, (-1)*amount));
        this.balance -= amount;
    }
}
