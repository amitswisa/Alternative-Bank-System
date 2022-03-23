package engine_managers;

import dto.AbsDataTransferObject;

public class EngineManager {

    private XMLManager xmlManager;

    public EngineManager() {
        xmlManager = new XMLManager(); // Init xml manager.
    }

    // Handle user choice.
    public AbsDataTransferObject handleUserChoice(int userChoice) {

        switch (userChoice)
        {
            case 1: {
                return xmlManager.loadXMLfile(); // Load xml file and return DTO response message.
            }
            case 8: {
                System.exit(0);
                break;
            }
        }

        return null; // No case was chosen.
    }
}
