package parts.payment_area;


import components.Customer.AppCustomer;
import pages.Customer_Screen.customerScreenController;
import dto.infodata.DataTransferObject;
import dto.objectdata.LoanDataObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentAreaController implements Initializable {

    private customerScreenController customerController;
    private LoanDataObject currentLoan = null;
    private TextInputDialog dialog;

    @FXML private Button payThisPayment, payAll;
    @FXML private Label loanStatus, paymentNumber , amountLeft , paymentAmount;

    public PaymentAreaController() {
        dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setGraphic(null);
        dialog.setHeaderText(null);
        dialog.setContentText("Enter amount to pay: ");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        payThisPayment.setOnAction((ActionEvent event) -> {

            if(currentLoan == null)
                return;

            int amountToPay = 0;
            if(currentLoan.getLoanStatus() == LoanDataObject.Status.RISK)
            {
                Optional<String> res = dialog.showAndWait();

                if(!res.isPresent())
                    return;

                try {
                    int num = Integer.parseInt(res.get());

                    if(num <= 0 || num > customerController.getCustomerBalance())
                        //TODO - servlet that return current yaz
                        throw new DataTransferObject("Unvalid amount of money.",0/* TODO- BankSystem.getCurrentYaz()*/);

                    amountToPay = num;
                } catch (NumberFormatException | DataTransferObject e) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    dialog.getEditor().setText("");
                    a.setContentText(e.getMessage());
                    a.showAndWait();
                }
            }

            if(currentLoan.getLoanStatus() == LoanDataObject.Status.RISK && amountToPay == 0)
                return;

            //customerController.handleCustomerLoansPayments(currentLoan,amountToPay);
            refreshRelevantData();
        });

        payAll.setOnAction((ActionEvent event) -> {
            List<LoanDataObject> temp = new ArrayList<>(1);
            temp.add(currentLoan);

            //try {
               // customerController.handleCustomerPayAllDebt(temp);
                refreshRelevantData();
            /*} catch (DataTransferObject e) {
                System.out.println(e.getMessage());
            }*/

        });

    }

    public void updateInfo(LoanDataObject dataObject){
        currentLoan=dataObject;
        loanStatus.setText(dataObject.getLoanStatus().toString());
        paymentNumber.setText(dataObject.getThisPaymentNumber() + "");
        amountLeft.setText(dataObject.getLastPayment(AppCustomer.getTimeInYazAsInteger()) + " ");
        paymentAmount.setText(dataObject.getAmountLeftToPayTofinished() + " ");
        activateBtnPayThisPayment(dataObject);
        activateBtnPayAll(dataObject);
    }

    public void setCustomerController(customerScreenController controller) {

        this.customerController = controller;
    }

    public void activateBtnPayThisPayment(LoanDataObject dataObject){
        //Status active
        if(dataObject.getLoanStatus() == LoanDataObject.Status.ACTIVE) {
            if (customerController.getCustomerBalance() >= dataObject.getThisPaymentAmount() &&
                    0/* TODO- BankSystem.getCurrentYaz()*/ == dataObject.getPaymentYaz())
                payThisPayment.setDisable(false);
            else
                payThisPayment.setDisable(true);
        }
        else {
            //Status risk
            if (dataObject.getLoanStatus() == LoanDataObject.Status.RISK &&
                    customerController.getCustomerBalance() > 0)
                payThisPayment.setDisable(false);
            else
                payThisPayment.setDisable(true);
        }
    }

    public void activateBtnPayAll(LoanDataObject dataObject){

        if(customerController.getCustomerBalance() >= dataObject.getAmountLeftToPayTofinished())
            payAll.setDisable(false);
        else
            payAll.setDisable(true);

    }

    public void refreshRelevantData() {
        customerController.refreshPaymentTable();
        currentLoan = null;
        payAll.setDisable(true);
        payThisPayment.setDisable(true);
        loanStatus.setText("");
        paymentNumber.setText("");
        amountLeft.setText("");
        paymentAmount.setText("");
    }
}
