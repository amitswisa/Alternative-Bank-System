package abs;

import java.util.HashSet;
import java.util.Set;

public class BankCategories {

    private Set<String> bankCategories;

    public BankCategories() {
        this.bankCategories = new HashSet<>();
    }

    public BankCategories(Set<String> setOfCategories) {
        this.bankCategories = setOfCategories;
    }

    public Set<String> getBankCategories() {
        return this.bankCategories;
    }

    public void addAnotherCategoriesSet(Set<String> setOfCat) {
        this.bankCategories.addAll(setOfCat);
    }

}
