// Pallete https://coolors.co/palette/ffd6ff-e7c6ff-c8b6ff-b8c0ff-bbd0ff

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_screen/mainScreen.fxml"));
        Scene firstScene = new Scene(loader.load());
        primaryStage.setScene(firstScene);
        primaryStage.setTitle("Alternative Banking System");
        primaryStage.show();
    }
}