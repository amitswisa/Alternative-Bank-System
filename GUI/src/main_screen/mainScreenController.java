package main_screen;

import admin_screen.AdminController;
import engine.EngineManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class mainScreenController implements Initializable {

    // Data members
    private final EngineManager engineManager;
    private String currentUser;

    // Pages
    @FXML private AnchorPane adminPageComponent;
    @FXML private AdminController adminPageComponentController;

    // FXML members
    @FXML private ChoiceBox<String> userTypeChoice;
    @FXML private Label pathLabel;
    @FXML private Label yazLabel;

    public mainScreenController() {
        engineManager = new EngineManager();
        this.currentUser = "Admin";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Transfer engine to included fxmls.
        adminPageComponentController.setInitData(this.engineManager, this);

        // Bind data-members
        this.pathLabel.textProperty().bind(adminPageComponentController.getPathTextProperty());

        //Set init values for choice-box (only admin).
        adminPageComponentController.setCustomersNamesChoiceBox();
    }

    public ChoiceBox<String> getUserTypeChoice() {
        return userTypeChoice;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setYazLabelText(String yazString) {
        this.yazLabel.setText(yazString);
    }
}
