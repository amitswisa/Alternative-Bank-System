package pages.Admin_Screen;


import AppAdmin.AppAdmin;
import javafx.fxml.FXML;
import tableview.loan_tableview.loansTableView;
import tableview.customer_tableview.customerTableView;

public class AdminController {

    private AppAdmin currentAdmin;

    // @FXML
    @FXML private loansTableView loanTableController;
    @FXML private customerTableView customerTableController;

    public void setUser(AppAdmin currentAdmin) {
        this.currentAdmin = currentAdmin;
        loanTableController.setLoansObservableList(this.currentAdmin.getAllLoan());
        customerTableController.setCustomerList(this.currentAdmin.getCustomers());
    }

}
