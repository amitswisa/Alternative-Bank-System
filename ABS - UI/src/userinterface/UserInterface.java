package userinterface;

import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;

    public UserInterface() {
        scanner = new Scanner(System.in);
    }

    // Present menu
    public void init() {
        do {
            this.presentMenu(); // Print menu.
            int userChoice = this.getUserChoice(); // Get choice from buffer.

        } while(true);
    }

    /*
        TODO - Validation tests.
    */
    private int getUserChoice() {
        int userChoice = scanner.nextInt(); // Get choice from buffer.
        return userChoice;
    }

    // Print menu to screen.
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

}
