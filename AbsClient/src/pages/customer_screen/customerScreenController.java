package pages.customer_screen;

import Utils.*;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;
import pages.tables_design.payment_area.PaymentAreaController;
/*import pages.payment_area.PaymentAreaController;
import tableview.loan_tableview.loansTableView;
import tableview.payment_view.PaymentTableView;
import tableview.transactions_view.TransactionTable;*/

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class customerScreenController implements Initializable {

    // DATA MEMBERS
    private CustomerDataObject currentCustomer;
    private TextInputDialog moneyPopup;
    private Alert alertDialog;
    private List<LoanDataObject> loansToInvestList; // Holds loans to invest in when user mark them.

    // FXML MEMBERS
    @FXML private AnchorPane customerPane;
    @FXML private User currentUser;
    /*@FXML private loansTableView myLoansTableController, myInvestmentsLoansController, loansToInvestTableController;
    @FXML private TransactionTable myTransactionListController;
    @FXML private PaymentTableView paymentTableController;*/
    @FXML private PaymentAreaController paymentAreaController;
    @FXML private Label currentBalance;
    @FXML private Button depositBtn, withdrawalBtn;

    // SCRAMBLE
    @FXML private Slider investmentAmount;
    @FXML private Label amountLabel;
    @FXML private CheckComboBox<String> filterCats;
    @FXML private TextField minInterest, minYaz, maxOpenLoans, ownershipPrecent;
    @FXML private ProgressBar progressBar;

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

        // Set filtering listeners.
        //this.setFilterOptionsListeners();

        // Deposit button functionality
        /*
        depositBtn.setOnMouseClicked(event -> {

            moneyPopup.setTitle("Deposit");
            Optional<String> result = moneyPopup.showAndWait();

            // If closed
            if(!result.isPresent()) {
                moneyPopup.getEditor().setText("");
                return;
            }

            String value = String.valueOf(moneyPopup.getEditor().getText());
            moneyPopup.getEditor().setText(""); // empty input text.

            // Try parsing input to int and if success update user bank.
            try {
                int valueOfInput = Integer.parseInt(value);

                // Trying to deposit negative number.
                if(valueOfInput <= 0) {
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

            refreshPaymentView();
        });

        // Withdrawal button functionality
        withdrawalBtn.setOnMouseClicked(event -> {
            moneyPopup.setTitle("Withdrawal");
            Optional<String> result = moneyPopup.showAndWait();

            // If closed
            if(!result.isPresent()) {
                moneyPopup.getEditor().setText("");
                return;
            }

            String value = String.valueOf(moneyPopup.getEditor().getText());
            moneyPopup.getEditor().setText(""); // empty input text.

            // Try parsing input to int and if success update user bank.
            try {
                int valueOfInput = Integer.parseInt(value);

                // Trying to deposit negative number.
                if(valueOfInput <= 0) {
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

            refreshPaymentView();
        });*/

        // adding checkbox for invest to scramble.
        TableColumn investColumn = new TableColumn("Invest");
        investColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LoanDataObject, CheckBox>, ObservableValue<CheckBox>>() {

            @Override
            public ObservableValue<CheckBox> call(
                    TableColumn.CellDataFeatures<LoanDataObject, CheckBox> arg0) {

                LoanDataObject loan = arg0.getValue();
                CheckBox checkBox = new CheckBox();

                checkBox.selectedProperty().setValue(loansToInvestList.contains(loan));

                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                                        Boolean old_val, Boolean new_val) {

                        if(new_val == true)
                        {
                            if(!loansToInvestList.contains(loan))
                                loansToInvestList.add(loan);
                        } else {
                            if(loansToInvestList.contains(loan))
                                loansToInvestList.remove(loan);
                        }

                    }
                });

                return new SimpleObjectProperty<CheckBox>(checkBox);

            }

        });

        /*// adding CheckBox column to scramble tableview.
        loansToInvestTableController.addCheckboxColumn(investColumn);

        // send reference to list of loans to invest.
        loansToInvestTableController.setLoansToInvestList(this.loansToInvestList);*/

        // send customer controller instance to payment table view to update payment area.
        //paymentTableController.setCustomerController(this);
        //paymentAreaController.setCustomerController(this);

    }

    // Update current customer.
    public void setUser(String username) {
        currentUser.setUsername(username);
    }

    /*
    public void updatePayments() {
        paymentTableController.setPaymentList(currentCustomer.getLoanList());
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
            String temp = validateTextField(oldValue, newValue, minInterest);
            this.loansToInvestTableController.setMinInterest(Integer.parseInt(temp));
        });

        this.minYaz.textProperty().addListener((observable, oldValue, newValue) -> {
            String temp = validateTextField(oldValue, newValue, minYaz);
            this.loansToInvestTableController.setMinYaz(Integer.parseInt(temp));
        });

        this.maxOpenLoans.textProperty().addListener((observable, oldValue, newValue) -> {

            String temp = validateTextField(oldValue, newValue, maxOpenLoans);
            this.loansToInvestTableController.setMaxOpenLoans(Integer.parseInt(temp));

        });

        this.ownershipPrecent.textProperty().addListener((observable, oldValue, newValue) -> {

            String temp = validateTextField(oldValue, newValue, ownershipPrecent);

            if(Integer.parseInt(temp) > 100)
                this.ownershipPrecent.setText("100");
        });

    }
*/
    // Update view of payment area when choosing a loan.
    public void setPaymentArea(LoanDataObject d){
        paymentAreaController.updateInfo(d);
    }
