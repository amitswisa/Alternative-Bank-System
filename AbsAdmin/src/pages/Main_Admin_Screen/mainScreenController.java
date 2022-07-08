package pages.Main_Admin_Screen;

import Utils.User;
import dto.objectdata.CustomerAlertData;
import dto.objectdata.CustomerDataObject;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;
import listview.ListViewCell;
import pages.Admin_Screen.AdminController;

import java.net.URL;
import java.util.*;

public class mainScreenController implements Initializable {

    // Data members
    private User currentAdmin;
    private TranslateTransition translateTransition;
    private ChoiceDialog<String> dialog;

    // Pages
    @FXML private AnchorPane mainPane;
    @FXML private AnchorPane dataDiv;
    @FXML private AnchorPane adminPageComponent;
    @FXML private AdminController adminPageComponentController;


    // FXML members
    @FXML private ChoiceBox<String> userTypeChoice;
    @FXML private ImageView settingsBtn;
    @FXML private Button increaseYAZBtn;
    @FXML private Button rewindBtn;
    @FXML private Label yazLabel;


    public mainScreenController() {

        // Dialog to settings
        List<String> choices = new ArrayList<String>();
        choices.add("Light theme");
        choices.add("Dark theme");
        dialog = new ChoiceDialog<>("Light theme", choices);
        dialog.setTitle("Settings");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setContentText("Theme: ");
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Save");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Transfer engine to included fxmls.
       // adminPageComponentController.setInitData(this);

    }

    public ChoiceBox<String> getUserTypeChoice() {
        return userTypeChoice;
    }

    // Return current chosen customer name.
    public String getCurrentAdmin() {
        return this.currentAdmin.getUsername();
    }

    public void setYazLabelText(String yazString) {
        this.yazLabel.setText(yazString);
    }


    public void settingsFunctionallity(MouseEvent mouseEvent) {

        String formResult = dialog.getResult();
        Optional<String> result = dialog.showAndWait();

        // If clicked ok.
        if (result.isPresent()){

            if(result.get().equals(formResult))
                return;

            if(result.get().equals("Dark theme")) {
                mainPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("mainScreenDark.css")).toExternalForm());
                settingsBtn.setImage(new Image("/resources/images/settingsWhite.png"));
            } else {
                mainPane.getStylesheets().remove(Objects.requireNonNull(getClass().getResource("mainScreenDark.css")).toExternalForm());
                settingsBtn.setImage(new Image("/resources/images/settings.png"));
            }

        }

    }

    // Called when "increase yaz" btn clicked.
    public void increaseYaz(MouseEvent mouseEvent) {
        //TODO - create servlet that increase yaz
        //engineManager.increaseYazDate();
        setYazLabelText(/*BankSystem.getCurrentYaz()*/ 0 +"");
    }

    public void setUser(User currentAdmin) {
        this.currentAdmin = currentAdmin;
    }
}
