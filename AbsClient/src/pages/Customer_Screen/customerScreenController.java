package pages.Customer_Screen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Customer.AppCustomer;
import dto.objectdata.CustomerAlertData;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import org.controlsfx.control.CheckComboBox;
import org.jetbrains.annotations.NotNull;
import parts.payment_area.PaymentAreaController;
import server_con.HttpClientUtil;
import parts.tableview.loan_tableview.loansTableView;
import parts.tableview.payment_view.PaymentTableView;
import parts.tableview.transactions_view.TransactionTable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class customerScreenController implements Initializable {

    // DATA MEMBERS
    private AppCustomer currentCustomer;
    private final TextInputDialog moneyPopup;
    private final Alert alertDialog;
    private final List<LoanDataObject> loansToInvestList; // Holds loans to invest in when user mark them.
    //private LoanTask loanTask;

    // FXML MEMBERS
    @FXML private AnchorPane customerPane;
    @FXML private loansTableView myLoansTableController, myInvestmentsLoansController, loansToInvestTableController;
    @FXML private TransactionTable myTransactionListController;
    @FXML private PaymentTableView paymentTableController;
    @FXML private PaymentAreaController paymentAreaController;
    @FXML private Label currentBalance;
    @FXML private Button depositBtn, withdrawalBtn;

    // Invest
    @FXML private Slider investmentAmount;
    @FXML private Label amountLabel;
    @FXML private CheckComboBox<String> filterCats;
    @FXML private TextField minInterest, minYaz, maxOpenLoans, ownershipPrecent;

    //Make New Loan
    @FXML private TextField loanID, capital, interestPerPayment, paymentsInterval, loanTotalTime;
    @FXML private ChoiceBox category;
    @FXML private Button createLoan;

    public customerScreenController() {

        //Text dialog settings
        loansToInvestList = new ArrayList<>();
        alertDialog = new Alert(Alert.AlertType.INFORMATION);
        moneyPopup = new TextInputDialog();
        moneyPopup.setHeaderText("Enter amount of money: ");
        moneyPopup.initStyle(StageStyle.UTILITY);
        moneyPopup.setGraphic(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Set filtering listeners.
        this.setFilterOptionsListeners();

        // Deposit button functionality
        depositBtn.setOnMouseClicked(event -> {
            this.fundsActionsFunction("Deposit");
        });

        // Withdrawal button functionality
        withdrawalBtn.setOnMouseClicked(event -> {
            fundsActionsFunction("Withdraw");
        });

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

        // adding CheckBox column to scramble tableview.
        loansToInvestTableController.addCheckboxColumn(investColumn);

        // send reference to list of loans to invest.
        loansToInvestTableController.setLoansToInvestList(this.loansToInvestList);

        // send customer controller instance to payment table view to update payment area.
        paymentTableController.setCustomerController(this);
        paymentAreaController.setCustomerController(this);
    }

    // Handle Deposit/Withdrawal requests.
    private void fundsActionsFunction(String action) {
        moneyPopup.setTitle(action);
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

            // Building HTTP POST Request.
            Request fundRequest =
                    new Request.Builder()
                            .url(HttpClientUtil.PATH + "/FundsActions")
                            .post(new FormBody.Builder()
                                    .add("customerName", this.currentCustomer.getName())
                                    .add("moneyAmount", String.valueOf(valueOfInput))
                                    .add("action", action)
                                    .build())
                            .build();

            // Send POST Request and receive response from server.
            Response fundResponse = HttpClientUtil.sendSyncRequest(fundRequest);

            // Deposit happened.
            if(fundResponse.code() == 200) {
                alertDialog.setContentText(fundResponse.body().string());

                if(action.equals("Deposit"))
                    this.currentCustomer.depositMoney(valueOfInput);
                else
                    this.currentCustomer.withdrawMoney(valueOfInput);

                //this.setMaxAmountToInvest();

            } else
                alertDialog.setContentText("Unexpected problem occurred.");

        } catch(NumberFormatException nfe) {

            // If ok button was pressed!
            if(nfe.getMessage().equals("Negstive"))
                alertDialog.setContentText("Cant " + action + " negative numbers.");
            else
                alertDialog.setContentText("Please enter a number.");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            alertDialog.showAndWait();
        }

        refreshPaymentView();
    }

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
            public void onChanged(Change<? extends String> c) {
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

    // Update view of payment area when choosing a loan.
    public void setPaymentArea(LoanDataObject d){
        paymentAreaController.updateInfo(d);
    }

    /* SCRAMBLE PAGE */

    // Show and re-render all loans to invest in list.
    private void updateScramble() {
/*
        // Get all loans and exclude current user's ones.
        List<LoanDataObject> listOfLoansExcludedCurrentUser = this.engineManager.getAllLoansData().stream()
                .filter(e -> (e.getLoanStatus() == LoanDataObject.Status.NEW || e.getLoanStatus() == LoanDataObject.Status.PENDING) && !e.getOwner().equals(this.currentUser.getUsername())).collect(Collectors.toList());

        // update number of open loans for customer.
        for(LoanDataObject e : listOfLoansExcludedCurrentUser)
            e.setUnfinishedLoansNumber(this.engineManager.getCustomerByName(e.getOwner()).countUnfinishedLoans());

        loansToInvestTableController.setLoanItems(listOfLoansExcludedCurrentUser);

        this.setMaxAmountToInvest(); // Define max amount to invest by user balance.
        this.resetSettings(); // reset filter object's values.*/

    }

    // Set slider max value to be user balance (even if balance changed).
    /*private void setMaxAmountToInvest() {
         this.investmentAmount.setMax(this.engineManager.getBalanceOfCustomerByName(this.currentUser.getUsername()));
    }*/

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

    // Make investment button functionality.
    public void makeInvestmentClicked(ActionEvent actionEvent) {

        // if user didnt chose any loans to invest.
        if(this.loansToInvestList.isEmpty()) {
            alertDialog.setContentText("Please choose some loans to invest.");
            alertDialog.showAndWait();
            return;
        }

        moneyPopup.setTitle("Investment");
        moneyPopup.setContentText("Enter amount of money to invest: ");
        Optional<String> result = moneyPopup.showAndWait();
        if(!result.isPresent())
            return;

        String value = String.valueOf(moneyPopup.getEditor().getText());

        // Try parsing input to int and if success update user bank.
       // try {
            int valueOfInput = Integer.parseInt(value);

            // Trying to deposit negative number.
            if(valueOfInput <= 0)
                throw new NumberFormatException("Negative");

           /* if(valueOfInput > this.engineManager.getBalanceOfCustomerByName(this.currentUser.getUsername()))
                throw new DataTransferObject("You cant invest more money then your balance.", BankSystem.getCurrentYaz());*/

            // Invest proccess
            /*List<Triple<String,Integer,String>> nameOfLoansToInvest = new ArrayList<>();
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
        }
*/
    }

    public List<CustomerAlertData> getCustomerAlertList() {
        return currentCustomer.getListOfAlerts();
    }
    /* END SCRAMBLE PAGE */

    public AnchorPane getCustomerMainPane() {
        return this.customerPane;
    }

    public int getCurrentCustomerBalance(){
      return currentCustomer.getBalance();
    }

    // Payment page

    // Pay all loans current payment.
    /*public void handleCustomerLoansPayments(LoanDataObject loan, int amountToPay) {
        this.engineManager.handleCustomerLoansPayments(loan, amountToPay);
    }*/

    // Close all loans debt or specific loan.
    /*public void handleCustomerPayAllDebt(List<LoanDataObject> loans) throws DataTransferObject {
        this.engineManager.handleCustomerPayAllDebt(loans);
    }*/

    public void refreshPaymentTable() {
        paymentTableController.refreshTable();
    }

    public void refreshPaymentView() { paymentAreaController.refreshRelevantData(); }

    public void setCusomter(AppCustomer customer) {
        this.currentCustomer = customer;
        this.currentBalance.setText("Current balance: " + this.currentCustomer.getBalance());

        // Set-up balance change listener.
        this.currentCustomer.getBalanceAsIntegerProperty().addListener((observable, oldValue, newValue) -> {
            currentBalance.setText("Current balance: " + newValue.intValue());
        });

        // Bind tables that shows user loans, investments and transactions.
        myLoansTableController.setLoansObservableList(this.currentCustomer.getLoanList());
        myInvestmentsLoansController.setLoansObservableList(this.currentCustomer.getInvestmentList());
        myTransactionListController.setTransactionList(this.currentCustomer.getLogCustomer());

        //this.updatePayments();
        //paymentTableController.resetChoiceBtn();

    }

    public CheckComboBox<String> getCategoryComboBox() {
        return this.filterCats;
    }

    public void addCategoryToList(String category_name) {
        filterCats.getItems().add(category_name);
    }

    //Make New Loan page
    //loanID, capital, interestPerPayment, paymentsInterval, loanTotalTime;

    public void makeNewLoanClicked(ActionEvent actionEvent) {

        try {
            LoanDataObject newLoan = new LoanDataObject(currentCustomer.getName(), loanID.getText(), category.toString() , Integer.parseInt(capital.getText()),
                    Integer.parseInt(interestPerPayment.getText()), Integer.parseInt(paymentsInterval.getText()),
                    /*TODO- loan opening time = current yaz*/0, Integer.parseInt(loanTotalTime.getText()));

            if(newLoan.isValidLoan()){
             //TODO- request to the servlet to add the loan
               Gson gson = new GsonBuilder().registerTypeAdapter(LoanDataObject.class, new LoanDataObject.LoanDataObjectAdapter()).create();
               String jsonLoan = gson.toJson(newLoan, LoanDataObject.class);

               HttpClientUtil.runAsync(HttpClientUtil.PATH + "/ClientMakeLoanServlet", jsonLoan, new okhttp3.Callback() {
                   @Override
                   public void onFailure(@NotNull Call call, @NotNull IOException e) {
                       System.out.println(e.getMessage());
                   }

                   @Override
                   public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    //TODO
                   }
               });
           } else {

           }

        } catch (Error e){

}

    }
}
