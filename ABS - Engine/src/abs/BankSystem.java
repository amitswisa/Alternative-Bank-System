package abs;

import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import engine.convertor.Convertor;
import generalObjects.Triple;
import javafx.util.Pair;
import xmlgenerated.AbsDescriptor;

import java.util.*;

public class BankSystem {

    private static int currentYaz;
    private BankCategories categories;
    private Map<String, BankCustomer> customers;

    // TODO - Ask Aviad.
    public BankSystem()
    {
        currentYaz = 1;
        categories =new BankCategories();
        customers = new HashMap<>();
    }

    public BankSystem(AbsDescriptor absDescriptor)
    {
        currentYaz = 1; // After file loaded successfully from xml -> start bank system from scratch.
        categories = Convertor.parseAbsCategories(absDescriptor.getAbsCategories());
        customers = Convertor.parseAbsCustomers(absDescriptor.getAbsCustomers());
        Convertor.parseAbsLoans(customers, absDescriptor.getAbsLoans());
    }

    public static int getCurrentYaz() {
        return currentYaz;
    }

    // Return list of LoanDataObject -> all customers loans data goes inside that list.
    public List<LoanDataObject> getCustomersLoansData() {

        if(customers == null || customers.size() <= 0)
            return null;

        List<LoanDataObject> newList = new ArrayList<>();
        for(Map.Entry<String,BankCustomer> singleCustomer : this.customers.entrySet()) {
            for(LoanDataObject singleLoan : singleCustomer.getValue().getListOfLoans()) {
                newList.add(singleLoan);
            }
        }
        return newList;
    }

    // return list of users names.
    public List<String> getCustomersNames() {

        if( customers == null || customers.size() <= 0)
            return null;

        return new ArrayList<String>(customers.keySet());
    }

    // Makes a deposite for given user.
    public void makeDepositeByName(String nameOfUserToDeposite, int depositeAmount) {
        customers.get(nameOfUserToDeposite).deposite(depositeAmount);
    }

    public List<CustomerDataObject> getAllCustomersLoansAndLogs() {
        if(customers == null || customers.size() <= 0)
            return null;

        List<CustomerDataObject> customerData = new ArrayList<>();
        for(Map.Entry<String, BankCustomer> customer : customers.entrySet()) {
            customerData.add(new CustomerDataObject(customer.getValue()));
        }

        return customerData;
    }

    // Makes a deposite for given user.
    public void makeWithdrawByName(String nameOfUserToDeposite, int depositeAmount) throws DataTransferObject {
        customers.get(nameOfUserToDeposite).withdraw(depositeAmount);
    }

    // Return customer if found by name.
    public BankCustomer getCustomerByName(String customerName) {
        return customers.get(customerName);
    }

    public Set<String> getBankCategories() {
        return this.categories.getBankCategories();
    }

    //Section 6 - from menu.
    public void makeInvestments(String customerName, int amountToInvest, List<Triple<String,Integer,String>> customerLoansToInvestList) {

        // Get list of Bank Loans from list of bank loans names.
        List<BankLoan> loansToInvest = this.makeLoansListFromLoansNames(customerLoansToInvestList);

        while(amountToInvest > 0 && loansToInvest.size() > 0) {

            // As long as there are loans that still didnt invest.
            int leftOver = amountToInvest % loansToInvest.size();
            int avgInvestmentAmount = amountToInvest / loansToInvest.size(); // Initial average investment in each loan.


            // go through the list and invest money as much as possible and equally berween all loans.
            for (BankLoan loan : loansToInvest) {

                int rest = loan.invest(customerName , avgInvestmentAmount);
                amountToInvest -= rest;

                if(leftOver <= loan.getAmountLeftToActivateLoan())
                    amountToInvest -= loan.invest(customerName , leftOver);

                //remove loan from list.
                if(loan.getLoanStatus() == BankLoan.Status.ACTIVE)
                    loansToInvest.remove(loan);
            }

        }
    }

    // Return list of Bank Loans from list of bank loans names.
    private List<BankLoan> makeLoansListFromLoansNames(List<Triple<String,Integer,String>> customerLoansToInvestList) {
        List<BankLoan> loansToInvest = new ArrayList<>(customerLoansToInvestList.size());
        customerLoansToInvestList.stream().forEach(loan -> {
            loansToInvest.add(customers.get(loan.getKey()).getLoanByNameAndYaz(loan.getValue(), loan.getExtraData()));
        });

        return loansToInvest;
    }
}
