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

    private ObservableList<String> obsList;
    @FXML private TableView<String> paymentTable;
    @FXML private TableColumn<String, Void> title, test, testv;

    public PaymentTableView() {
        obsList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        test.setCellValueFactory(new PropertyValueFactory<>("test"));
        testv.setCellValueFactory(new PropertyValueFactory<>("testv"));
    }

    public void setPaymentList(List<String> list) {
        obsList.clear();

        if(list == null || list.isEmpty())
            return;


        obsList.addAll(list);
        paymentTable.setItems(obsList);
    }
}
