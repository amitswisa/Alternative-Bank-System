package main_screen;

import Utils.User;
import dto.objectdata.CustomerAlertData;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.net.URL;
import java.util.*;

public class mainScreenController implements Initializable {

    // Data members
    private User currentUser;
    private TranslateTransition translateTransition;
    private ChoiceDialog<String> dialog;

    // Pages
    @FXML private AnchorPane mainPane;
    @FXML private AnchorPane dataDiv;

    // FXML members
    @FXML private ImageView settingsBtn;
    @FXML private Label yazLabel;
    @FXML private Label alertMessageCounter;
    @FXML private ImageView alertBtn;
    @FXML private AnchorPane alertPane;
    @FXML private ListView<CustomerAlertData> alertsViewList;
    @FXML private HBox alertBox;
    private ObservableList<CustomerAlertData> alertObservableList;

    public mainScreenController() {
        alertObservableList = FXCollections.observableArrayList();

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

        translateTransition = new TranslateTransition(Duration.millis(200), alertBox);
        final double start = 0.0;
        final double end = start - 4.0;
        translateTransition.setFromY(start);
        translateTransition.setToY(end);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(Animation.INDEFINITE);
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);

        // Alerts list view init to invisible.
        alertPane.setVisible(false);
        alertsViewList.setPlaceholder(new Label("No content found"));

        // Handling clicking on alert icon open listview event.
        alertBtn.onMouseClickedProperty().set(e -> handleAlertBoxClick());
        alertMessageCounter.onMouseClickedProperty().set(e -> handleAlertBoxClick());

        // Set alert listview cell factory.
        alertsViewList.setCellFactory(new Callback<ListView<CustomerAlertData>, ListCell<CustomerAlertData>>()
        {
            @Override
            public ListCell<CustomerAlertData> call(ListView<CustomerAlertData> listView)
            {
                return new ListViewCell();
            }
        });

    }

    public void setYazLabelText(String yazString) {
        this.yazLabel.setText(yazString);
    }

    private void getCustomerAlerts() {
        // Alerts handling.
        //alertObservableList.setAll(customerPageComponentController.getCustomerAlertList());
        FXCollections.reverse(alertObservableList); // sort from end to beginning.
        alertsViewList.setItems(alertObservableList);

        if(countUnReadMsg() > 0)
            translateTransition.play();
        else
            translateTransition.stop();
    }

    private void handleAlertBoxClick() {

        /*if(userTypeChoice.getValue().equals("Admin")) {
            alertPane.setVisible(false);
            return;
        }*/

        // Refresh changes.
        alertPane.setVisible(!alertPane.isVisible());

        if(!alertPane.isVisible())
            alertsViewList.refresh();
        else {
            this.alertMessageCounter.setText("0");
            translateTransition.stop();
            //customerPageComponentController.getCustomerAlertList().forEach(CustomerAlertData::markAsRead);
        }
    }

    private int countUnReadMsg() {
        // Count number of unread notifications.
        /*int count = (int) customerPageComponentController.getCustomerAlertList().stream().filter(e -> !e.isAlertGotRead()).count();
        this.alertMessageCounter.setText(count + "");
        return count;*/
        return 0;
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
                alertBtn.setImage(new Image("/resources/images/alertWhite.png"));
                settingsBtn.setImage(new Image("/resources/images/settingsWhite.png"));
            } else {
                mainPane.getStylesheets().remove(Objects.requireNonNull(getClass().getResource("mainScreenDark.css")).toExternalForm());
                alertBtn.setImage(new Image("/resources/images/alert.png"));
                settingsBtn.setImage(new Image("/resources/images/settings.png"));
            }

        }

    }
}
