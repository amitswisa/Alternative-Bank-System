package pages.Admin_Screen;


import javafx.fxml.FXML;
import pages.Main_Admin_Screen.mainScreenController;
import tableview.loan_tableview.loansTableView;
import tableview.customer_tableview.customerTableView;
import java.util.ArrayList;
import java.util.List;

public class AdminController {

    private List<String> customersNames;

    // @FXML
    @FXML private loansTableView loanTableController;
    @FXML private customerTableView customerTableController;

    public AdminController() {
        customersNames = new ArrayList<String>();
    }

    public void setInitData(mainScreenController mainScreenController) {
        //TODO- set table information
    }


//TODO - update table information
   /* public void updateAdminLists() {
        if(fileToLoad != null) {
            loanTableController.setLoanItems(this.engineManager.getAllLoansData()); // Transfer loan's list from loaded file to controller.
            customerTableController.setCustomerList(this.engineManager.getAllCustomerData()); // Transfer customer's list from loaded file to controller.
        }
    }*/

}
