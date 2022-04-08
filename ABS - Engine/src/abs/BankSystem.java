package abs;

import dto.DataTransferObject;
import dto.infoholder.CustomerDataObject;
import dto.infoholder.LoanDataObject;
import engine.convertor.Convertor;
import xmlgenerated.AbsDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
