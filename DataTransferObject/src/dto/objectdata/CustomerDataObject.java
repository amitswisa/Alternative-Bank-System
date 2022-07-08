package dto.objectdata;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDataObject {

    private final String name;
    private SimpleIntegerProperty balance;
    private ObservableList<CustomerOperationData> logCustomer;
    private ObservableList<LoanDataObject> investmentList; // My investments
    private ObservableList<LoanDataObject> loanList; // my Loans
    private ObservableList<CustomerAlertData> listOfAlerts;

    public CustomerDataObject(String name, List<CustomerOperationData> logCustomer, List<LoanDataObject> investmentList, List<LoanDataObject> loanList, int balance, List<CustomerAlertData> listOfAlerts){
        this.name = name;
        this.logCustomer = FXCollections.observableArrayList(logCustomer);
        this.investmentList = FXCollections.observableArrayList(investmentList);
        this.loanList = FXCollections.observableArrayList(loanList);
        this.balance = new SimpleIntegerProperty(balance);
        this.listOfAlerts = FXCollections.observableArrayList(listOfAlerts);
    }

    public CustomerDataObject(String name) {
        this.name = name;
        this.logCustomer = FXCollections.observableArrayList();
        this.investmentList = FXCollections.observableArrayList();
        this.loanList = FXCollections.observableArrayList();
        this.balance = new SimpleIntegerProperty(0);
        this.listOfAlerts = FXCollections.observableArrayList();
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
            int temp = this.loanList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.NEW)).collect(Collectors.toList()).size();
           res += temp;
           res += " / ";

            temp = this.loanList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.PENDING)).collect(Collectors.toList()).size();
            res += temp;
            res += " / ";

            temp = this.loanList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.ACTIVE)).collect(Collectors.toList()).size();
            res += temp;
            res += " / ";

            temp = this.loanList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.RISK)).collect(Collectors.toList()).size();
            res += temp;
        }
        else
            res += "0";

        return res;
    }

    public String getInvestedLoansNumber() {
        String res = "";
        if(this.investmentList != null && !this.investmentList.isEmpty()){
            int temp = this.investmentList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.NEW)).collect(Collectors.toList()).size();
        res += temp;
        res += " / ";

        temp = this.investmentList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.PENDING)).collect(Collectors.toList()).size();
        res += temp;
        res += " / ";

        temp = this.investmentList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.ACTIVE)).collect(Collectors.toList()).size();
        res += temp;
        res += " / ";

        temp = this.investmentList.stream().filter(e -> e.getLoanStatus().equals(LoanDataObject.Status.RISK)).collect(Collectors.toList()).size();
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

    public int countUnfinishedLoans() {
        return (int) this.getLoanList().stream()
                .filter(i -> i.getLoanStatus() != LoanDataObject.Status.FINISHED).count();
    }

    public ObservableList<CustomerAlertData> getListOfAlerts() {
        return this.listOfAlerts;
    }

    // Adding customer loans to his own list of loans.
    public void addLoans(List<LoanDataObject> newLoansUploadedList) {
        this.loanList.addAll(newLoansUploadedList);
    }

    public void depositMoney(int depositAmount) {
        this.balance.set(this.balance.get() + depositAmount);
    }

    public void withdrawMoney(int withdrawAmount) {
        this.balance.set(this.balance.get() - withdrawAmount);
    }
}
