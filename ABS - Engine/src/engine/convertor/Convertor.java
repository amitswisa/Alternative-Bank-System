package engine.convertor;

import abs.BankCategories;
import abs.BankCustomer;
import abs.BankLoan;
import abs.BankSystem;
import xmlgenerated.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Convertor {

    // Parse AbsCategories to our BankCategories object.
    public static BankCategories parseAbsCategories(AbsCategories absCats) {
        Set<String> bankCategories = new HashSet<>(absCats.getAbsCategory()); // Create new set of categories name.
        return new BankCategories(bankCategories);
    }

    // Parse AbsCategories to our BankCategories object.
    public static Map<String, BankCustomer> parseAbsCustomers(AbsCustomers absCats) {
        // Creating map -> Keys = Customer name, Value = Customer object.
        Map<String, BankCustomer> customerMap = new HashMap<>();

        // Parsing customers from AbsCustomers to Map object.
        for(AbsCustomer customer: absCats.getAbsCustomer()){
            BankCustomer newCustomer= new BankCustomer(customer);
            customerMap.put(customer.getName(),newCustomer);
        }

        return customerMap;
    }
     public static void parseAbsLoans(Map<String,BankCustomer> customerMap, AbsLoans absLoans) {
         // Classify and add a loan to relevant customer.
        for(AbsLoan absLoan: absLoans.getAbsLoan()){
            customerMap.get(absLoan.getAbsOwner()).addLoan(new BankLoan(absLoan));
        }
     }
}