/*
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

        this.updatePayments();
        paymentTableController.resetChoiceBtn();
    }

    public void updateCustomerInfo() {
        this.updateCustomerInfo(this.currentUser.getUsername());
    }

    *//* SCRAMBLE PAGE *//*

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
        this.resetSettings(); // reset filter object's values.

    }

    // Set slider max value to be user balance (even if balance changed).
    private void setMaxAmountToInvest() {
         this.investmentAmount.setMax(this.engineManager.getBalanceOfCustomerByName(this.currentUser.getUsername()));
    }

    public void resetSettings() {

        // reset filter object's value.
        this.investmentAmount.adjustValue(0); // Updating label too because of binding.
        minInterest.setText("");
        minYaz.setText("");
        maxOpenLoans.setText("");
        ownershipPrecent.setText("");

        // Categories in filer (CheckComboBox) set all to be selected.
        for(String s : this.filterCats.getItems()) {
            this.filterCats.getItemBooleanProperty(s).set(true);
        }

        // Add checkbox column for scramble page.
        this.resetCheckBoxToInvestList();
    }

    // Update and init category list to choose from in filter.
    private void setCategoryList() {
        ObservableList<String> strings = FXCollections.observableArrayList();
        strings.setAll(this.engineManager.getBankCategories());
        this.filterCats.getItems().setAll(strings);
    }

    // Validator function for binding textfield to check textfield dont contain non-integers.
    private String validateTextField(String oldValue, String newValue, TextField tf) {

        if (!newValue.matches("\\d*"))
            tf.setText(newValue = newValue.replaceAll("[^\\d]", ""));

        if(newValue.equals("") || newValue.isEmpty())
            newValue = "0";

        return newValue;
    }

    private void resetCheckBoxToInvestList() {

        loansToInvestList.clear(); // clear loans to invest list.
        loansToInvestTableController.resetCheckboxColumn();
    }
*/
    // Make investment button functionality.
    public void makeInvestmentClicked(ActionEvent actionEvent) {

        // if user didnt chose any loans to invest.
        if(this.loansToInvestList.isEmpty()) {
            alertDialog.setContentText("Please choose some loans to invest.");
            alertDialog.showAndWait();
            return;
        }

        /*moneyPopup.setTitle("Investment");
        moneyPopup.setContentText("Enter amount of money to invest: ");
        Optional<String> result = moneyPopup.showAndWait();
        if(!result.isPresent())
            return;

        String value = String.valueOf(moneyPopup.getEditor().getText());

        // Try parsing input to int and if success update user bank.
        try {
            int valueOfInput = Integer.parseInt(value);

            // Trying to deposit negative number.
            if(valueOfInput <= 0)
                throw new NumberFormatException("Negative");

            if(valueOfInput > this.engineManager.getBalanceOfCustomerByName(this.currentUser.getUsername()))
                throw new DataTransferObject("You cant invest more money then your balance.", BankSystem.getCurrentYaz());

            // Invest proccess
            List<Triple<String,Integer,String>> nameOfLoansToInvest = new ArrayList<>();
            for(LoanDataObject l : loansToInvestList)
                nameOfLoansToInvest.add(new Triple<>(l.getOwner(), l.getLoanOpeningTime(), l.getLoanID()));

            loanTask
                    = new LoanTask(nameOfLoansToInvest, this.engineManager, valueOfInput, this.currentUser.getUsername(), alertDialog);

            // Progressbar
            progressBar.setVisible(true);
            progressBar.progressProperty().bind(loanTask.progressProperty());

            // Run task as a thread.
            Thread taskThread = new Thread(loanTask);
            taskThread.setDaemon(true);
            taskThread.start();

        } catch(NumberFormatException | DataTransferObject nfe) {

            // If ok button was pressed!
            if(result.isPresent()) {
                if(nfe instanceof DataTransferObject)
                    alertDialog.setContentText(nfe.getMessage());
                else if(nfe.getMessage().equals("Negstive"))
                    alertDialog.setContentText("Please enter positive number.");
                else
                    alertDialog.setContentText("Please enter a valid number.");

                alertDialog.showAndWait();
            }
        } finally {
            moneyPopup.getEditor().setText(""); // empty input text.
            alertDialog.setHeaderText("Error");
            alertDialog.setAlertType(Alert.AlertType.ERROR);
        }*/

    }
/*
    public List<CustomerAlertData> getCustomerAlertList() {
        return currentCustomer.getListOfAlerts();
    }
    *//* END SCRAMBLE PAGE *//*

    public AnchorPane getCustomerMainPane() {
        return this.customerPane;
    }
*/
    public int getCurrentCustomerBalance(){
      return  currentCustomer.getBalance();
    }
/*
    // Payment page

    // Pay all loans current payment.
    public void handleCustomerLoansPayments(LoanDataObject loan, int amountToPay) {
        this.engineManager.handleCustomerLoansPayments(loan, amountToPay);
    }

    // Close all loans debt or specific loan.
    public void handleCustomerPayAllDebt(List<LoanDataObject> loans) throws DataTransferObject {
        this.engineManager.handleCustomerPayAllDebt(loans);
    }

    public void refreshPaymentTable() {
        paymentTableController.refreshTable();
   }
*/
    public void refreshPaymentView() { paymentAreaController.refreshRelevantData(); }
}