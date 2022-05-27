package tableview.payment_view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentTableView implements Initializable {

    //public TableColumn loanID;
    private ObservableList<String> obsList;
    @FXML private TableView<String> paymentTable;
    @FXML private TableColumn<String, Void> loanID, paymentYaz, capitalANDIntrest;

    public PaymentTableView() {
        obsList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loanID.setCellValueFactory(new PropertyValueFactory<>("loanID"));
        paymentYaz.setCellValueFactory(new PropertyValueFactory<>("paymentYaz"));
        capitalANDIntrest.setCellValueFactory(new PropertyValueFactory<>("capitalANDIntrest"));
    }

    public void setPaymentList(List<String> list) {
        obsList.clear();

        if(list == null || list.isEmpty())
            return;


        obsList.addAll(list);
        paymentTable.setItems(obsList);
    }
}
