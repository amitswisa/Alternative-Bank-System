package abs;

import xmlgenerated.AbsCategories;

import java.util.HashSet;
import java.util.Set;

public class BankCategories {

    private Set<String> bankCategories;

    public BankCategories(Set<String> setOfCategories) {
        this.bankCategories = setOfCategories;
    }

    public Set<String> getBankCategories() {
        return this.bankCategories;
    }

}
