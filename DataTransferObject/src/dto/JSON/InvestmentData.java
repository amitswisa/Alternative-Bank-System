package dto.JSON;

import dto.objectdata.Triple;

import java.util.List;

public class InvestmentData {

    private List<Triple<String,Integer,String>> nameOfLoansToInvest;
    private int investmentAmount;
    private String investorName;

    public InvestmentData(List<Triple<String, Integer, String>> nameOfLoansToInvest, int investmentAmount, String investorName) {
        this.nameOfLoansToInvest = nameOfLoansToInvest;
        this.investmentAmount = investmentAmount;
        this.investorName = investorName;
    }

    public List<Triple<String, Integer, String>> getNameOfLoansToInvest() {
        return nameOfLoansToInvest;
    }

    public int getInvestmentAmount() {
        return investmentAmount;
    }

    public String getInvestorName() {
        return investorName;
    }
}
