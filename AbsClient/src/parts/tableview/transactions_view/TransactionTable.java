package parts.tableview.transactions_view;

import dto.objectdata.CustomerOperationData;
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


public class TransactionTable implements Initializable {

    @FXML private TableView<CustomerOperationData> transactionTable;
    @FXML private TableColumn<CustomerOperationData, String> operationName, currentOptAmount, balanceAfter;

    public TransactionTable() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        operationName.setCellValueFactory(new PropertyValueFactory<>("operationName"));
        currentOptAmount.setCellValueFactory(new PropertyValueFactory<>("currentOptAmount"));
        balanceAfter.setCellValueFactory(new PropertyValueFactory<>("balanceAfter"));
    }

    public void setTransactionList(ObservableList<CustomerOperationData> transactionList) {
        transactionTable.itemsProperty().set(transactionList);
    }
}
