package main_screen;

import admin_screen.AdminController;
import engine.EngineManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class mainScreen implements Initializable {

    // Data members
    private final EngineManager engineManager;
    private String currentUser;

    // Pages
    @FXML private AnchorPane adminPageComponent;
    @FXML private AdminController adminPageComponentController;

    // FXML members
    @FXML private ChoiceBox<String> userTypeChoice;
    @FXML private Label pathLabel;

    public mainScreen() {
        engineManager = new EngineManager();
        this.currentUser = "Admin";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Transfer engine to included fxmls.
        adminPageComponentController.setEngineManager(this.engineManager);

        //Add Values to ChoiceBox.
        ObservableList<String> userChoiceList = FXCollections.observableArrayList();
        userChoiceList.add(this.currentUser);
        userTypeChoice.setItems(userChoiceList);
        userTypeChoice.setValue(this.currentUser);
    }
}
