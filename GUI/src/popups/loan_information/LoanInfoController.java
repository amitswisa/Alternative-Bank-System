package popups.loan_information;

import dto.objectdata.LoanDataObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoanInfoController {

    @FXML private Label loanName;

    // Grid elements
    @FXML private Label owner;
    @FXML private Label category;
    @FXML private Label capital;
    @FXML private Label interest;
    @FXML private Label totalTime;
    @FXML private Label interval;
    @FXML private Label status;


    public void setLoanPopupInfo(LoanDataObject loan) {
        this.loanName.setText(loan.getId());
        this.owner.setText(loan.getOwner());
        this.category.setText(loan.getCategory());
        this.capital.setText(loan.getCapital()+"");
        this.interest.setText(loan.getInterest()+"");
        this.totalTime.setText(loan.getTotalTime()+"");
        this.interval.setText(loan.getLoanInterval() + "");
        this.status.setText(loan.getStatus());
    }
}
