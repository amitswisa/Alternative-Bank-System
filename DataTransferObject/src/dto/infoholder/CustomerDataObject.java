package dto.infoholder;

import abs.BankCustomer;

import java.util.List;

public class CustomerDataObject {

    private String name;
    private List<CustomerOperationData> logCustomer;
    private List<LoanDataObject> investmentList;
    private List<LoanDataObject> loanList;

    public CustomerDataObject(BankCustomer customer){
        name = customer.getName();
        this.logCustomer = customer.getCustomerLog();
        this.investmentList = customer.getLoansInvested();
        this.loanList = customer.getLoansTaken();
    }

    public String getName() {
        return this.name;
    }

    public List<CustomerOperationData> getLogCustomer() {
        return logCustomer;
    }

    public List<LoanDataObject> getInvestmentList() {
        return investmentList;
    }

    public List<LoanDataObject> getLoanList() {
        return loanList;
    }
}
