package engine.convertor;


import abs.BankCustomer;
import abs.BankLoan;
import dto.objectdata.LoanDataObject;
import xmlgenerated.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Convertor {

    // Parse AbsCategories to our BankCategories object.
    public static Set<String> parseAbsCategories(AbsCategories absCats) {
        return new HashSet<>(absCats.getAbsCategory()); // Create new set of categories name.
    }

    // Parse AbsCategories to our BankCategories object.
  /*  public static Map<String, BankCustomer> parseAbsCustomers(AbsCustomers absCats) {
        // Creating map -> Keys = Customer name, Value = Customer object.
        Map<String, BankCustomer> customerMap = new HashMap<>();

        // Parsing customers from AbsCustomers to Map object.
        for(AbsCustomer customer: absCats.getAbsCustomer()){
            BankCustomer newCustomer= new BankCustomer(customer);
            customerMap.put(customer.getName(),newCustomer);
        }

        return customerMap;
    }*/

     public static List<LoanDataObject> parseAbsLoans(BankCustomer customer, AbsLoans absLoans) {

         // List of all new loans as "LoanDataObject" so it can be sent to customer and json parse it.
         // Customer holds "LoanDataObject" lists.
         List<LoanDataObject> resLoanList = new ArrayList<>();

         // Classify and add a loan to relevant customer.
        for(AbsLoan absLoan: absLoans.getAbsLoan()){
            BankLoan B_l = new BankLoan(absLoan, customer.getName());
            customer.addLoan(B_l);

            LoanDataObject d = new LoanDataObject(B_l.getOwner(), B_l.getLoanID(), B_l.getLoanCategory(), B_l.getLoanAmount(),
                    B_l.getLoanOpeningTime(), B_l.getLoanTotalTime(), B_l.getLoanStartTime(), B_l.getLoanEndTime(),
                    B_l.getLoanInterestPerPayment(), B_l.getPaymentInterval(), B_l.getLoanStatus(),
                    B_l.getAmountLeftToActivateLoan(), B_l.getTransactionList(), B_l.getLoanInvestorsToView(), B_l.getInvestorsSellStatusMap());

            resLoanList.add(d);

        }

        return resLoanList;
     }
}
