package pages.Login_Screen;

import AppAdmin.AppAdmin;
import Utils.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import pages.Main_Admin_Screen.mainScreenController;
import server_con.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static server_con.HttpClientUtil.*;

public class loginScreenController {

    // FXML elements
    @FXML TextField usernameField;
    @FXML Label errorMsg;
    @FXML Button loginBtn;

    // Data members
    private String redirectURL;
    private Stage primStage = null;

    public void customerLogin(MouseEvent mouseEvent) {

        // Field Validation
        String usernameFieldContent = usernameField.getText();

        // Username field is empty.
        if(usernameFieldContent.isEmpty())
            errorMsg.setText("Error: Username field cant be empty.");

        // Server request to authenticate customer username
        // If is a new customer creates the user.
        String finalUrl = HttpUrl
                .parse(PATH + adminLoginPage)
                .newBuilder()
                .addQueryParameter("username", usernameFieldContent)
                .build()
                .toString(); // Build url string.

        Request customerValidationRequest = new Request.Builder().url(finalUrl).build(); // Build http request from url string.

        String eMsg = "";
        try {
            Response res = HttpClientUtil.sendSyncRequest(customerValidationRequest);
            eMsg = res.body().string();

            // If user is authenticated and server approved login.
            // Then pass username to Customer's page Controller and open new scene with customer papge.
            System.out.println(res.code());
            if(res.code() == 200) {
                FXMLLoader loader = new FXMLLoader();
                URL adminPage = getClass().getResource("/pages/Main_Admin_Screen/mainScreen.fxml");
                loader.setLocation(adminPage);

                // Get costumer controller and pass user name to it.
                Parent root = loader.load();
                mainScreenController mainController = loader.getController();

                this.primStage.setScene(new Scene(root));
                mainController.setUser(new AppAdmin(usernameFieldContent, Integer.parseInt(eMsg)));
                this.primStage.show();
            }

            System.out.println(eMsg);


        } catch(IOException e) {
            eMsg = e.getMessage();
        } finally {
            errorMsg.setText(eMsg);
        }
    }

    public void setPostLoginData(String url, Stage stage) {
        this.primStage = stage;
        this.redirectURL = url;
    }
}
