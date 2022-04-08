package userinterface;

import dto.DataTransferObject;
import dto.infoholder.CustomerDataObject;
import dto.infoholder.LoanDataObject;
import engine.EngineManager;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class UserInterface {

    private final Scanner scanner;
    private EngineManager engine; // Program engine, contains all program processes.

    public UserInterface() {
        scanner = new Scanner(System.in); // Init scanner.
        engine = new EngineManager(); // Init program engine.
    }

    // Initialize menu display.
    public void init() {
        do {
            this.presentMenu();
            int userChoice = this.getUserChoice(); // Read user choice.
            this.cleanBuffer(); // Clean '\n' stuck in buffer after reading choice.

            // Skip current loop when user chose 2/3/6/7 without loading xml file first.
            if ((userChoice == 2 || userChoice == 3 || userChoice == 6 || userChoice == 7)
                    && !this.engine.isFileLoaded()) {
                System.out.println("Error: Xml file must be loaded first.");
                continue;
            }

            // Handle user choice by calling relevant function from program engine.
            switch (userChoice) {
                case 1: {
                    System.out.print("Enter full xml file path: ");
                    String filePathString = scanner.nextLine();
                    // Try load xml file and validate it, print DTO result.
                    System.out.println(engine.loadXML(filePathString));
                    break;
                }
                case 2: {
                   // Get all loans in BankSystem.
                   List<LoanDataObject> allLoans = this.engine.getAllLoansData();
                   if(allLoans.size() == 0)
                       System.out.println("There are no loans exist.");
                   else {
                       System.out.println("All loans information:");
                       for (LoanDataObject loanData : allLoans)
                           loanData.showLoan();
                   }
                   break;
                }
                case 3: {
                    List<CustomerDataObject> getCustomersLoansData = this.engine.getAllCustomersLoansAndLogs();
                    if(getCustomersLoansData == null)
                        System.out.println("There are no customers to report on.");
                    else {
                        for(CustomerDataObject data : getCustomersLoansData) {
                            System.out.println(data.getName() + ":"); // prints customer name.

                            //Print each customer logs.
                            System.out.println("   Customer operations list: ");
                            if(data.getLogCustomer() == null || data.getLogCustomer().size() <= 0)
                                System.out.println("      There are no operations to view.");
                            else
                                System.out.println(data.getLogCustomer());

                            //Print eacg customer investments.
                            System.out.println("   Customer Investments list: ");
                            if(data.getInvestmentList() == null || data.getInvestmentList().size() <= 0)
                                System.out.println("      There are no investments to view.");
                            else {
                                // Trigger relevant printing function for each of the customer investments.
                                for(LoanDataObject loanData : data.getInvestmentList())
                                    System.out.println(loanData.getLoanDetails());
                            }

                            //Print eacg customer loans taken.
                            System.out.println("   Customer taken loans list: ");
                            if(data.getLoanList() == null || data.getLoanList().size() <= 0)
                                System.out.println("      There are no taken loans to view.");
                            else {
                                // Trigger relevant printing function for each of the customer investments.
                                for(LoanDataObject loanData : data.getLoanList())
                                    System.out.println(loanData.getLoanDetails());
                            }

                        }
                    }
                    break;
                }
                case 4: {
                    List<String> customersNames = this.engine.getAllCustomersNames();
                    if(customersNames == null) {
                        System.out.println("Error: There are no customers exist.");
                        continue;
                    }

                    this.printCustomersNames(customersNames);
                    String userName = this.getUserNameAsInput(customersNames);

                    // Wait for the user to insert a valid amount of money.
                    int depositeAmount = -1;
                    do {
                        System.out.println("Enter amount to deposit: ");
                        if(scanner.hasNextInt())
                            depositeAmount = scanner.nextInt();

                        this.cleanBuffer();

                        if(depositeAmount <= 0)
                            System.out.println("Error: Please enter a valid amount to deposit.");

                    } while(depositeAmount <= 0);

                    this.engine.depositeMoney(userName, depositeAmount);
                    System.out.println("Bank: " + userName + " deposit made successfully.");
                    break;
                }
                case 5: {
                    List<String> customersNames = this.engine.getAllCustomersNames();
                    if(customersNames == null) {
                        System.out.println("Error: There are no customers exist.");
                        continue;
                    }

                    this.printCustomersNames(customersNames);
                    String userName = this.getUserNameAsInput(customersNames);

                    // Wait for the user to insert a valid amount of money.
                    int withdrawAmount = -1;
                    do {
                        System.out.println("Enter amount to withdraw: ");

                        if(scanner.hasNextInt())
                            withdrawAmount = scanner.nextInt();

                        this.cleanBuffer();

                        if(withdrawAmount <= 0)
                            System.out.println("Error: Withdraw amount must be greater then 0.");

                    } while(withdrawAmount <= 0);

                    try {
                        this.engine.withdrawMoney(userName, withdrawAmount);
                        System.out.println("Bank: " + userName + " withrawal made successfully.");
                    } catch(DataTransferObject e) {
                        System.out.println(e);
                    }
                }
                case 8: {
                    System.exit(0);
                    break;
                }
                default: {
                    System.out.println("Error: invalid choice.");
                }
            }

        } while(true);
    }

    // Get name that exist in customers names list.
    private String getUserNameAsInput(List<String> customersNames) {
        // Wait for the user to insert an existing name.
        String userName = "";
        do {
            System.out.println("Enter a name from the list: ");
            userName = scanner.nextLine();

            if(customersNames.indexOf(userName) == -1)
                System.out.println("Error: There is no user with that given name exist.");

        } while(customersNames.indexOf(userName) == -1);

        return userName;
    }

    // getUserChoice -> Check if next input is an int, if so return it else return 0 (non-valid choice).
    private int getUserChoice() {
        if(scanner.hasNextInt())
            return scanner.nextInt();

        return 0;
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

    // Prints all customers names.
    private void printCustomersNames(List<String> someList) {
        System.out.println("List of users names: ");
        someList.stream().forEach(customerName -> System.out.println(customerName));
    }

    // cleanBuffer -> Read next line to clean buffer from un relevant data left.
    private void cleanBuffer() {
        scanner.nextLine();
    }
}
