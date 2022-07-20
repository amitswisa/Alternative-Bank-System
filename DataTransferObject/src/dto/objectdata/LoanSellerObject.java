package dto.objectdata;

public class LoanSellerObject {

    private final LoanDataObject loan;
    private final String sellerName;

    public LoanSellerObject(LoanDataObject loan, String sellerName) {
        this.loan = loan;
        this.sellerName = sellerName;
    }

    public LoanDataObject getLoan() {
        return loan;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getLoanName() {
        return this.getLoan().getLoanID();
    }

    public String getLoanOwner() {
        return this.getLoan().getOwner();
    }

    public String getSeller() {
        return this.getSellerName();
    }

    public String getShare() {
        Integer totalShare = this.getLoan().getInvestorShare(this.getSellerName());
        if(totalShare == null)
            return "Not found";

        return ((totalShare / getLoan().getNumberOfPayment()) * ((this.getLoan().getLoanTotalTime() / this.getLoan().getPaymentInterval()) - this.getLoan().getThisPaymentNumber() + 1)) + "";
    }

    public String getInterest() {
        return this.getLoan().getLoanInterestPerPayment() + "%";
    }

    public String getPaymentLeft() {
        return ((this.getLoan().getLoanTotalTime() / this.getLoan().getPaymentInterval()) - this.getLoan().getThisPaymentNumber() + 1) + "";
    }


}
