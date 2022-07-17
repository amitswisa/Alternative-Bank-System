package pages.Customer_Screen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Customer.AppCustomer;
import dto.JSON.InvestmentData;
import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerAlertData;
import dto.objectdata.LoanDataObject;
import dto.objectdata.Triple;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
import java.util.*;

import static components.Customer.AppCustomer.getTimeInYazAsInteger;

public class customerScreenController implements Initializable {

    // DATA MEMBERS
    private AppCustomer currentCustomer;
    private final TextInputDialog moneyPopup;
    private final Alert alertDialog;
    private final List<LoanDataObject> loansToInvestList; // Holds loans to invest in when user mark them.
    private final ObservableList<LoanDataObject> allLoans;

    // FXML MEMBERS
    @FXML private AnchorPane customerPane;
    @FXML private loansTableView myLoansTableController, myInvestmentsLoansController, loansToInvestTableController, LoansToSaleTable;
    @FXML private TransactionTable myTransactionListController;
    @FXML private PaymentTableView paymentTableController;
    @FXML private PaymentAreaController paymentAreaController;
    @FXML private Label currentBalance;
    @FXML private Button depositBtn, withdrawalBtn;

    // SCRAMBLE
    @FXML private Slider investmentAmount;
    @FXML private Label amountLabel;
    @FXML private CheckComboBox<String> filterCats;
    @FXML private TextField minInterest, minYaz, maxOpenLoans, ownershipPrecent;

    //Make New Loan
    @FXML private TextField loanID, capital, interestPerPayment, paymentsInterval, loanTotalTime;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private Label newLoanErMsg;

    public customerScreenController() {

        //Text dialog settings
        allLoans = FXCollections.observableArrayList();
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

                        if(new_val.booleanValue())
                        {
                            if(!loansToInvestList.contains(loan)) {
                                loansToInvestList.add(loan);
                            }
                        } else {
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

    // Bind filter options to all loans investment table view.
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
        try {
            int valueOfInput = Integer.parseInt(value);

            // Trying to deposit negative number.
            if(valueOfInput <= 0)
                throw new NumberFormatException("Negative");

            if(valueOfInput > this.getCustomerBalance())
                throw new DataTransferObject("You cant invest more money then your balance.", getTimeInYazAsInteger());

            // Invest proccess
            List<Triple<String,Integer,String>> nameOfLoansToInvest = new ArrayList<>();
            for(LoanDataObject l : loansToInvestList)
                nameOfLoansToInvest.add(new Triple<>(l.getOwner(), l.getLoanOpeningTime(), l.getLoanID()));

            // Send investment to server to invest.
            sendInvestmentsToServer(nameOfLoansToInvest, valueOfInput);
            resetCheckBoxToInvestList();


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

    }

    // Method that builds investmentData object and send to server with HTTP POST Request the information.
    private void sendInvestmentsToServer(List<Triple<String, Integer, String>> nameOfLoansToInvest, int valueOfInput) {
        InvestmentData investmentData = new InvestmentData(nameOfLoansToInvest, valueOfInput, this.currentCustomer.getName());
        String investmentDataAsJson = new Gson().toJson(investmentData, InvestmentData.class);

        Request request = new Request.Builder()
                .url(HttpClientUtil.PATH + HttpClientUtil.MAKE_INVESTMENT)
                .post(new FormBody.Builder()
                        .add("investmentData", investmentDataAsJson).build())
                .build();

        HttpClientUtil.runAsync(request, new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String msg = response.body().string();
                        System.out.println(msg);
                    }
                });
    }

    public List<CustomerAlertData> getCustomerAlertList() {
        return currentCustomer.getListOfAlerts();
    }

    public void refreshPaymentTable() {
        paymentTableController.refreshTable();
    }

    public void refreshPaymentView() { paymentAreaController.refreshRelevantData(); }

