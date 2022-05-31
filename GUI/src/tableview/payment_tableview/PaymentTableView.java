package tableview.payment_tableview;

import abs.BankSystem;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import popups.loan_information.LoanInfoController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;



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
        lastPayment.setCellValueFactory((TableColumn.CellDataFeatures<LoanDataObject, String> d) -> new SimpleStringProperty(d.getValue().getLastPayment(BankSystem.getCurrentYaz())+""));
    }

    public void setpaymentItems(List<LoanDataObject> list) {

        if(list == null || list.isEmpty())
            return;

        this.list.setAll(list);
        this.paymentTable.setItems(this.list);
        this.paymentTable.refresh();
    }

}
