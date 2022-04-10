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

        //Sort the list by  the amount left to activate the loan.
        this.sortLoanslist(loansToInvest);

        // go through the list and invest money as much as possible and equally berween all loans.
        this.investEqually(customerName , amountToInvest , loansToInvest);

    }

    // go through the list and invest money as much as possible and equally berween all loans.
    private void investEqually(String customerName, int amountToInvest, List<BankLoan> loansToInvest) {

        for (int i = 0 ; i < loansToInvest.size() ; i++) {
            int avgInvestmentAmount = amountToInvest / (loansToInvest.size() - i ); // Initial average investment in each loan.
            //for the last investment we try to invest the max amount letf
            if(i == (loansToInvest.size() -1)) {
                amountToInvest -= loansToInvest.get(i).invest(customerName , amountToInvest);
            }
            else {
                amountToInvest -= loansToInvest.get(i).invest(customerName , avgInvestmentAmount);
            }
        }

        //TODO - think how we handle if not all the money invested, send messege how much money invest
    }

    //Sort the list by  the amount left to activate the loan.
    private void sortLoanslist(List<BankLoan> loansToInvest) {

        Collections.sort(loansToInvest, new Comparator<BankLoan>() {
            @Override
            public int compare(BankLoan o1, BankLoan o2) {
                return o1.getAmountLeftToActivateLoan() < o2.getAmountLeftToActivateLoan() ? -1
                        : o1.getAmountLeftToActivateLoan() == o2.getAmountLeftToActivateLoan() ? 0 : 1;
            }
        });

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
