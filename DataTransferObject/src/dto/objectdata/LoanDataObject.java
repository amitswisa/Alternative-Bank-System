package dto.objectdata;

import abs.BankLoan;
import abs.BankLoanTransaction;
import abs.BankSystem;
import abs.Investor;
import dto.infodata.DataTransferObject;

import java.util.Map;

public class LoanDataObject extends DataTransferObject {

    private BankLoan loan;

    public LoanDataObject(BankLoan loan){
        super();
        this.loan = loan;
    }

    // Section 2 from menu.
    public void showLoan() {
        System.out.println(this);

        //Print all the investors.
        Map<String, Investor> temp = this.loan.getLoanInvestors();
        if(temp != null && temp.size() > 0) {
            System.out.println("List of investors: ");
            for (Map.Entry<String, Investor> invester : temp.entrySet())
                System.out.println("Name:" + invester.getKey() + ", Investment: " + invester.getValue().getInitialInvestment());
        } else
            System.out.println("No investors at this moment.");

        //Show more deatails according to loan status.
        BankLoan.Status status = this.loan.getLoanStatus();
        switch (status){
            case ACTIVE: {
                this.presentActiveStatusData();
                break;
            }
            case RISK: {
                this.presentActiveStatusData();
                this.presentRiskStatusData();
                break;
            }
            case FINISHED: {
                System.out.println("Starting loan time: " + this.loan.getLoanStartTime());
                System.out.println("Ending loan time: " + this.loan.getLoanEndTime());
                break;
            }
            default: {
                if(status != BankLoan.Status.PENDING) // if current loan status is not pending or any of the above.
                    System.out.println("Error: invalid loan status.");
            }

        }
        System.out.println(" ");
    }

    private void presentActiveStatusData() {
        System.out.println("Started YAZ:" + this.loan.getLoanStartTime()); //print the YAZ start to be active

        // Calculate next payment in yaz.
        int nextYaz = this.loan.getNextPaymentTime();
        if(nextYaz <= BankSystem.getCurrentYaz())
            nextYaz += + this.loan.getPaymentInterval();
        System.out.println("Next YAZ payment: " + nextYaz);

        int totalInterestPayed = 0;
        int totalLoanPayment = 0;
        // Print all transaction that are payed in time.
        for(BankLoanTransaction tranc : this.loan.getPayedTransactions()) {
            System.out.println(tranc);
            totalInterestPayed += tranc.getInterestValue();
            totalLoanPayment += tranc.getPaymentValue();
        }

        System.out.println("Total payments already payed: " + totalLoanPayment);
        System.out.println("Total interest already payed: " + totalInterestPayed);
        System.out.println("Total payments left to pay: " + (this.loan.getLoanAmount()-totalLoanPayment));
        System.out.println("Total interest left to pay: "
                + (this.loan.getTotalLoanInterestInMoney()-totalInterestPayed));

    }

    private void presentRiskStatusData() {
        System.out.println("There are " + this.loan.getUnpayedTransactions().size() + " unpaied payments.");
        System.out.println("Total unpaid payments so far: "
                + (this.loan.getLastUnPaidTransaction().getPaymentValue() + this.loan.getLastUnPaidTransaction().getInterestValue()));
    }

    // Part of section 3 from menu.
    public String getLoanDetails() {
         return "   Loan Name: " + this.loan.getLoanID() + ".\n" +
                "      Loan Category: " + this.loan.getLoanCategory() + ".\n" +
                "      Loan Amount: " + this.loan.getLoanAmount() + ".\n" +
                "      Payment Interval: " + this.loan.getPaymentInterval() + ".\n" +
                "      Loan Interest Per Payment: " + this.loan.getLoanInterestPerPayment() + ".\n" +
                "      Loan total (interest+capital): " + (this.loan.getTotalLoanInterestInMoney()+this.loan.getLoanAmount()) + ".\n" +
                "      Loan Status: " + this.loan.getLoanStatus() + ".\n" +
                this.getLoanDetailsAccordingToStatus();
    }

    private String getLoanDetailsAccordingToStatus() {
        String res = "";
        switch(loan.getLoanStatus()) {
            case PENDING: {
                res = "      Amount left to make loan active: " + loan.getAmountLeftToActivateLoan() + "\n";
                break;
            }
            case ACTIVE: {
                res = "      Next payment date (in YAZ): " + this.loan.getNextPaymentTime()
                        +".\n      Next payment value: " + ((this.loan.getLoanAmount()/(this.loan.getLoanTotalTime()/this.loan.getPaymentInterval()))+this.loan.getLoanInterestPerPayment()) + "\n";
                break;
            }
            case RISK: {
                res = "      Total value of unpaied transactions: " + this.loan.getUnpayedTransactionsAmountOfMoney() + "\n";
                break;
            }
            case FINISHED: {
                res = "      Loan started at: " + this.loan.getLoanStartTime() +
                        ".\n      Loan ending time: " + (this.loan.getLoanEndTime()) + "\n";
                break;
            }
        }
        return res;
    }

    public String getLoanOwnerName() {
        return this.loan.getOwner();
    }

    public int getLoanTotalTime() {
        return this.loan.getLoanTotalTime();
    }

    public int getLoanInterest() {return this.loan.getLoanInterestPerPayment();}

    public String getLoanCatgory() {return this.loan.getLoanCategory();}

    public BankLoan.Status getLoanStatus() {return this.loan.getLoanStatus();}

    public String getLoanName() {return this.loan.getLoanID(); }

    public int getLoanOpeningTime() {
        return this.loan.getLoanOpeningTime();
    }

    @Override
    public String toString() {
        return  "Loan ID: " + this.loan.getLoanID() + "\n" +
                "Loan Category: " + this.loan.getLoanCategory() + "\n" +
                "Loan Amount: " + this.loan.getLoanAmount() + "\n" +
                "Original Time of loan: " + this.loan.getLoanTotalTime() + "\n" +
                "Loan Interest: " + this.loan.getTotalLoanInterestInMoney() + "\n" +
                "Payment Interval: " + this.loan.getPaymentInterval() + "\n" +
                "Loan Status: " + this.loan.getLoanStatus();
    }

}
