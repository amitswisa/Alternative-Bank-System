package dto.infoholder;

import abs.BankLoan;
import dto.DataTransferObject;

import java.util.HashSet;
import java.util.Set;

public class BankOperationData extends DataTransferObject {
    Set<BankLoan> loans;

    public BankOperationData(int timeInYaz, String message){
        super(message,timeInYaz);
        loans= new HashSet<>();
        //TODO- load all the loans
    }
}
