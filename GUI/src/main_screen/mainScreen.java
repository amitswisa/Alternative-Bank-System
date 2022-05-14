package main_screen;

import engine.EngineManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainScreen implements Initializable {

    private EngineManager engineManager;
    private String currentUser;

    // FXML
    @FXML private AnchorPane addBody;
    @FXML private ChoiceBox<String> userTypeChoice;

    public mainScreen() {
        this.currentUser = "Admin";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Add Values to ChoiceBox.
        ObservableList<String> userChoiceList = FXCollections.observableArrayList();
        userChoiceList.add(this.currentUser);
        userTypeChoice.setItems(userChoiceList);

        userTypeChoice.setValue(this.currentUser);
    }

    public void setEngine(EngineManager engineManager) {
        this.engineManager = engineManager;
    }
}
