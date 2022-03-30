package engine;

import abs.BankSystem;
import dto.DataTransferObject;
import engine.convertor.Convertor;
import engine.xmlmanager.XMLManager;
import xmlgenerated.AbsDescriptor;

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

}
