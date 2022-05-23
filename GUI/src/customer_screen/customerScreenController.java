package customer_screen;

import Utils.*;
import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import engine.EngineManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
    @FXML private TextField minInterest, minYaz, maxOpenLoans, ownershipPrecent;

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

        this.setFilterOptionsListeners();
    }

    // Set ChangeEventListener to each filter component.
    private void setFilterOptionsListeners() {

        // Bind slider value to label.
        this.investmentAmount.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                amountLabel.setText(newValue.intValue() + "");
                loansToInvestTableController.setMinAmount(newValue.intValue()); // Filter by investmentAmount.
            }
        });

        // Listener for CheckComboBox item pick -> updating cats list in view.
        this.filterCats.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                loansToInvestTableController.updateFilterCategories(filterCats.getCheckModel().getCheckedItems());
        } });

        // text fields bindings.
        this.minInterest.textProperty().addListener((observable, oldValue, newValue) -> {
            if(this.validateTextField(oldValue, newValue, minInterest))
                this.loansToInvestTableController.setMinInterest(Integer.parseInt(newValue));
        });

        this.minYaz.textProperty().addListener((observable, oldValue, newValue) -> {
            if(this.validateTextField(oldValue, newValue, this.minYaz))
                this.loansToInvestTableController.setMinYaz(Integer.parseInt(newValue));
        });

        this.maxOpenLoans.textProperty().addListener((observable, oldValue, newValue) -> {
            if(this.validateTextField(oldValue, newValue, maxOpenLoans))
                this.loansToInvestTableController.setMaxOpenLoans(Integer.parseInt(newValue));
        });

        this.ownershipPrecent.textProperty().addListener((observable, oldValue, newValue) -> {
            this.validateTextField(oldValue, newValue, ownershipPrecent);

            if(Integer.parseInt(newValue) > 100)
                this.ownershipPrecent.setText("100");
        });

    }

    // Set engine.
    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
    }

    // Get the current chosen user.
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

        this.setCategoryList();
    }

    /* SCRAMBLE PAGE */

    // Show and re-render all loans to invest in list.
    private void updateScramble() {

        // Get all loans and exclude current user's ones.
        List<LoanDataObject> listOfLoansExcludedCurrentUser = this.engineManager.getAllLoansData().stream()
                .filter(e -> (e.getLoanStatus() == LoanDataObject.Status.NEW || e.getLoanStatus() == LoanDataObject.Status.PENDING) && !e.getOwner().equals(this.currentUser.getUsername())).collect(Collectors.toList());

        // update number of open loans for customer.
        for(LoanDataObject e : listOfLoansExcludedCurrentUser)
            e.setUnfinishedLoansNumber(this.engineManager.getCustomerByName(e.getOwner()).countUnfinishedLoans());

        loansToInvestTableController.setLoanItems(listOfLoansExcludedCurrentUser);

        this.setMaxAmountToInvest(); // Define max amount to invest by user balance.
    }

    // Set slider max value to be user balance (even if balance changed).
    private void setMaxAmountToInvest() {
         this.investmentAmount.setMax(this.engineManager.getBalanceOfCustomerByName(this.currentUser.getUsername()));
    }

    private void cleanListOfLoansToInvest() {
        // Clear list of loans to invest.
        loansToInvestList.clear();
    }

    public void resetSettings() {
        this.investmentAmount.adjustValue(0); // Updating label too because of binding.
        minInterest.setText("0");
        minYaz.setText("0");
        maxOpenLoans.setText("0");
        ownershipPrecent.setText("0");
    }

    private void setCategoryList() {
        ObservableList<String> strings = FXCollections.observableArrayList();
        strings.setAll(this.engineManager.getBankCategories());
        this.filterCats.getItems().setAll(strings);
    }

    // Validator function for binding textfield to check textfield dont contain non-integers.
    private boolean validateTextField(String oldValue, String newValue, TextField tf) {
        try {
            int newV = Integer.parseInt(newValue);

            if(newV < 0)
                throw new NumberFormatException();

            return true;

        } catch(NumberFormatException e) {
            tf.setText(oldValue);
            return false;
        }
    }
    /* END SCRAMBLE PAGE */
}
