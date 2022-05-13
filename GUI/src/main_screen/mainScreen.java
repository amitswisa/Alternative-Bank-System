package main_screen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class mainScreen {

    @FXML AnchorPane addBody;
    @FXML ChoiceBox<String> userTypeChoice;

    public mainScreen() {

    }

    @FXML
    public void initialize() throws IOException {
        // Adding admin fxml as default app body.
        Node node = (Node) FXMLLoader.load(getClass().getResource("/admin_screen/adminScreen.fxml"));
        addBody.getChildren().setAll(node);

        //Add Values to ChoiceBox.
        ObservableList<String> userChoiceList = FXCollections.observableArrayList();
        userChoiceList.add("Admin");
    }

}
