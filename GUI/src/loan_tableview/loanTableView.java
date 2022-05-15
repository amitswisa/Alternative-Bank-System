package loan_tableview;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class loanTableView implements Initializable {

    @FXML private TableView<LoanData> shoeTable;
    @FXML private TableColumn<LoanData, String> shoeImage, shoeName, shoeShop, releaseDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
