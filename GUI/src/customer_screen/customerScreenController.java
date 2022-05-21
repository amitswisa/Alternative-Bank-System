package customer_screen;

import Utils.*;
import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import engine.EngineManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.StageStyle;
import org.controlsfx.control.CheckComboBox;
import tableview.loan_tableview.loansTableView;
import tableview.transactions_view.TransactionTable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class customerScreenController implements Initializable {

    // DATA MEMBERS
    private EngineManager engineManager;
    private CustomerDataObject currentCustomer;
    private TextInputDialog moneyPopup;
    private Alert alertDialog;
    private List<LoanDataObject> allLoans;
    private List<String> loansToInvestList; // Holds loans to invest in when user mark them.

    // FXML MEMBERS
    @FXML private User currentUser;
    @FXML private loansTableView myLoansTableController, myInvestmentsLoansController, loansToInvestTableController;
    @FXML private TransactionTable myTransactionListController;
    @FXML private Label currentBalance;
    @FXML private Button depositBtn, withdrawalBtn;

    // SCRAMBLE
    @FXML private Slider investmentAmount;
    @FXML private Label amountLabel;
    @FXML private CheckComboBox<String> filterCats;
    @FXML private TextField maxInterest, maxYax, maxOpenLoans, ownershipPrecent;

    public customerScreenController() {
        currentUser = new User();

        //Text dialog settings
        loansToInvestList = new ArrayList<>();
        alertDialog = new Alert(Alert.AlertType.ERROR);
        moneyPopup = new TextInputDialog();
        moneyPopup.setHeaderText("Enter amount of money: ");
        moneyPopup.initStyle(StageStyle.UTILITY);
        moneyPopup.setGraphic(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Do some changes when client changes user type.
        currentUser.getUsernameProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals(currentUser.getDefaultUserName()))
                    return;

                updateCustomerInfo(newValue);
                cleanListOfLoansToInvest();
                updateScramble();
            }
        });

        // Deposit button functionality
        depositBtn.setOnMouseClicked(event -> {
            moneyPopup.setTitle("Deposit");
            Optional<String> result = moneyPopup.showAndWait();
            String value = String.valueOf(moneyPopup.getEditor().getText());
            moneyPopup.getEditor().setText(""); // empty input text.

            // Try parsing input to int and if success update user bank.
            try {
                int valueOfInput = Integer.parseInt(value);

                // Trying to deposit negative number.
                if(valueOfInput < 0) {
                    throw new NumberFormatException("Negative");
                }

                // Deposite money and refresh view.
                this.engineManager.depositeMoney(this.currentUser.getUsername(), valueOfInput);
                this.updateCustomerInfo(this.currentUser.getUsername());
                this.setMaxAmountToInvest();

                currentBalance.setText("Current balance: " + this.engineManager.getBalanceOfCustomerByName(this.currentUser.getUsername()));
            } catch(NumberFormatException nfe) {

                // If ok button was pressed!
                if(result.isPresent()) {
                    if(nfe.getMessage().equals("Negstive"))
                        alertDialog.setContentText("Cant deposit negative numbers.");
                    else
                        alertDialog.setContentText("Please enter a number.");

                    alertDialog.showAndWait();
                }
            }
        });

        // Withdrawal button functionality
        withdrawalBtn.setOnMouseClicked(event -> {
            moneyPopup.setTitle("Withdrawal");
            Optional<String> result = moneyPopup.showAndWait();
            String value = String.valueOf(moneyPopup.getEditor().getText());
            moneyPopup.getEditor().setText(""); // empty input text.

            // Try parsing input to int and if success update user bank.
            try {
                int valueOfInput = Integer.parseInt(value);

                // Trying to deposit negative number.
                if(valueOfInput < 0) {
                    throw new NumberFormatException("Cant Withdrawal negative numbers.");
                }

                // Withdraw money and refresh view.
                this.engineManager.withdrawMoney(this.currentUser.getUsername(), valueOfInput);
                this.updateCustomerInfo(this.currentUser.getUsername());
                this.setMaxAmountToInvest();

                currentBalance.setText("Current balance: " + this.engineManager.getBalanceOfCustomerByName(this.currentUser.getUsername()));
            } catch(NumberFormatException | DataTransferObject nfe) {

                // If ok button was pressed!
                if(result.isPresent()) {
                    if(nfe instanceof DataTransferObject)
                        alertDialog.setContentText(nfe.getMessage());
                    else
                        alertDialog.setContentText("Please enter a number.");

                    alertDialog.showAndWait();
                }

            }
        });

        // Bind slider value to label.
        this.investmentAmount.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                amountLabel.setText(newValue.intValue() + "");
                loansToInvestTableController.filterByAmount(newValue.intValue()); // Filter by investmentAmount.
            }
        });
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    // When changings customer view rerender all customer details.
    private void updateCustomerInfo(String newName) {

        // update current user Customer information DTO when selected user changes.
        this.currentCustomer = this.engineManager.getCustomerByName(newName);

        // Update user balance text.
        currentBalance.setText("Current balance: " + this.engineManager.getBalanceOfCustomerByName(newName));

        // Update tables that shows user loans, investments and transactions.
        myLoansTableController.setLoanItems(this.currentCustomer.getLoanList());
        myInvestmentsLoansController.setLoanItems(this.currentCustomer.getInvestmentList());
        myTransactionListController.setTransactionList(this.currentCustomer.getLogCustomer());

    }


    /* SCRAMBLE PAGE */

    // Show and re-render all loans to invest in list.
    private void updateScramble() {
        // Get all loans and exclude current user's ones.
        List<LoanDataObject> listOfLoansExcludedCurrentUser = this.engineManager.getAllLoansData().stream()
                .filter(e -> (e.getLoanStatus() == LoanDataObject.Status.NEW || e.getLoanStatus() == LoanDataObject.Status.PENDING) && !e.getOwner().equals(this.currentUser.getUsername())).collect(Collectors.toList());
        loansToInvestTableController.setLoanItems(listOfLoansExcludedCurrentUser);

        this.setMaxAmountToInvest();
    }

    private void setMaxAmountToInvest() {
         this.investmentAmount.setMax(this.engineManager.getBalanceOfCustomerByName(this.currentUser.getUsername()));
    }

    private void cleanListOfLoansToInvest() {
        // Clear list of loans to invest.
        loansToInvestList.clear();
    }

    public void resetSettings() {
        this.investmentAmount.adjustValue(0); // Updating label too because of binding.
    }

    /* END SCRAMBLE PAGE */
}
