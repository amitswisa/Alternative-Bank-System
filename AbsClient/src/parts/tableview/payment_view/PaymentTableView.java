package parts.tableview.payment_view;

import pages.Customer_Screen.customerScreenController;
import dto.objectdata.LoanDataObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static components.Customer.AppCustomer.getTimeInYazAsInteger;

public class PaymentTableView implements Initializable {

    private RadioButton currentBtn = null;
    private customerScreenController customerController;

    @FXML private TableView<LoanDataObject> paymentTable;
    @FXML private TableColumn<LoanDataObject, String> loanID, paymentYaz, lastPayment;
    @FXML private TableColumn<LoanDataObject, Void> choseBtn;

    public PaymentTableView() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loanID.setCellValueFactory(new PropertyValueFactory<>("loanID"));
        paymentYaz.setCellValueFactory(new PropertyValueFactory<>("paymentYaz"));
        lastPayment.setCellValueFactory((TableColumn.CellDataFeatures<LoanDataObject, String> d) -> new SimpleStringProperty(d.getValue().getLastPayment(getTimeInYazAsInteger())+""));

        Callback<TableColumn<LoanDataObject, Void>, TableCell<LoanDataObject, Void>> cellFactory = param -> {
            return new TableCell<LoanDataObject, Void>() {

                private final RadioButton btn = new RadioButton();
                {
                    btn.setOnAction((ActionEvent event) -> {
                        // Get loan data when clicking specific View button.
                        LoanDataObject data = getTableView().getItems().get(getIndex());

                        if(btn.isSelected()) {
                            customerController.setPaymentArea(data);

                            if(currentBtn != null && currentBtn != btn)
                                currentBtn.setSelected(false);

                            currentBtn = btn;
                        } else {
                            if(currentBtn == btn)
                                btn.setSelected(true);
                        }

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

    public void setPaymentList(ObservableList<LoanDataObject> list) {

        if(list == null)
            return;

        paymentTable.setItems(list.filtered(new Predicate<LoanDataObject>() {
            @Override
            public boolean test(LoanDataObject loanDataObject) {
                return loanDataObject.getLoanStatus() == LoanDataObject.Status.ACTIVE ||
                        loanDataObject.getLoanStatus() == LoanDataObject.Status.RISK;
            }
        }));
    }

    public void setCustomerController(customerScreenController controller) {
        this.customerController = controller;
    }

    public void resetChoiceBtn() {
        if(currentBtn != null) {
            refreshTable();
            currentBtn.setSelected(false);
            currentBtn = null;
            customerController.refreshPaymentView();
        }
    }

    public void refreshTable() {
        paymentTable.refresh();
    }

}
