package components.Customer;


import dto.objectdata.CustomerAlertData;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.CustomerOperationData;
import dto.objectdata.LoanDataObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class AppCustomer {

    private final String name;
    private final SimpleIntegerProperty balance;
    private final SimpleIntegerProperty timeInYaz;
    private ObservableList<CustomerOperationData> logCustomer;
    private ObservableList<LoanDataObject> investmentList; // My investments
    private ObservableList<LoanDataObject> loanList; // my Loans
    private ObservableList<CustomerAlertData> listOfAlerts;

    public AppCustomer(String name){
        this.name = name;
        this.logCustomer = FXCollections.observableArrayList();
        this.investmentList = FXCollections.observableArrayList();
        this.loanList = FXCollections.observableArrayList();
        this.balance = new SimpleIntegerProperty(0);
        this.timeInYaz = new SimpleIntegerProperty(0);
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
        this.setInvestmentList(data.getInvestmentList());
        this.setListOfAlerts(data.getListOfAlerts());
        this.setLogCustomerList(data.getLogCustomer());
        this.setLoanList(data.getLoanList());
    }

    public IntegerProperty getTimeInYaz() {
        return timeInYaz;
    }

    public void setTimeInYaz(Integer timeInYaz) {
        this.timeInYaz.set(timeInYaz);
    }
}

