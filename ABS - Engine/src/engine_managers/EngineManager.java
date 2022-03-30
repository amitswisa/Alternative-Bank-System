package engine_managers;

import dto.DataTransferObject;
import xmlgenerated.AbsDescriptor;

public class EngineManager {

    private final XMLManager xmlManager;

    public EngineManager() {
        xmlManager = new XMLManager(); // Init xml manager.
    }

    /*# readXmlFile - Load relevant xml file to XMLManager.
    # arg::String filePath - path of xml file.
    # return value - DataTransferObject Object.*/
    public DataTransferObject loadXML(String filePath) {
        AbsDescriptor xmlObject;
        try {
            xmlObject = this.xmlManager.loadXMLfile(filePath);
        } catch(DataTransferObject e) {
            return e;
        }

        //TODO - Converting AbsDescriptor to our kind of object.

        return new DataTransferObject("File loaded successfully!");
    }

}
