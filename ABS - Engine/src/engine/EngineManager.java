package engine;

import abs.BankCustomer;
import abs.BankSystem;
import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import engine.convertor.Convertor;
import engine.xmlmanager.XMLManager;
import generalObjects.LoanTask;
import generalObjects.Triple;
import xmlgenerated.AbsDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EngineManager {

    private final XMLManager xmlManager;
    private BankSystem bankSystem;

    public EngineManager() {
        bankSystem = new BankSystem(); // Creating bank systenm for the first time.
        xmlManager = new XMLManager(); // Init xml manager.
    }

    /*# readXmlFile - Load relevant xml file to XMLManager.
    # arg::String filePath - path of xml file.
    # return value - DataTransferObject Object.*/
    // TODO - add information to specific customer.
    public synchronized DataTransferObject loadXML(String filePath, String username) {

        if(filePath.isEmpty())
            return new DataTransferObject("Please choose a file!", BankSystem.getCurrentYaz());

        try {
           AbsDescriptor res = this.xmlManager.loadXMLfile(filePath); // Try loading xml file and return AbdDescriptor.
        } catch(DataTransferObject e) {
            return e;
        }

        return new DataTransferObject("File loaded successfully!", true, BankSystem.getCurrentYaz());
    }

    public synchronized void loadCustomerData(String filePathString, String username) {
        DataTransferObject response = this.loadXML(filePathString, username);
    }

    public boolean isFileLoaded() {
        return this.xmlManager.isFileLoaded();
    }

    // Section 2 from menu.
    // Returns list of all loan's DTO.
    public List<LoanDataObject> getAllLoansData() {
        return this.bankSystem.getCustomersLoansData();
    }

    public List<String> getAllCustomersNames() {
        if(bankSystem != null)
            return this.bankSystem.getCustomersNames();

        return null;
    }

    public CustomerDataObject getCustomerByName(String customerName) {
        if(bankSystem != null) {
            BankCustomer temp = this.bankSystem.getCustomerByName(customerName);
            return new CustomerDataObject(temp.getName(), temp.getCustomerLog(), temp.getLoansInvested(), temp.getLoansTaken(), temp.getBalance(), temp.getListOfAlerts());
        }

        return null;
    }

    // Section 3
    public List<CustomerDataObject> getAllCustomersLoansAndLogs() {
        return this.bankSystem.getAllCustomersLoansAndLogs();
    }

    // Section 4
    public void depositeMoney(String userName, int depositeAmount) {
        this.bankSystem.makeDepositeByName(userName, depositeAmount);
    }

    // Section 5
    public void withdrawMoney(String userName, int widthrawAmount) throws DataTransferObject {
        this.bankSystem.makeWithdrawByName(userName, widthrawAmount);
    }

    //Section 6
    public int getBalanceOfCustomerByName(String customerName) {
        return this.bankSystem.getCustomerByName(customerName).getBalance();
    }

    public Set<String> getBankCategories() {
        return this.bankSystem.getBankCategories();
    }

    public List<LoanDataObject> getRelevantPendingLoansList(String chosenCustomer, List<String> catChoice, int interest, int totalTime) {
        List<LoanDataObject> pendingLoans = this.getAllLoansData();

        if(pendingLoans == null)
            return null;

        List<LoanDataObject> resData = new ArrayList<>(); // result list.

        // filter loans list -> only loans with status PENDING or NEW & loan's owner is other then current customer.
        pendingLoans = pendingLoans.stream().filter(loan -> !loan.getOwner().equals(chosenCustomer)
                && (loan.getLoanStatus() == LoanDataObject.Status.PENDING || loan.getLoanStatus() == LoanDataObject.Status.NEW)).collect(Collectors.toList());

        // filter loans list -> if customer chose specific category then the list will hold loans from that category.
        if(!catChoice.get(0).equals("All"))
            for(String ch : catChoice)
                resData.addAll(pendingLoans.stream().filter(loan -> loan.getLoanCategory().equals(ch)).collect(Collectors.toList()));

        // filter loans lost -> if interent isnt equal to 0 -> hold loans with interest greater or equal to given interest.
        if(interest > 0)
            resData = resData.stream().filter(loan -> loan.getLoanInterestPerPayment() >= interest).collect(Collectors.toList());

        // filter loans list -> filter list by total time left to loans.
        if(totalTime > 0)
            resData = resData.stream().filter(loan -> loan.getLoanTotalTime() <= totalTime).collect(Collectors.toList());

        return resData;
    }

    // Get list of loans to invest.
    public String makeInvestments(String customerName, int amountToInvest, List<Triple<String, Integer, String>> customerLoansToInvestList, LoanTask loanTask) throws DataTransferObject {
        return this.bankSystem.makeInvestments(customerName, amountToInvest, customerLoansToInvestList, loanTask);
    }

    // Increase YAZ date by 1.
    public void increaseYazDate() {
        this.bankSystem.increaseYazDate();
    }

    public List<CustomerDataObject> getAllCustomerData() {
        return this.bankSystem.getAllCustomersLoansAndLogs();
    }

    // Customer Payments.

    // Pay loan current payment.
    public void handleCustomerLoansPayments(LoanDataObject loan, int amountToPay) {
        this.bankSystem.handleCustomerLoanPayment(loan, amountToPay);
    }

    // Close all loans debt or specific loan.
    public void handleCustomerPayAllDebt(List<LoanDataObject> loans) throws DataTransferObject {
        this.bankSystem.handleCustomerPayAllDebt(loans);
    }

    // Add new customer to customer's list when first logged in.
    public void addNewCustomer(String customerName) {
        this.bankSystem.addNewCustomer(customerName);
    }
}
