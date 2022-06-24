package abs;

import dto.infodata.DataTransferObject;
import engine.EngineManager;


public class BankManager {

    private EngineManager engineManager;

    public BankManager() {
        engineManager = new EngineManager() ;
    }

    public synchronized void loadCustomerData(String filePathString, String username) {
        DataTransferObject response = engineManager.loadXML(filePathString);
    }

}
