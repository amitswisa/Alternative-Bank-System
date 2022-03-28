package engine_managers;

import dto.AbsDataTransferObject;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class EngineManager {

    private XMLManager xmlManager;

    public EngineManager() {
        xmlManager = new XMLManager(); // Init xml manager.
    }

    /*# readXmlFile - Load relevant xml file to XMLManager.
    # arg::String filePath - path of xml file.
    # return value - AbsDataTransferObject Object.*/
    public AbsDataTransferObject loadXML(String filePath) throws JAXBException, FileNotFoundException {
        return this.xmlManager.loadXMLfile(filePath);
    }

}
