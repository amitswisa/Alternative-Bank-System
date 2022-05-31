package payment_area;

import abs.BankSystem;
import customer_screen.customerScreenController;
import dto.objectdata.LoanDataObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class PaymentAreaController {

    private customerScreenController customerController;

    @FXML private Button payThisPayment, payAll;
    @FXML private Label loanStatus, paymentNumber , amountLeft , paymentAmount;

    /*@Override
    public void initialize(URL location, ResourceBundle resources) {

        payThisPayment.setOnAction((ActionEvent event) -> {


                    });

        payAll.setOnAction((ActionEvent event) -> {


        });

    }*/

    public void updateInfo(LoanDataObject dataObject){
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
//להוסיף סינון של יז מתאים
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

}
