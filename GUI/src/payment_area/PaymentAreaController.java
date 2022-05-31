package payment_area;

import abs.BankSystem;
import customer_screen.customerScreenController;
import dto.infodata.DataTransferObject;
import dto.objectdata.LoanDataObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentAreaController implements Initializable {

    private customerScreenController customerController;
    private LoanDataObject currentLoan = null;

    @FXML private Button payThisPayment, payAll;
    @FXML private Label loanStatus, paymentNumber , amountLeft , paymentAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        payThisPayment.setOnAction((ActionEvent event) -> {
            customerController.handleCustomerLoansPayments(currentLoan,0);
            refreshRelevantData();
        });

        payAll.setOnAction((ActionEvent event) -> {
            List<LoanDataObject> temp = new ArrayList<>(1);
            temp.add(currentLoan);

            try {
                customerController.handleCustomerPayAllDebt(temp);
                refreshRelevantData();
            } catch (DataTransferObject e) {
                System.out.println(e.getMessage());
            }

        });

    }

    public void updateInfo(LoanDataObject dataObject){
        currentLoan=dataObject;
        loanStatus.setText(dataObject.getLoanStatus().toString());
        paymentNumber.setText(dataObject.getThisPaymentNumber() + "/" + dataObject.getNumberOfPayment());
        amountLeft.setText(dataObject.getAmountLeftToPayTofinished() + " ");
        paymentAmount.setText(dataObject.getThisPaymentAmount() + " ");
        activateBtnPayThisPayment(dataObject);
        activateBtnPayAll(dataObject);
    }

    public void setCustomerController(customerScreenController controller) {

        this.customerController = controller;
    }

    public void activateBtnPayThisPayment(LoanDataObject dataObject){
        //Status active
        if(dataObject.getLoanStatus()== LoanDataObject.Status.ACTIVE &&
                customerController.getCurrentCustomerBalance() >= dataObject.getThisPaymentAmount() &&
                BankSystem.getCurrentYaz() == dataObject.getPaymentYaz())
        payThisPayment.setDisable(false);

        //Status risk
        if(dataObject.getLoanStatus()== LoanDataObject.Status.RISK &&
                customerController.getCurrentCustomerBalance() >= dataObject.getThisPaymentAmount())
            payThisPayment.setDisable(false);
    }

    public void activateBtnPayAll(LoanDataObject dataObject){

        if(customerController.getCurrentCustomerBalance() >= dataObject.getAmountLeftToPayTofinished())
            payAll.setDisable(false);

    }

    private void refreshRelevantData() {
        customerController.updateCustomerInfo();
        customerController.refreshPaymentTable();
        loanStatus.setText("");
        paymentNumber.setText("");
        amountLeft.setText("");
        paymentAmount.setText("");
    }
}
