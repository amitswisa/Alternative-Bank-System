package admin_screen;

import dto.infodata.DataTransferObject;
import engine.EngineManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sun.util.resources.tr.CalendarData_tr;

import java.io.File;
import java.util.List;

public class AdminController {

    private EngineManager engineManager;

    // XML files.
    private Alert alertDiaglog;
    private FileChooser fileChooser;
    private File fileToLoad; // hold xml file to load.
    private List<String> customersNames;

    // @FXML
    @FXML Button loadXmlFileBtn;

    public AdminController() {
        fileChooser  = new FileChooser();
        alertDiaglog = new Alert(Alert.AlertType.INFORMATION);
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
    }

    // Load File button press event.
    public void loadXmlFile(MouseEvent mouseEvent) {
        File selectedFile = fileChooser.showOpenDialog((Stage)((Node) mouseEvent.getSource()).getScene().getWindow());
        if(selectedFile != null) {
            DataTransferObject response = this.engineManager.loadXML(selectedFile.getPath());

            // XML file load successfully process.
            if(response.isSuccess()) {
                this.fileToLoad = selectedFile;
                this.customersNames = this.engineManager.getAllCustomersNames();
            }

            // Pop an alert message.
            alertDiaglog.setContentText(response.getMessage());
            alertDiaglog.showAndWait();
        }
    }


}
