package pages.Main_Screen;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import org.controlsfx.control.CheckComboBox;
import pages.Customer_Screen.customerScreenController;
import dto.objectdata.CustomerAlertData;
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
import server_con.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

public class mainScreenController implements Initializable {

    // Data members
    private CustomerDataObject currentCustomer;
    private TranslateTransition translateTransition;
    private ChoiceDialog<String> dialog;
    private FileChooser fileChooser;
    private ObservableList<CustomerAlertData> alertObservableList;
    private Alert informationPopup;
    private Gson gson;

    // Pages
    @FXML private AnchorPane mainPane;
    @FXML private AnchorPane dataDiv;
    @FXML private AnchorPane customerPageComponent;
    @FXML private customerScreenController customerPageComponentController;

    // FXML members
    @FXML private ImageView settingsBtn;
    @FXML private Label yazLabel;
    @FXML private Label alertMessageCounter;
    @FXML private ImageView alertBtn;
    @FXML private AnchorPane alertPane;
    @FXML private ListView<CustomerAlertData> alertsViewList;
    @FXML private HBox alertBox;
    @FXML private Button uploadXMLBtn;

    public mainScreenController() {
        fileChooser = new FileChooser();
        gson = new Gson();
        informationPopup = new Alert(Alert.AlertType.INFORMATION);
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

        /*// Bind data-members
        this.pathLabel.textProperty().bind(adminPageComponentController.getPathTextProperty());*/

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

    // Reset settings and setup customer changing event.
    //TODO- get customer name from login page
    public void loadUserData(ActionEvent actionEvent) {
        customerPageComponentController.resetSettings();
        this.getCustomerAlerts();
        this.alertBox.setVisible(false);
    }

    private void getCustomerAlerts() {
        // Alerts handling.
        alertObservableList.setAll(customerPageComponentController.getCustomerAlertList());
        FXCollections.reverse(alertObservableList); // sort from end to beginning.
        alertsViewList.setItems(alertObservableList);

        if(countUnReadMsg() > 0)
            translateTransition.play();
        else
            translateTransition.stop();
    }

    private void handleAlertBoxClick() {

        // Refresh changes.
        alertPane.setVisible(!alertPane.isVisible());

        if(!alertPane.isVisible())
            alertsViewList.refresh();
        else {
            this.alertMessageCounter.setText("0");
            translateTransition.stop();
            customerPageComponentController.getCustomerAlertList().forEach(CustomerAlertData::markAsRead);
        }
    }

    private int countUnReadMsg() {
        // Count number of unread notifications.
        int count = (int) customerPageComponentController.getCustomerAlertList().stream().filter(e -> !e.isAlertGotRead()).count();
        this.alertMessageCounter.setText(count + "");
        return count;
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

    // Updates current customer project when customer logged in - called from login controller.
    public void setUser(CustomerDataObject newCustomer) {
        this.currentCustomer = newCustomer;
        this.customerPageComponentController.setCusomter(this.currentCustomer);
    }

    public void loadXmlFile(MouseEvent mouseEvent) throws IOException {
        File selectedFile = fileChooser.showOpenDialog((Stage)((Node) mouseEvent.getSource()).getScene().getWindow());
        if(selectedFile != null) {

            // Creating request body part.
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("fileContent", selectedFile.getName(), RequestBody.create(MediaType.parse("text/plain"), selectedFile))
                    .addFormDataPart("customerName", this.currentCustomer.getName())
                    .build();

            // Create request and add its body from "formBody"
            Request upload_xml_request = new Request.Builder()
                    .url(HttpClientUtil.PATH + "/UploadCustomerData")
                            .post(formBody).build();

            String responseMessage = "";
            try {
                Response response = HttpClientUtil.sendSyncRequest(upload_xml_request);
                String msg = response.body().string();

                // If response succeed (Status between 200-299).
                if(response.isSuccessful()) {
                    responseMessage = "File loaded successfully!";

                    Type listType = new TypeToken<ArrayList<LoanDataObject>>(){}.getType(); // Get returned type in run-time.
                    List<LoanDataObject> newLoansUploadedList = new Gson().fromJson(msg, listType); // Parse to List of LoanDataObject.
                    insertLoansToLists(newLoansUploadedList);

                } else
                    responseMessage = msg;

            }catch(IOException e) {
                responseMessage = e.getMessage();
            } finally {
                System.out.println(responseMessage);
                informationPopup.setContentText(responseMessage);
                informationPopup.showAndWait(); // Pop an alert message.
            }

        }
    }

    private void insertLoansToLists(List<LoanDataObject> newLoansUploadedList) {

        for(LoanDataObject loan : newLoansUploadedList) {

            // Add category if its not there.
            CheckComboBox<String> catList = customerPageComponentController.getCategoryComboBox();
            if(!catList.getItems().contains(loan.getLoanCategory()))
                customerPageComponentController.addCategoryToList(loan.getLoanCategory());
        }

        // Add loan to customer and then refresh his lists.
        this.currentCustomer.addLoans(newLoansUploadedList);
    }
}
