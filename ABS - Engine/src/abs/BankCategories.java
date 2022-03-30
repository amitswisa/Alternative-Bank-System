package abs;

import xmlgenerated.AbsCategories;

import java.util.HashSet;
import java.util.Set;

public class BankCategories {

    private Set<String> bankCategories;

    public BankCategories() { // TODO - Ask Aviad.
        this.bankCategories = new HashSet<>();
    }

    public BankCategories(Set<String> setOfCategories) {
        this.bankCategories = setOfCategories;
    }



}
