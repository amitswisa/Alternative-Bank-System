package listview;

import dto.objectdata.CustomerAlertData;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import listview.ListItem.ListItem;

import java.awt.*;

public class ListViewCell extends ListCell<CustomerAlertData>
{
    @Override
    public void updateItem(CustomerAlertData alert, boolean empty)
    {
        super.updateItem(alert,empty);
        if(alert != null)
        {
            ListItem data = new ListItem();
            data.setInfo(alert);
            setGraphic(data.getItem());
            setPrefHeight(Region.USE_COMPUTED_SIZE);

            if(alert.isAlertGotRead())
                setStyle("-fx-background-color: #FFF;-fx-text-fill: #2e2e2e;");
            else {
                setStyle("-fx-background-color: #ededed;-fx-text-fill: #2e2e2e;");
            }
        }
    }
}