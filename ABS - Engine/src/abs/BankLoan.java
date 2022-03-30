package abs;

import xmlgenerated.AbsLoan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankLoan {

    enum Status {
        PENDING,
        ACTIVE,
        RISK,
        FINISHED
    }

    private String loanID;
    private String loanCategory;
    private final int loanAmount; // Loan amount of money.
    private int loanTotalTime;
    private int loanStartTime; // in yaz - set value when being active.
    private int loanInterestPerPayment;
    private int paymentInterval; // Time in yaz for every customer payment.(ex: every 2 yaz etc...)
    private Map<String, Integer> loanInvestors; // List of investors and their amount of investment;
    private Status loanStatus;
    private List<BankLoanTransaction> transactionList; // hold all transaction's history.

    public BankLoan(AbsLoan absLoan) {
        this.loanID = absLoan.getId();
        this.loanCategory = absLoan.getAbsCategory();
        this.loanAmount = absLoan.getAbsCapital();
        this.loanTotalTime = absLoan.getAbsTotalYazTime();
        this.loanStartTime = 0;
        this.loanInterestPerPayment = absLoan.getAbsIntristPerPayment();
        this.paymentInterval = absLoan.getAbsPaysEveryYaz();
        this.loanInvestors = new HashMap<>();
        this.loanStatus = Status.PENDING;
        transactionList = new ArrayList<>(5);
    }


}
