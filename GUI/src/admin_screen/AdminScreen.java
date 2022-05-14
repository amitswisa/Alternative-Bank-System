package admin_screen;

import engine.EngineManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;

public class AdminScreen {

    private EngineManager engineManager;

    // XML files.
    private FileChooser fileChooser;
    private File fileToLoad; // hold xml file to load.
    //private Stage currentStage;

    // @FXML
    @FXML Button loadXmlFileBtn;

    public AdminScreen() {
        //this.engineManager = engineManager;
        fileChooser  = new FileChooser();
       // this.currentStage = null;
    }

    // Load File button press event.
    public void loadXmlFile(MouseEvent mouseEvent) {
//        if(this.currentStage == null)
//            this.currentStage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
//
//        File selectedFile = fileChooser.showOpenDialog(this.currentStage);
    }


}