    public void setCustomer(AppCustomer customer) {
        this.currentCustomer = customer;

        // Initialize properties
        this.currentBalance.setText("Current balance: " + this.currentCustomer.getBalance());

        // Set-up balance change listener.
        this.currentCustomer.getBalanceAsIntegerProperty().addListener((observable, oldValue, newValue) -> {
            currentBalance.setText("Current balance: " + newValue.intValue());

            // Slider integer max range value.
            int sliderValue = 100;
            if(newValue.intValue() > 0)
                sliderValue = newValue.intValue();

            this.investmentAmount.setMax(sliderValue);
        });

        paymentTableController.setPaymentList(this.currentCustomer.getLoanList());
        myLoansTableController.setLoansObservableList(this.currentCustomer.getLoanList());
        myInvestmentsLoansController.setLoansObservableList(this.currentCustomer.getInvestmentList());
        myTransactionListController.setTransactionList(this.currentCustomer.getLogCustomer());
        loansToInvestTableController.setLoansObservableList(allLoans);
    }

    // Insert new loans into all loans list.
    public void updateAllLoansList(List<LoanDataObject> list) {
        list.forEach(e -> {
            int loanIndex = allLoans.indexOf(e);
            // -1 EQUALS NOT FOUND.
            if(e.getLoanStatus() == LoanDataObject.Status.NEW
                    || e.getLoanStatus() == LoanDataObject.Status.PENDING)
                if(loanIndex == -1)
                    allLoans.add(e);
                else {
                    LoanDataObject loan = allLoans.get(loanIndex);
                    loan.update(e);
                }
            else
               allLoans.remove(e);
        });

        loansToInvestTableController.refresh(); // update table view.
    }

    public CheckComboBox<String> getCategoryComboBox() {
        return this.filterCats;
    }

    public void addCategoryToList(String category_name) {
        filterCats.getItems().add(category_name);
        categoryChoiceBox.getItems().add(category_name);
    }

    // Update list of categories if received new added category.
    public void addCategoriesToList(Set<String> categories_name) {
        categories_name.forEach(e -> {
            if(!filterCats.getItems().contains(e)) {
                filterCats.getItems().add(e);
                categoryChoiceBox.getItems().add(e);
            }
        });
    }

    public int getCustomerBalance() {
        return this.currentCustomer.getBalance();
    }

    // Create new loan.
    public void makeNewLoanClicked(MouseEvent actionEvent) {

        if (loanID.getText().isEmpty() || capital.getText().isEmpty()
                || interestPerPayment.getText().isEmpty() || paymentsInterval.getText().isEmpty()
                || loanTotalTime.getText().isEmpty() || categoryChoiceBox.getValue().isEmpty())
        {
            popInfoAlert("Please fill all fields.");
            return;
        }

        try {
            LoanDataObject newLoan = new LoanDataObject(currentCustomer.getName(), loanID.getText(), categoryChoiceBox.getValue() , Integer.parseInt(capital.getText()),
                    Integer.parseInt(interestPerPayment.getText()), Integer.parseInt(paymentsInterval.getText()),
                    getTimeInYazAsInteger(), Integer.parseInt(loanTotalTime.getText()));

            if(newLoan.isValidLoan()) {
               Gson gson = new GsonBuilder().registerTypeAdapter(LoanDataObject.class, new LoanDataObject.LoanDataObjectAdapter()).create();
               String jsonLoan = gson.toJson(newLoan, LoanDataObject.class);

                Request request = new Request.Builder()
                        .url(HttpClientUtil.PATH + "/ClientMakeLoanServlet")
                        .post(new FormBody.Builder()
                                .add("loanData", jsonLoan).build())
                        .build();

               HttpClientUtil.runAsync(request, new okhttp3.Callback() {
                   @Override
                   public void onFailure(@NotNull Call call, @NotNull IOException e) {
                       System.out.println(e.getMessage());
                   }

                   @Override
                   public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                       String responseMsg = response.body().string();

                       // If success -> clear all fields.
                       if(response.code() == 200) {
                           Platform.runLater(() -> {
                               loanID.setText("");
                               capital.setText("");
                               interestPerPayment.setText("");
                               paymentsInterval.setText("");
                               loanTotalTime.setText("");
                               categoryChoiceBox.getSelectionModel().clearSelection();
                           });
                       }

                       // Pop up message.
                       Platform.runLater(() -> popInfoAlert(responseMsg));
                   }
               });
            } else {
                popInfoAlert("Payments arent dividing equally.");
            }

        } catch (Error e){
            System.out.println(e.getMessage());
        }

    }

    public void popInfoAlert(String message) {
        alertDialog.setAlertType(Alert.AlertType.INFORMATION);
        alertDialog.setContentText(message);
        alertDialog.showAndWait();
    }

}

