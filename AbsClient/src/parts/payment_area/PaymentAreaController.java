package parts.payment_area;


import com.google.gson.Gson;
import components.Customer.AppCustomer;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import pages.Customer_Screen.customerScreenController;
import dto.infodata.DataTransferObject;
import dto.objectdata.LoanDataObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import server_con.HttpClientUtil;

import java.io.IOException;
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
                        throw new DataTransferObject("Unvalid amount of money.", AppCustomer.getTimeInYazAsInteger());

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

            this.handleCustomerLoansPayments(amountToPay);
            refreshRelevantData();
        });

        payAll.setOnAction((ActionEvent event) -> {

            if(currentLoan == null)
                return;

            if(currentLoan.getAmountLeftToPayTofinished() <= customerController.getCustomerBalance())
            {
                handleCustomerPayAllDebt();
                refreshRelevantData();
            }

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
                    AppCustomer.getTimeInYazAsInteger() == dataObject.getPaymentYaz())
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

    public void handleCustomerLoansPayments(int investAmount) {
        if(currentLoan == null)
            return;

        String jsonLoan = new Gson().toJson(currentLoan, LoanDataObject.class);

        Request request = new Request.Builder()
                .url(HttpClientUtil.PATH + "/payloan")
                .post(new FormBody.Builder()
                        .add("loan", jsonLoan)
                        .add("investAmount", String.valueOf(investAmount)).build())
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String msg = "";
                if(response.code() == 200) {
                    msg = "Loan paid!";
                } else {
                    msg = "Some error occurred.";
                }

                String finalMsg = msg;
                Platform.runLater(() -> {
                    refreshRelevantData();
                    customerController.popInfoAlert(finalMsg);
                });

            }
        });
    }

    public void handleCustomerPayAllDebt() {
        if(currentLoan == null)
            return;

        String jsonLoan = new Gson().toJson(currentLoan, LoanDataObject.class);

        Request request = new Request.Builder()
                .url(HttpClientUtil.PATH + "/payalldebt")
                .post(new FormBody.Builder()
                        .add("loan", jsonLoan).build())
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                Platform.runLater(() -> {
                    try {
                        customerController.popInfoAlert(response.body().string());
                        refreshRelevantData();
                    } catch (IOException e) {
                        e.printStackTrace();
                        customerController.popInfoAlert("Error occurred!");
                    }
                });

            }
        });
    }
}
