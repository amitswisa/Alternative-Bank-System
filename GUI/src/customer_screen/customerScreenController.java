package customer_screen;

import Utils.User;
import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerDataObject;
import engine.EngineManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.StageStyle;
import loan_tableview.loansTableView;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class customerScreenController implements Initializable {

    // DATA MEMBERS
    private EngineManager engineManager;
    private CustomerDataObject currentCustomer;
    private TextInputDialog moneyPopup;
    private Alert alertDialog;

    // FXML MEMBERS
    @FXML private User currentUser;
    @FXML private loansTableView myLoansTableController;
    @FXML private loansTableView myInvestmentsLoansController;
    @FXML private Label currentBalance;
    @FXML private Button depositBtn;
    @FXML private Button withdrawalBtn;

    public customerScreenController() {
        currentUser = new User();

        //Text dialog settings
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
            }
        });

        // Deposit & Withdrawal button functionality
        depositBtn.setOnMouseClicked(event -> {
            moneyPopup.setTitle("Deposit");
            Optional<String> result = moneyPopup.showAndWait();
            String value = String.valueOf(moneyPopup.getEditor().getText());
            moneyPopup.getEditor().setText(""); // empty input text.

            // Try parsing input to int and if success update user bank.
            try {
                int valueOfInput = Integer.parseInt(value);
                this.engineManager.depositeMoney(this.currentUser.getUsername(), valueOfInput);
                currentBalance.setText("Current balance: " + this.engineManager.getBalanceOfCustomerByName(this.currentUser.getUsername()));
            } catch(NumberFormatException nfe) {

                // If ok button was pressed!
                if(result.isPresent()) {
                    alertDialog.setContentText("Please enter a number.");
                    alertDialog.showAndWait();
                }
            }
        });

        // Deposit & Withdrawal button functionality
        withdrawalBtn.setOnMouseClicked(event -> {
            moneyPopup.setTitle("Withdrawal");
            Optional<String> result = moneyPopup.showAndWait();
            String value = String.valueOf(moneyPopup.getEditor().getText());
            moneyPopup.getEditor().setText(""); // empty input text.

            // Try parsing input to int and if success update user bank.
            try {
                int valueOfInput = Integer.parseInt(value);
                this.engineManager.withdrawMoney(this.currentUser.getUsername(), valueOfInput);
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
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    private void updateCustomerInfo(String newName) {

        // update current user Customer information DTO when selected user changes.
        this.currentCustomer = this.engineManager.getCustomerByName(newName);

        currentBalance.setText("Current balance: " + this.engineManager.getBalanceOfCustomerByName(newName));

        myLoansTableController.setLoanItems(this.currentCustomer.getLoanList());
        myInvestmentsLoansController.setLoanItems(this.currentCustomer.getInvestmentList());

    }
}
