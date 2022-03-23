package engine_managers;

import dto.AbsDataTransferObject;

public class EngineManager {

    private XMLManager xmlManager;

    public EngineManager() {
        xmlManager = new XMLManager(); // Init xml manager.
    }

    public AbsDataTransferObject readXmlFile(String filePath) {
        return this.xmlManager.loadXMLfile(filePath);
    }

}
