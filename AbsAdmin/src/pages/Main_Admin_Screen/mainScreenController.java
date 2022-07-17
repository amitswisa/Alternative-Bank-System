package pages.Main_Admin_Screen;

import AppAdmin.AppAdmin;
import AppAdmin.AdminRefresher;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import pages.Admin_Screen.AdminController;
import server_con.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static AppAdmin.AppAdmin.getYazInTime;
import static server_con.HttpClientUtil.PATH;

public class mainScreenController implements Initializable {

    // Data members
    private Timer timer;
    private AdminRefresher adminRefresher;
    private AppAdmin currentAdmin;
    private TranslateTransition translateTransition;
    private final ChoiceDialog<String> dialog;

    // Pages
    @FXML private AdminController adminPageComponentController;
    @FXML private AnchorPane mainPane;

    // FXML members
    @FXML private ImageView settingsBtn;
    @FXML private Button rewindBtn;
    @FXML private Label yazLabel;


    public mainScreenController() {

        timer = new Timer();

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

        

    }

    // TimerTask creation, every 400ms refresh data from server.
   private void startAdminDataUpdate() {
        adminRefresher = new AdminRefresher(currentAdmin::updateAdminData);
        timer = new Timer();
        timer.schedule(adminRefresher, 400, 400);
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

        String finalUrl = HttpUrl
                .parse(PATH + "/increaseYaz")
                .newBuilder()
                .build()
                .toString(); // Build url string.

        Request increaseYazRequest = new Request.Builder().url(finalUrl).build(); // Build http request from url string.

        try {
            Response res = HttpClientUtil.sendSyncRequest(increaseYazRequest);
            String yazRes = res.body().string().replace("\"", "");
            this.currentAdmin.setYaz(Integer.parseInt(yazRes));

        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setUser(AppAdmin currentAdmin) {
        this.currentAdmin = currentAdmin;
        adminPageComponentController.setUser(this.currentAdmin);

        yazLabel.setText(getYazInTime().get()+"");
        getYazInTime().addListener((observable, oldValue, newValue) -> {
            yazLabel.setText(newValue.intValue() + "");
        });

        startAdminDataUpdate();
    }

    public void shutdown() {
        // When closing app stop refresher task.
        if(adminRefresher != null && timer != null)
        {
            adminRefresher.cancel();
            timer.cancel();
        }
        System.exit(0);
    }
}
