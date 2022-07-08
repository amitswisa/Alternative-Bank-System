package dto.infodata;

import dto.objectdata.LoanDataObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XmlFileData extends DataTransferObject {

    public List<LoanDataObject> listOfLoans;
    public Set<String> categoriesNames;

    public XmlFileData() {
        super();
        listOfLoans = new ArrayList<>();
        categoriesNames = new HashSet<>();
    }

    public List<LoanDataObject> getListOfLoans() {
        return listOfLoans;
    }

    public void setListOfLoans(List<LoanDataObject> list_of_loans) {
        this.listOfLoans.addAll(list_of_loans);
    }

    public Set<String> getCategoriesNames() {
        return categoriesNames;
    }

    public void setCategoriesNames(Set<String> set_of_categories) {
        this.categoriesNames.addAll(set_of_categories);
    }
}
