package dto.objectdata;

import java.util.List;

public class CustomerDataObject {

    private final String name;
    private int balance;
    private List<CustomerOperationData> logCustomer;
    private List<LoanDataObject> investmentList;
    private List<LoanDataObject> loanList;

    public CustomerDataObject(String name, List<CustomerOperationData> logCustomer, List<LoanDataObject> investmentList,  List<LoanDataObject> loanList, int balance){
        this.name = name;
        this.logCustomer = logCustomer;
        this.investmentList = investmentList;
        this.loanList = loanList;
        this.balance = balance;
    }

    public String getName() {
        return this.name;
    }

    public int getBalance() {
        return this.balance;
    }

    public int getTakenLoansNumber() {
        if(this.loanList != null && !this.loanList.isEmpty())
            return this.loanList.size();

        return 0;
    }

    public int getInvestedLoansNumber() {
        if(this.investmentList != null && !this.investmentList.isEmpty())
            return this.loanList.size();

        return 0;
    }

    public List<CustomerOperationData> getLogCustomer() {
        return this.logCustomer;
    }

    public List<LoanDataObject> getInvestmentList() {
        return investmentList;
    }

    public List<LoanDataObject> getLoanList() {
        return this.loanList;
    }

}
