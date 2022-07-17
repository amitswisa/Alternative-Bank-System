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
    @FXML private TableView<CustomerDataObject> customerTable;
    @FXML private TableColumn<LoanDataObject, String> name, balance ;

    public customerTableView() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }

    public void setCustomerList(ObservableList<CustomerDataObject> list) {

        if(list == null)
            return;

        this.customerTable.setItems(list);
    }
}
