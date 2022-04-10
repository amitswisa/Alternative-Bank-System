package engine;

import abs.BankCustomer;
import abs.BankLoan;
import abs.BankSystem;
import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import engine.xmlmanager.XMLManager;
import generalObjects.Triple;
import javafx.util.Pair;
import xmlgenerated.AbsDescriptor;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EngineManager {

    private final XMLManager xmlManager;
    private BankSystem bankSystem;

    public EngineManager() {
        xmlManager = new XMLManager(); // Init xml manager.
    }

    /*# readXmlFile - Load relevant xml file to XMLManager.
    # arg::String filePath - path of xml file.
    # return value - DataTransferObject Object.*/
    public DataTransferObject loadXML(String filePath) {
        AbsDescriptor xmlObject;
        try {
            xmlObject = this.xmlManager.loadXMLfile(filePath); // Try loading xml file and return AbdDescriptor.
        } catch(DataTransferObject e) {
            return e;
        }

        bankSystem = new BankSystem(xmlObject); // Creating bank system from AbsDescriptor.
        return new DataTransferObject("File loaded successfully!");
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

    public List<LoanDataObject> getRelevantPendingLoansList(String chosenCustomer, String catChoice, int interest, int totalTime) {
        List<LoanDataObject> pendingLoans = this.getAllLoansData();

        if(pendingLoans == null)
            return null;

        // filter loans list -> only loans with status PENDING & loan's owner is other then current customer.
        pendingLoans = pendingLoans.stream().filter(loan -> !loan.getLoanOwnerName().equals(chosenCustomer)
                && loan.getLoanStatus() == BankLoan.Status.PENDING ).collect(Collectors.toList());

        // filter loans list -> if customer chose specific category then the list will hold loans from that category.
        if(!catChoice.equals(""))
            pendingLoans = pendingLoans.stream().filter(loan -> loan.getLoanCatgory().equals(catChoice)).collect(Collectors.toList());

        // filter loans lost -> if interent isnt equal to 0 -> hold loans with interest greater or equal to given interest.
        if(interest > 0)
            pendingLoans = pendingLoans.stream().filter(loan -> loan.getLoanInterest() >= interest).collect(Collectors.toList());

        // filter loans list -> filter list by total time left to loans.
        if(totalTime > 0)
            pendingLoans = pendingLoans.stream().filter(loan -> loan.getLoanTotalTime() <= totalTime).collect(Collectors.toList());

        return pendingLoans;
    }

    // Get list of loans to invest.
    public void makeInvestments(String customerName, int amountToInvest, List<Triple<String,Integer,String>> customerLoansToInvestList) throws DataTransferObject {
        this.bankSystem.makeInvestments(customerName, amountToInvest, customerLoansToInvestList);
    }
}
