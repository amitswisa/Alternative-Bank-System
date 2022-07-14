package dto.JSON;

import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;

import java.util.List;

public class SystemUpdates {

    private CustomerDataObject curCustomerData;
    private List<LoanDataObject> all_loans;
    private Integer timeInYaz;

    public SystemUpdates(CustomerDataObject curCustomerData,
                         List<LoanDataObject> all_loans,
                         Integer timeInYaz) {
        this.curCustomerData = curCustomerData;
        this.all_loans = all_loans;
        this.timeInYaz = timeInYaz;
    }

    public CustomerDataObject getCurCustomerData() {
        return curCustomerData;
    }

    public List<LoanDataObject> getAll_loans() {
        return all_loans;
    }

    public Integer getTimeInYaz() {
        return timeInYaz;
    }
}
