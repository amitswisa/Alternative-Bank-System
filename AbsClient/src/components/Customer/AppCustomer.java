package components.Customer;


import dto.objectdata.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class AppCustomer {

    private final String name;
    private final SimpleIntegerProperty balance;
    private static SimpleIntegerProperty timeInYaz;
    private ObservableList<CustomerOperationData> logCustomer;
    private ObservableList<LoanDataObject> investmentList;//  My investments
    private ObservableList<LoanDataObject> loanList; // my Loans
    private ObservableList<CustomerAlertData> listOfAlerts;
    private ObservableList<LoanSellerObject> loanSellerList;

    public AppCustomer(String name, String initYaz){
        this.name = name;
        this.logCustomer = FXCollections.observableArrayList();
        this.investmentList = FXCollections.observableArrayList();
        this.loanList = FXCollections.observableArrayList();
        this.balance = new SimpleIntegerProperty(0);
        timeInYaz = new SimpleIntegerProperty(Integer.parseInt(initYaz));
        this.listOfAlerts = FXCollections.observableArrayList();
        this.loanSellerList = FXCollections.observableArrayList();
    }

    public String getName() {
        return this.name;
    }

    public int getBalance() {
        return this.balance.get();
    }

    public SimpleIntegerProperty getBalanceAsIntegerProperty() {
        return this.balance;
    }

    public String getTakenLoansNumber() {
        String res = "";
        if(this.loanList != null && !this.loanList.isEmpty()) {
            int temp = (int) this.loanList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.NEW)).count();
            res += temp;
            res += " / ";

            temp = (int) this.loanList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.PENDING)).count();
            res += temp;
            res += " / ";

            temp = (int) this.loanList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.ACTIVE)).count();
            res += temp;
            res += " / ";

            temp = (int) this.loanList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.RISK)).count();
            res += temp;
        }
        else
            res += "0";

        return res;
    }

    public String getInvestedLoansNumber() {
        String res = "";
        if(this.investmentList != null && !this.investmentList.isEmpty()){
            int temp = (int) this.investmentList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.NEW)).count();
            res += temp;
            res += " / ";

            temp = (int) this.investmentList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.PENDING)).count();
            res += temp;
            res += " / ";

            temp = (int) this.investmentList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.ACTIVE)).count();
            res += temp;
            res += " / ";

            temp = (int) this.investmentList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.RISK)).count();
            res += temp;
        }
        else
            res += "0";

        return res;
    }

    public ObservableList<CustomerOperationData> getLogCustomer() {
        return this.logCustomer;
    }

    public ObservableList<LoanDataObject> getInvestmentList() {
        return investmentList;
    }

    public ObservableList<LoanDataObject> getLoanList() {
        return this.loanList;
    }

    public ObservableList<LoanSellerObject> getLoanSellerList() {
        return this.loanSellerList;
    }

    public int countUnfinishedLoans() {
        return (int) this.getLoanList().stream()
                .filter(i -> i.getLoanStatus() != LoanDataObject.Status.FINISHED).count();
    }

    public ObservableList<CustomerAlertData> getListOfAlerts() {
        return this.listOfAlerts;
    }

    public void depositMoney(int valueOfInput) {
        this.balance.set(this.balance.get() + valueOfInput);
    }

    public void withdrawMoney(int valueOfInput) {
        this.balance.set(this.balance.get() - valueOfInput);
    }

    // Add list with new loans to my loan's list.
    public void addToMyLoansList(List<LoanDataObject> newLoansUploadedList) {
        loanList.addAll(newLoansUploadedList);
    }

    public void setLogCustomerList(List<CustomerOperationData> operationData) {
        this.logCustomer.setAll(operationData);
    }

    public void setInvestmentList(List<LoanDataObject> investmentList) {
        this.investmentList.setAll(investmentList);
    }

    public void setLoanList(List<LoanDataObject> loanList) {
        this.loanList.setAll(loanList);
    }

    public void setLoanSellerList(List<LoanDataObject> allOtherLoans) {

        // Clear list.
        this.loanSellerList.clear();

        // Go through all other loans and insert to list relevant ones.
        allOtherLoans.forEach(loan -> {
            loan.getShareSellList().forEach((sellerName, isSelling) -> {
                if(isSelling && !sellerName.equals(this.name)) { // Means that other investor sell his share.
                    this.loanSellerList.add(new LoanSellerObject(loan, sellerName));
                }
            });
        });

        // Go through all customer investments and insert to list relevant ones.
        this.getInvestmentList().forEach(loan -> {
            loan.getShareSellList().forEach((sellerName, isSelling) -> {
                if(isSelling && !sellerName.equals(this.name)) { // Means that other investor sell his share.
                    this.loanSellerList.add(new LoanSellerObject(loan, sellerName));
                }
            });
        });
    }

    public void setListOfAlerts(List<CustomerAlertData> listOfAlerts) {
        if(listOfAlerts != null && !listOfAlerts.isEmpty()) {
            listOfAlerts.forEach(e -> {
                if(e != null)
                    this.listOfAlerts.add(e);
            });
        }
    }

    // Updates customer from data returns from server.
    public void updateUser(CustomerDataObject data) {
        this.balance.set(data.getBalance());
        this.setInvestmentList(data.getInvestmentList());
        this.setListOfAlerts(data.getListOfAlerts());
        this.setLogCustomerList(data.getLogCustomer());
        this.setLoanList(data.getLoanList());
    }

    public SimpleIntegerProperty getTimeInYaz() {
        return timeInYaz;
    }

    public static int getTimeInYazAsInteger() {
        return timeInYaz.get();
    }

    public void setTimeInYaz(Integer yaz) {
        timeInYaz.set(yaz);
    }
}

