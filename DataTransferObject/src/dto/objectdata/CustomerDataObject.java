package dto.objectdata;

import java.util.List;

public class CustomerDataObject {

    private String name;
    private List<CustomerOperationData> logCustomer;
    private List<LoanDataObject> investmentList;
    private List<LoanDataObject> loanList;

    public CustomerDataObject(String name, List<CustomerOperationData> logCustomer, List<LoanDataObject> investmentList,  List<LoanDataObject> loanList){
        this.name = name;
        this.logCustomer = logCustomer;
        this.investmentList = investmentList;
        this.loanList = loanList;
    }

    public String getName() {
        return this.name;
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
