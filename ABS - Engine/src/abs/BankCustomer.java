package abs;

import dto.infoholder.CustomerOperationData;
import xmlgenerated.AbsCustomer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class BankCustomer {
    private final String name;
    private int balance;
    private Map<Integer, Stack<CustomerOperationData>> customerLog;
    //private Map<Integer, List<BankLoan>> loansTaken; // List of all loans current customer took.
    //private Map<Integer, List<BankLoan>> loansInvested; // List of all loans current customer invest in.

    public BankCustomer(AbsCustomer customer){
        this.name = customer.getName();
        this.balance = customer.getAbsBalance();
        customerLog = new HashMap<>();
    }
}
