package payment_area;

import dto.objectdata.LoanDataObject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PaymentAreaController {

    @FXML private Button payThisPayment, payAll;
    @FXML private Label loanStatus, paymentNumber , amountLeft , paymentAmount;

    public void updateInfo(LoanDataObject dataObject){
        loanStatus.setText(dataObject.getLoanStatus().toString());
        paymentNumber.setText(dataObject.getThisPaymentNumber() + "/" + dataObject.getNumberOfPayment());
        amountLeft.setText(dataObject.getAmountLeftToPayTofinished() + " ");
        paymentAmount.setText(dataObject.getThisPaymentAmount() + " ");
        payThisPayment.setDisable(false);
        payAll.setDisable(false);
    }

}
