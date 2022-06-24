package Login_Screen;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import server_con.HttpClientUtil;

import java.io.IOException;

import static server_con.HttpClientUtil.*;

public class loginScreenController {

    // FXML elements
    @FXML TextField usernameField;
    @FXML Label errorMsg;
    @FXML Button loginBtn;


    public void customerLogin(MouseEvent mouseEvent) {

        // Field Validation
        String usernameFieldContent = usernameField.getText();

        // Username field is empty.
        if(usernameFieldContent.isEmpty())
            errorMsg.setText("Error: Username field cant be empty.");

        // Server request to authenticate customer username
        // If is a new customer creates the user.
        String finalUrl = HttpUrl
                .parse(PATH + userLoginPage)
                .newBuilder()
                .addQueryParameter("username", usernameFieldContent)
                .build()
                .toString(); // Build url string.

        Request customerValidationRequest = new Request.Builder().url(finalUrl).build(); // Build http request from url string.

        String eMsg = "";
        try {
            Response res = HttpClientUtil.sendSyncRequest(customerValidationRequest);
            eMsg = res.body().string();
        } catch(IOException e) {
            eMsg = e.getMessage();
        } finally {
            errorMsg.setText(eMsg);
        }
    }
}
