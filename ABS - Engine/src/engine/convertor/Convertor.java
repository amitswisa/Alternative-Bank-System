package engine.convertor;


import abs.BankCustomer;
import abs.BankLoan;
import xmlgenerated.*;
import java.util.HashSet;
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

     public static void parseAbsLoans(BankCustomer customer, AbsLoans absLoans) {
         // Classify and add a loan to relevant customer.
        for(AbsLoan absLoan: absLoans.getAbsLoan()){
            customer.addLoan(new BankLoan(absLoan, customer.getName()));
        }
     }
}
