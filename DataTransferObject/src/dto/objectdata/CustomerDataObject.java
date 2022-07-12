package dto.objectdata;

import javafx.beans.property.SimpleIntegerProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerDataObject {

    private final String name;
    private final SimpleIntegerProperty balance;
    private final List<CustomerOperationData> logCustomer;
    private final List<LoanDataObject> investmentList; // My investments
    private final List<LoanDataObject> loanList; // my Loans
    private final List<CustomerAlertData> listOfAlerts;

    public CustomerDataObject(String name, List<CustomerOperationData> logCustomer, List<LoanDataObject> investmentList, List<LoanDataObject> loanList, int balance, List<CustomerAlertData> listOfAlerts){

        if(loanList == null)
            this.loanList = new ArrayList<>();
        else
            this.loanList = loanList;

        if(investmentList == null)
            this.investmentList = new ArrayList<>();
        else
            this.investmentList = investmentList;

        this.name = name;
        this.logCustomer = logCustomer;
        this.balance = new SimpleIntegerProperty(balance);
        this.listOfAlerts = listOfAlerts;
    }

    public String getName() {
        return this.name;
    }

    public int getBalance() {
        return this.balance.get();
    }

    public List<CustomerOperationData> getLogCustomer() {
        return this.logCustomer;
    }

    public List<LoanDataObject> getInvestmentList() {
        return this.investmentList;
    }

    public List<LoanDataObject> getLoanList() {
        return this.loanList;
    }

    public List<CustomerAlertData> getListOfAlerts() {
        return this.listOfAlerts;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
