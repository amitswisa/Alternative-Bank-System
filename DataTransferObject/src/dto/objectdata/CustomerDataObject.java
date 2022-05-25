package dto.objectdata;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerDataObject {

    private final String name;
    private int balance;
    private List<CustomerOperationData> logCustomer;
    private List<LoanDataObject> investmentList;
    private List<LoanDataObject> loanList;
    private List<CustomerAlertData> listOfAlerts;

    public CustomerDataObject(String name, List<CustomerOperationData> logCustomer, List<LoanDataObject> investmentList, List<LoanDataObject> loanList, int balance, List<CustomerAlertData> listOfAlerts){
        this.name = name;
        this.logCustomer = logCustomer;
        this.investmentList = investmentList;
        this.loanList = loanList;
        this.balance = balance;
        this.listOfAlerts = listOfAlerts;
    }

    public String getName() {
        return this.name;
    }

    public int getBalance() {
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

    public List<CustomerOperationData> getLogCustomer() {
        return this.logCustomer;
    }

    public List<LoanDataObject> getInvestmentList() {
        return investmentList;
    }

    public List<LoanDataObject> getLoanList() {
        return this.loanList;
    }

    public int countUnfinishedLoans() {
        return (int) this.getLoanList().stream()
                .filter(i -> i.getLoanStatus() != LoanDataObject.Status.FINISHED).count();
    }

    public List<CustomerAlertData> getListOfAlerts() {
        return this.listOfAlerts;
    }

}
