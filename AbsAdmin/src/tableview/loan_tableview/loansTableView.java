package tableview.loan_tableview;

import dto.objectdata.LoanDataObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import popups.loan_information.LoanInfoController;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class loansTableView implements Initializable {

    //POPUP Data
    private FXMLLoader loader;
    private LoanInfoController controller;
    private Scene firstScene = null;

    // this Data-Members
    private ObservableList<LoanDataObject> obsList;
    private FilteredList<LoanDataObject> list = null;
    private Stage popUpStage;

    @FXML private TableView<LoanDataObject> loansTable;
    @FXML private TableColumn<LoanDataObject, String> loanID, owner, loanCategory, loanAmount, loanInterestPerPayment, loanTotalTime, amountLeftToPay;

    public loansTableView() throws IOException {
        // Create popup window.
        popUpStage = new Stage();
        popUpStage.initModality(Modality.APPLICATION_MODAL);
        popUpStage.setTitle("Loan details");

        loader = new FXMLLoader(getClass().getResource("/popups/loan_information/loanInfo.fxml"));
        firstScene = new Scene(loader.load());
        controller = loader.getController();
        popUpStage.setScene(firstScene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Init table columns
        loanID.setCellValueFactory(new PropertyValueFactory<>("loanID"));
        owner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        loanCategory.setCellValueFactory(new PropertyValueFactory<>("loanCategory"));
        loanAmount.setCellValueFactory(new PropertyValueFactory<>("loanAmount"));
        amountLeftToPay.setCellValueFactory(new PropertyValueFactory<>("amountLeftToPay"));
        loanInterestPerPayment.setCellValueFactory(new PropertyValueFactory<>("loanInterestPerPayment"));
        loanTotalTime.setCellValueFactory(new PropertyValueFactory<>("loanTotalTime"));

        TableColumn<LoanDataObject, Void> colBtn = new TableColumn("View Loan");
        Callback<TableColumn<LoanDataObject, Void>, TableCell<LoanDataObject, Void>> cellFactory = param -> {
            return new TableCell<LoanDataObject, Void>() {

                private final Button btn = new Button("View");
                {
                    btn.setOnAction((ActionEvent event) -> {
                        // Get loan data when clicking specific View button.
                        LoanDataObject dataObject = getTableView().getItems().get(getIndex());

                        // Create popup window with above details.
                        controller.setLoanPopupInfo(dataObject);
                        popUpStage.showAndWait();
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
        colBtn.setCellFactory(cellFactory);
        loansTable.getColumns().add(colBtn);
    }

    public void setLoansObservableList(ObservableList<LoanDataObject> loansList) {

        if(loansList == null)
            return;

        list = new FilteredList<>(loansList);
        loansTable.setItems(list);
        this.loansTable.refresh();
    }

    public void resetCheckboxColumn() {
        this.loansTable.refresh();
    }

}
