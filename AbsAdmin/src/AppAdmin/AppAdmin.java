package AppAdmin;

import dto.JSON.AdminData;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Map;

public class AppAdmin {
    private final String username;
    private static SimpleIntegerProperty yazInTime;
    private ObservableList<LoanDataObject> allLoan;
    private ObservableList<CustomerDataObject> customers;

    public AppAdmin(String username, int yaz) {
        this.username = username;
        yazInTime = new SimpleIntegerProperty(yaz);
        this.allLoan = FXCollections.observableArrayList();
        this.customers = FXCollections.observableArrayList();//TODO- INIT with the customers
    }

    // Updates customer from data returns from server.
    public void updateUser(AdminData data) {
        this.setCustomers(data.getCustomers());
        this.setAllLoan(data.getAllLoans());
    }

    public void setAllLoan(List<LoanDataObject> allLoan) {
        this.allLoan.setAll(allLoan);
    }

    public void setCustomers(List<CustomerDataObject> customers) {
        this.customers.setAll(customers);
    }

    public String getName() {
        return this.username;
    }

    public static int getYazInTimeAsInt() {
        return yazInTime.get();
    }

    public static SimpleIntegerProperty getYazInTime() {
        return yazInTime;
    }

    public void setYaz(int yaz) {
        yazInTime.set(yaz);
    }

    public ObservableList<LoanDataObject> getAllLoan() {
        return allLoan;
    }

    public ObservableList<CustomerDataObject> getCustomers() {
        return customers;
    }
}
