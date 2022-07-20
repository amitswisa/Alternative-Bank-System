package pages.Main_Screen;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import components.Customer.AppCustomer;
import components.Customer.CustomerRefresher;
import dto.objectdata.LoanDataObject;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import org.controlsfx.control.CheckComboBox;
import pages.Customer_Screen.customerScreenController;
import dto.objectdata.CustomerAlertData;
import javafx.animation.*;
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
    private AppCustomer currentCustomer;
    private Timer timer;
    private CustomerRefresher customerRefresher;

    private TranslateTransition translateTransition;
    private final ChoiceDialog<String> dialog;
    private final FileChooser fileChooser;
    private final Alert informationPopup;

    // Pages
    @FXML private AnchorPane mainPane;
    @FXML private customerScreenController customerPageComponentController;

    // FXML members
    @FXML private ImageView settingsBtn;
    @FXML private Label yazLabel;
    @FXML private Label alertMessageCounter;
    @FXML private ImageView alertBtn;
    @FXML private AnchorPane alertPane;
    @FXML private ListView<CustomerAlertData> alertsViewList;
    @FXML private HBox alertBox;
    public Label customerNameLabel;

    public mainScreenController() {

        // Object data members init.
        fileChooser = new FileChooser();
        informationPopup = new Alert(Alert.AlertType.INFORMATION);

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

        // Handling clicking on alert icon open listview event.
        alertBtn.onMouseClickedProperty().set(e -> handleAlertBoxClick());
        alertMessageCounter.onMouseClickedProperty().set(e -> handleAlertBoxClick());

        // Set alert listview cell factory.
        alertsViewList.setPlaceholder(new Label("No content found"));
        alertsViewList.setCellFactory(new Callback<ListView<CustomerAlertData>, ListCell<CustomerAlertData>>()
        {
            @Override
            public ListCell<CustomerAlertData> call(ListView<CustomerAlertData> listView)
            {
                return new ListViewCell();
            }
        });
    }

    // Start running TimerTask AppCustomer run method every 400 ms.
    private void startCustomerDataUpdate() {
        customerRefresher = new CustomerRefresher(currentCustomer::updateUser
                ,currentCustomer::setTimeInYaz
                ,customerPageComponentController::updateAllLoansList
                ,customerPageComponentController::addCategoriesToList
                ,currentCustomer.getName()
                ,this::countUnReadMsg);
        timer = new Timer();
        timer.schedule(customerRefresher, 400, 400);
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

    private void countUnReadMsg(Integer object) {
        int count = (int) this.currentCustomer.getListOfAlerts().stream().filter(e -> !e.isAlertGotRead()).count();
        this.alertMessageCounter.setText(count + "");
    }

    public void loadAndChaneSettings(MouseEvent mouseEvent) {

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
    public void setCustomer(AppCustomer newCustomer) {
        // Handle instance of logged customer.
        this.currentCustomer = newCustomer;

        // Initialize properties
        customerNameLabel.setText("Hello " + newCustomer.getName());
        yazLabel.setText(this.currentCustomer.getTimeInYaz().get() + "");

        // Listeners and bindings.
        this.currentCustomer.getTimeInYaz().addListener((observable, oldValue, newValue) -> {
            yazLabel.setText(newValue.intValue() + "");
            customerPageComponentController.refreshPaymentView();
        });

        alertsViewList.setItems(this.currentCustomer.getListOfAlerts());

        // Pass instance of customer to customer screen.
        this.customerPageComponentController.setCustomer(this.currentCustomer);

        // Make customer updates run async.
        startCustomerDataUpdate();
    }

    // Process of loading xml file to system.
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
        this.currentCustomer.addToMyLoansList(newLoansUploadedList);
    }

    // Shutdown process when program close.
    public void shutdown() {
        // When closing app stop refresher task.
        if(customerRefresher != null && timer != null)
        {
            customerRefresher.cancel();
            timer.cancel();
        }
        System.exit(0);
    }

}
