package engine;

import abs.BankSystem;
import dto.DataTransferObject;
import dto.infoholder.CustomerDataObject;
import dto.infoholder.LoanDataObject;
import engine.convertor.Convertor;
import engine.xmlmanager.XMLManager;
import xmlgenerated.AbsDescriptor;

import javax.xml.crypto.Data;
import java.util.List;

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
}
