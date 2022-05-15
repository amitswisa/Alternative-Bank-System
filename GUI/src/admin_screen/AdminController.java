package admin_screen;

import dto.infodata.DataTransferObject;
import engine.EngineManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main_screen.mainScreenController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdminController {

    private EngineManager engineManager;
    private mainScreenController mainPage;
    private FileChooser fileChooser;
    private File fileToLoad; // hold xml file to load.
    private Alert alertDiaglog;
    private List<String> customersNames;

    private SimpleStringProperty xmlFilePathTextProperty;

    // @FXML
    @FXML Button loadXmlFileBtn;

    public AdminController() {
        fileChooser  = new FileChooser();
        customersNames = new ArrayList<String>();
        alertDiaglog = new Alert(Alert.AlertType.INFORMATION);
        fileToLoad = null;
        xmlFilePathTextProperty = new SimpleStringProperty("No xml file was loaded!");
    }

    public void setInitData(EngineManager engineManager, mainScreenController main) {
        this.engineManager = engineManager;
        this.mainPage = main;
    }

    // Load File button press event.
    public void loadXmlFile(MouseEvent mouseEvent) {
        File selectedFile = fileChooser.showOpenDialog((Stage)((Node) mouseEvent.getSource()).getScene().getWindow());
        if(selectedFile != null) {
            DataTransferObject response = this.engineManager.loadXML(selectedFile.getPath());

            // XML file load successfully process.
            if(response.isSuccess()) {
                this.fileToLoad = selectedFile;
                xmlFilePathTextProperty.set(selectedFile.getAbsolutePath()); // Change pathText
                this.setCustomersNamesChoiceBox();
            }

            // Pop an alert message.
            alertDiaglog.setContentText(response.getMessage());
            alertDiaglog.showAndWait();
        }
    }

    // Get customers list and push it into choice box at the header.
    public void setCustomersNamesChoiceBox() {

        ChoiceBox<String> cb = this.mainPage.getUserTypeChoice();

        cb.getItems().clear();
        cb.setValue(mainPage.getCurrentUser());

        customersNames.clear();
        customersNames.add(mainPage.getCurrentUser());

        if(fileToLoad != null)
            customersNames.addAll(this.engineManager.getAllCustomersNames());

        cb.setItems(FXCollections.observableArrayList(customersNames));

    }

    public SimpleStringProperty getPathTextProperty() {
        return this.xmlFilePathTextProperty;
    }
}
