package tableview.customer_tableview;

import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
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

public class customerTableView implements Initializable {

    // this Data-Members
    private ObservableList<CustomerDataObject> list = null;

    @FXML private TableView<CustomerDataObject> customerTable;
    @FXML private TableColumn<LoanDataObject, String> name, balance , takenLoansNumber , investedLoansNumber;

    public customerTableView() {
        list = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        takenLoansNumber.setCellValueFactory(new PropertyValueFactory<>("takenLoansNumber"));
        investedLoansNumber.setCellValueFactory(new PropertyValueFactory<>("investedLoansNumber"));
    }

    public void setCustomerList(List<CustomerDataObject> list) {

        if(list == null || list.isEmpty())
            return;

        this.list.setAll(list);
        this.customerTable.setItems(this.list);
    }
}
