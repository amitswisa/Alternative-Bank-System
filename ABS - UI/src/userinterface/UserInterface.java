package userinterface;

import engine_managers.EngineManager;
import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;
    private EngineManager engine;

    public UserInterface() {
        scanner = new Scanner(System.in); // Init scanner.
        engine = new EngineManager(); // Init program engine.
    }

    // Initialize menu display.
    public void init() {
        do {
            this.presentMenu(); // Print menu.
            int userChoice = scanner.nextInt(); // Read user choice.
            if(userChoiseIsValid(userChoice)) {
                this.cleanBuffer(); // Clean '\n' stuck in buffer after reading choice.

                // Handle user choice.
                switch (userChoice) {
                    case 1: {
                        System.out.print("Enter full xml file path: ");
                        String filePathString = scanner.nextLine();
                        System.out.println(this.engine.readXmlFile(filePathString)); // Handle load xml user's choice.
                        break;
                    }
                    case 8: {
                        System.exit(0);
                        break;
                    }
                }
            }

        } while(true);
    }

    // TODO - Validation tests.
    private boolean userChoiseIsValid(int userChoice) {
        // TO-DO
        return true;
    }

    // presentMenu -> Print menu to screen.
    private void presentMenu() {
        System.out.println("Current Time Unit: 1.");
        System.out.println("1. Load XML file.");
        System.out.println("2. Get available information about loans and status.");
        System.out.println("3. Present members information.");
        System.out.println("4. Deposit amount of money to member's account.");
        System.out.println("5. Withdraw amount of money from member's account.");
        System.out.println("6. Start investing.");
        System.out.println("7. Time unit management.");
        System.out.println("8. Exit.");
    }

    // cleanBuffer -> Read next line to clean buffer from un relevant data left.
    private void cleanBuffer() {
        scanner.nextLine();
    }
}
