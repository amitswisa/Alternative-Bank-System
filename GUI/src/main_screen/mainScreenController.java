package main_screen;

import Utils.User;
import admin_screen.AdminController;
import customer_screen.customerScreenController;
import engine.EngineManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
    private User currentUser;

    // Pages
    @FXML private AnchorPane adminPageComponent;
    @FXML private AdminController adminPageComponentController;
    @FXML private AnchorPane customerPageComponent;
    @FXML private customerScreenController customerPageComponentController;

    // FXML members
    @FXML private ChoiceBox<String> userTypeChoice;
    @FXML private Label pathLabel;
    @FXML private Label yazLabel;

    public mainScreenController() {
        engineManager = new EngineManager();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        customerPageComponent.setVisible(false);
        customerPageComponentController.setEngineManager(this.engineManager);
        currentUser = customerPageComponentController.getCurrentUser();

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
        return this.currentUser.getUsername();
    }

    public void setYazLabelText(String yazString) {
        this.yazLabel.setText(yazString);
    }

    public void changeUserType(ActionEvent actionEvent) {
        String newUser = userTypeChoice.getValue();

        if(newUser == null)
            return;

        if(newUser.equals(this.currentUser.getDefaultUserName())) {
            this.customerPageComponent.setVisible(false);
            this.adminPageComponent.setVisible(true);
            this.currentUser.setUsername(this.currentUser.getDefaultUserName());
        } else {
            this.customerPageComponent.setVisible(true);
            this.adminPageComponent.setVisible(false);
            this.currentUser.setUsername(newUser);
        }
    }
}
