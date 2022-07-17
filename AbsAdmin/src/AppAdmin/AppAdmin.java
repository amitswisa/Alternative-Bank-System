package AppAdmin;

import dto.JSON.AdminData;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class AppAdmin {
    private final String username;
    private static SimpleIntegerProperty yazInTime;
    private final ObservableList<LoanDataObject> allLoan;
    private final ObservableList<CustomerDataObject> customers;

    public AppAdmin(String username, int yaz) {
        this.username = username;
        yazInTime = new SimpleIntegerProperty(yaz);
        this.allLoan = FXCollections.observableArrayList();
        this.customers = FXCollections.observableArrayList();
    }

    // Updates customer from data returns from server.
    public void updateAdminData(AdminData data) {
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
