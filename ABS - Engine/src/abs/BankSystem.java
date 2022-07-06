package abs;

import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import engine.convertor.Convertor;
import generalObjects.LoanTask;
import generalObjects.Triple;
import xmlgenerated.AbsDescriptor;

import java.util.*;

public class BankSystem {

    private static int currentYaz = 1;
    private BankCategories categories;
    private Map<String, BankCustomer> customers;

    public void LoadCustomerXML(AbsDescriptor absDescriptor, String username) // TODO - Add username as parameter.
    {
        BankCustomer currentUser = getCustomerByName(username);

        categories.addAnotherCategoriesSet(Convertor.parseAbsCategories(absDescriptor.getAbsCategories()));
        Convertor.parseAbsLoans(currentUser, absDescriptor.getAbsLoans());
    }

    public BankSystem()
    {
        // Create empty bank when the server is running.
        categories = new BankCategories();
        customers = new HashMap<>();
    }

    public static int getCurrentYaz() {
        return currentYaz;
    }

    public void increaseYazDate() {
        // make relevant investments payment.
        customers.values().forEach(BankCustomer::updateCustomerLoansStatus);

        currentYaz++; // increase YAZ date by 1.
    }

    // Close all loans.
    public void handleCustomerPayAllDebt(List<LoanDataObject> loans) throws DataTransferObject {

        if(loans == null || loans.size() <= 0)
            throw new DataTransferObject("There are no loans to pay debt for.", BankSystem.getCurrentYaz());

        // Calculate amount to pay to cover all loans.
        int totalToPay = 0;
        for(LoanDataObject loan : loans)
            totalToPay += loan.getInterestAmount() + loan.getLoanAmount();

        // Check if customer has enough balance.
        BankCustomer customer = this.getCustomerByName(loans.get(0).getOwner());
        if(totalToPay > customer.getBalance())
            throw new DataTransferObject("You dont have enough balance to cover all your loans.", BankSystem.getCurrentYaz());

        loans.forEach(customer::payLoanAllDebt); // pay all loan.
    }

    // Pay for specific loan.
    public void handleCustomerLoanPayment(LoanDataObject loan, int amountToPay) {

        if(loan == null)
            return;

        this.getCustomerByName(loan.getOwner()).payCustomerTakenLoan(loan, amountToPay);
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
            customerData.add(new CustomerDataObject(customer.getValue().getName(), customer.getValue().getCustomerLog(), customer.getValue().getLoansInvested(), customer.getValue().getLoansTaken(), customer.getValue().getBalance(), customer.getValue().getListOfAlerts()));
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
    public String makeInvestments(String customerName, int amountToInvest, List<Triple<String,Integer,String>> customerLoansToInvestList, LoanTask loanTask) {

        // Get list of Bank Loans from list of bank loans names.
        List<BankLoan> loansToInvest = this.makeLoansListFromLoansNames(customerLoansToInvestList);

        //Sort the list by  the amount left to activate the loan.
        this.sortLoanslist(loansToInvest);

        // go through the list and invest money as much as possible and equally between all loans.
        return this.investEqually(getCustomerByName(customerName) , amountToInvest , loansToInvest, loanTask);

    }

    // go through the list and invest money as much as possible and equally berween all loans.
    private String investEqually(BankCustomer customerName, int amountToInvest, List<BankLoan> loansToInvest, LoanTask loanTask) {
        String res = "New investments: \n";
        for (int i = 0 ; i < loansToInvest.size() ; i++) {

            int avgInvestmentAmount = amountToInvest / (loansToInvest.size() - i ); // Initial average investment in each loan.

            //for the last investment we try to invest the max amount letf
            int realTimeInvestedAmount = 0;
            if(i == (loansToInvest.size() -1)) {
                if (amountToInvest > 0)
                    realTimeInvestedAmount = loansToInvest.get(i).invest(this.getCustomerByName(loansToInvest.get(i).getOwner()), customerName, amountToInvest);
            } else {
                if (avgInvestmentAmount > 0)
                    realTimeInvestedAmount = loansToInvest.get(i).invest(this.getCustomerByName(loansToInvest.get(i).getOwner()), customerName, avgInvestmentAmount);
            }

            amountToInvest -= realTimeInvestedAmount;
            res += "Invested " + realTimeInvestedAmount + " in " + loansToInvest.get(i).getLoanID() + ".\n";
            loanTask.setMessage(res);
            loanTask.progressUpdate();
            loanTask.sleepForAWhile();
        }
        return res;
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

    // Add new customer to customers' list when first logged in.
    public void addNewCustomer(String customerName) {
        this.customers.put(customerName, new BankCustomer(customerName));
    }
}
