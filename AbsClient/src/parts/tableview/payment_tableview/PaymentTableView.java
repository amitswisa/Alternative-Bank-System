package parts.tableview.payment_tableview;

import components.Customer.AppCustomer;
import dto.objectdata.LoanDataObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class PaymentTableView implements Initializable {

    // this Data-Members
    private ObservableList<LoanDataObject> list = null;

    @FXML private TableView<LoanDataObject> paymentTable;
    @FXML private TableColumn<LoanDataObject, String> loanID, paymentYaz, lastPayment;

    public PaymentTableView() {
        list = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Init table columns
        loanID.setCellValueFactory(new PropertyValueFactory<>("loanID"));
        paymentYaz.setCellValueFactory(new PropertyValueFactory<>("paymentYaz"));
        lastPayment.setCellValueFactory((TableColumn.CellDataFeatures<LoanDataObject, String> d) -> new SimpleStringProperty(d.getValue().getLastPayment(AppCustomer.getTimeInYazAsInteger())+""));
    }

    public void setpaymentItems(List<LoanDataObject> list) {

        if(list == null || list.isEmpty())
            return;

        this.list.setAll(list);
        this.paymentTable.setItems(this.list);
        this.paymentTable.refresh();
    }

}
