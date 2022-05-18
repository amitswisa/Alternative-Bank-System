package loan_tableview;

import dto.objectdata.LoanDataObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jdk.nashorn.internal.runtime.regexp.joni.ast.AnchorNode;
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
    private ObservableList<LoanDataObject> list = null;
    private Stage popUpStage;

    @FXML private TableView<LoanDataObject> loansTable;
    @FXML private TableColumn<LoanDataObject, String> id, owner, category, capital, interest, totalTime;

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

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        owner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        capital.setCellValueFactory(new PropertyValueFactory<>("capital"));
        interest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        totalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        list = FXCollections.observableArrayList();

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

    public void setLoanItems(List<LoanDataObject> loansList) {

        list.clear();

        if(loansList == null || loansList.isEmpty())
            return;

        list.addAll(loansList);
        loansTable.setItems(list);
    }
}
