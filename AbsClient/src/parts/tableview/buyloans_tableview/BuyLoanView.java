package parts.tableview.buyloans_tableview;

import components.Customer.AppCustomer;
import dto.objectdata.LoanDataObject;
import dto.objectdata.LoanSellerObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import server_con.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BuyLoanView implements Initializable {

    // this Data-Members
    private AppCustomer customerName;

    @FXML private TableView<LoanSellerObject> buyLoanTable;
    @FXML private TableColumn<LoanSellerObject, String> loanName, loanOwner, seller, share, interest, paymentLeft;

    public BuyLoanView() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Init table columns
        loanName.setCellValueFactory(new PropertyValueFactory<>("loanName"));
        loanOwner.setCellValueFactory(new PropertyValueFactory<>("loanOwner"));
        seller.setCellValueFactory(new PropertyValueFactory<>("seller"));
        share.setCellValueFactory(new PropertyValueFactory<>("share"));
        interest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        paymentLeft.setCellValueFactory(new PropertyValueFactory<>("paymentLeft"));

        TableColumn<LoanSellerObject, Void> buyLoanColumn = new TableColumn("Buy loan");
        Callback<TableColumn<LoanSellerObject, Void>, TableCell<LoanSellerObject, Void>> cellFactory = param -> {
            return new TableCell<LoanSellerObject, Void>() {

                private final Button btn = new Button("Buy loan");
                {
                    btn.setOnAction((ActionEvent event) -> {
                        // Get loan data when clicking specific View button.
                        LoanSellerObject dataObject = getTableView().getItems().get(getIndex());

                        Request buyShareRequest = new Request.Builder()
                                .url(HttpClientUtil.PATH + HttpClientUtil.BUY_LOAN)
                                .post(new FormBody.Builder()
                                        .add("loanOwner", dataObject.getLoanOwner())
                                        .add("sellerName", dataObject.getSellerName())
                                        .add("buyerName", customerName.getName())
                                        .add("loanName", dataObject.getLoanName()).build())
                                .build();

                        HttpClientUtil.runAsync(buyShareRequest, new okhttp3.Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                System.out.println(e.getMessage());
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String msg = response.body().string();
                                System.out.println(msg);

                                Platform.runLater(() -> {
                                    btn.setText(msg);
                                    System.out.println(msg);
                                });
                            }
                        });

                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        LoanSellerObject dataObject = getTableView().getItems().get(getIndex());

                        if(Integer.parseInt(dataObject.getShare()) > customerName.getBalance())
                            btn.setDisable(true);
                        else
                            btn.setDisable(false);

                        setGraphic(btn);
                    }
                }
            };
        };
        buyLoanColumn.setCellFactory(cellFactory);
        buyLoanTable.getColumns().add(buyLoanColumn);
    }


    public void refresh() {
        this.buyLoanTable.refresh();
    }

    public void setBuyLoansObservableList(ObservableList<LoanSellerObject> loanList, AppCustomer name) {
        // Init list of loans.
        this.customerName = name;
        buyLoanTable.setItems(loanList);
    }
}
