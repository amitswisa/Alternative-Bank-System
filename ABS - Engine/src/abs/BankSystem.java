package abs;

import engine.convertor.Convertor;
import xmlgenerated.AbsDescriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BankSystem {

    private static int currentYaz;
    private BankCategories categories;
    private Map<String, BankCustomer> customers;

    // TODO - Ask Aviad.
    public BankSystem()
    {
        currentYaz = 1;
        categories =new BankCategories();
        customers = new HashMap<>();
    }

    public BankSystem(AbsDescriptor absDescriptor)
    {
        currentYaz = 1; // After file loaded successfully from xml -> start bank system from scratch.
        categories = Convertor.parseAbsCategories(absDescriptor.getAbsCategories());
        customers = Convertor.parseAbsCustomers(absDescriptor.getAbsCustomers());
        Convertor.parseAbsLoans(customers, absDescriptor.getAbsLoans());
    }

    public static int getCurrentYaz() {
        return currentYaz;
    }
}
