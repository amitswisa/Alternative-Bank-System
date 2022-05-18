package customer_screen;

import Utils.User;
import abs.BankCustomer;
import com.sun.xml.internal.ws.api.pipe.Engine;
import dto.objectdata.CustomerDataObject;
import engine.EngineManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import loan_tableview.loansTableView;

import java.net.URL;
import java.util.ResourceBundle;

public class customerScreenController implements Initializable {

    private EngineManager engineManager;
    private CustomerDataObject currentCustomer;

    @FXML private User currentUser;
    @FXML private loansTableView myLoansTableController;
    @FXML private loansTableView myInvestmentsLoansController;

    public customerScreenController() {
        currentUser = new User();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Do some changes when client changes user type.
        currentUser.getUsernameProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals(currentUser.getDefaultUserName()))
                    return;

                updateCustomerInfo(newValue);
            }
        });
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    private void updateCustomerInfo(String newName) {

        // update current user Customer information DTO when selected user changes.
        this.currentCustomer = this.engineManager.getCustomerByName(newName);

        myLoansTableController.setLoanItems(this.currentCustomer.getLoanList());
        myInvestmentsLoansController.setLoanItems(this.currentCustomer.getInvestmentList());

    }
}
