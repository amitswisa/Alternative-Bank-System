package tableview.payment_view;

import dto.objectdata.LoanDataObject;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PaymentTableView implements Initializable {

    private ObservableList<LoanDataObject> obsList;
    @FXML private TableView<LoanDataObject> paymentTable;
    @FXML private TableColumn<LoanDataObject, String> loanID, paymentYaz, capitalANDIntrest;
    @FXML private TableColumn<LoanDataObject, Void> choseBtn;

    public PaymentTableView() {
        obsList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loanID.setCellValueFactory(new PropertyValueFactory<>("loanID"));
        paymentYaz.setCellValueFactory(new PropertyValueFactory<>("paymentYaz"));
        capitalANDIntrest.setCellValueFactory(new PropertyValueFactory<>("capitalANDIntrest"));

        Callback<TableColumn<LoanDataObject, Void>, TableCell<LoanDataObject, Void>> cellFactory = param -> {
            return new TableCell<LoanDataObject, Void>() {

                private final RadioButton btn = new RadioButton();
                {
                    btn.setOnAction((ActionEvent event) -> {
                        // Get loan data when clicking specific View button.
                        LoanDataObject dataObject = getTableView().getItems().get(getIndex());

                        // TODO - updatePשymentTable -> dataObject
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
        };
        choseBtn.setCellFactory(cellFactory);
    }

    public void setPaymentList(List<LoanDataObject> list) {

        if(list == null || list.isEmpty())
            return;

        // Filter my loans list to active and risk only.
        list = list.stream().filter(e -> e.getLoanStatus() == LoanDataObject.Status.ACTIVE
                || e.getLoanStatus() == LoanDataObject.Status.RISK).collect(Collectors.toList());

        obsList.setAll(list);
        paymentTable.setItems(obsList);
    }
}
