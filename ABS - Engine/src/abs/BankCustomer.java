package abs;

import dto.infoholder.CustomerOperationData;
import xmlgenerated.AbsCustomer;

import java.util.*;

public class BankCustomer {
    private final String name;
    private int balance;
    private Map<Integer, Stack<CustomerOperationData>> customerLog;
    private Map<Integer, Set<BankLoan>> loansTaken; // List of all loans current customer took.
    private Map<Integer, Set<BankLoan>> loansInvested; // List of all loans current customer invest in.

    public BankCustomer(AbsCustomer customer){
        this.name = customer.getName();
        this.balance = customer.getAbsBalance();
        customerLog = new HashMap<>();
        loansTaken = new HashMap<>();
        loansInvested = new HashMap<>();
    }

    public void addLoan(BankLoan loan) {
        Set<BankLoan> loans;
        if(loansTaken.containsKey(BankSystem.getCurrentYaz()))
            loans = loansTaken.get(BankSystem.getCurrentYaz());
        else
            loans = new HashSet<>();

        loans.add(loan);
        loansTaken.put(BankSystem.getCurrentYaz(), loans);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
