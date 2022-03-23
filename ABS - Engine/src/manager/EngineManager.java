package manager;

public class EngineManager {

    private XMLManager xmlManager;

    public EngineManager() {
        xmlManager = new XMLManager(); // Init xml manager.
    }

    // Handle user choice.
    public String handleUserChoice(int userChoice) {

        String resMsg = "Choice Error: please choose existing option."; // Default return message.
        switch (userChoice)
        {
            case 1: {
                resMsg = xmlManager.loadXMLfile();
                break;
            }
            case 8: {
                System.exit(0);
                break;
            }
        }

        return resMsg;
    }
}
