package dto.JSON;

import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdminData {

    private List<LoanDataObject> allLoans;
    private List<CustomerDataObject> customers;
    private Integer timeInYaz;


    public AdminData(List<CustomerDataObject> customers,
                         List<LoanDataObject> allLoans,
                         Integer timeInYaz) {
        this.customers = customers;
        this.allLoans = allLoans;
        this.timeInYaz = timeInYaz;
    }

    public List<LoanDataObject> getAllLoans() {
        return this.allLoans;
    }

    public List<CustomerDataObject> getCustomers() {
        return this.customers;
    }

    public Integer getTimeInYaz() {
        return timeInYaz;
    }
}
