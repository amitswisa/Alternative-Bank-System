package pages.listview;

import dto.objectdata.CustomerAlertData;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Region;
import pages.listview.ListItem.ListItem;

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