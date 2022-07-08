import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pages.Login_Screen.loginScreenController;

import java.io.IOException;
import java.net.URL;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL loginPage = getClass().getResource("/pages/Login_Screen/loginScreen.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            loginScreenController loginScreenController = fxmlLoader.getController();
            loginScreenController.setPostLoginData("/pages/Admin_Screen/adminScreen.fxml", primaryStage);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
