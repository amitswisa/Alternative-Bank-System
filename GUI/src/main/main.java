package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gp = new GridPane();
        Scene temp = new Scene(gp, 300, 300);
        primaryStage.setScene(temp);
        primaryStage.show();
    }
}
