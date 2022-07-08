package tableview.transactions_view;

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

    private ObservableList<CustomerOperationData> list;

    @FXML private TableView<CustomerOperationData> transactionTable;
    @FXML private TableColumn<CustomerOperationData, String> operationName, currentOptAmount, balanceAfter;

    public TransactionTable() {
        this.list = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        operationName.setCellValueFactory(new PropertyValueFactory<>("operationName"));
        currentOptAmount.setCellValueFactory(new PropertyValueFactory<>("currentOptAmount"));
        balanceAfter.setCellValueFactory(new PropertyValueFactory<>("balanceAfter"));
    }

    public void setTransactionList(List<CustomerOperationData> transactionList) {
        list.clear();

        if(transactionList == null || transactionList.isEmpty())
            return;


        list.addAll(transactionList);
        transactionTable.setItems(list);
    }
}
