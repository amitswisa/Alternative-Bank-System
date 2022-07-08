package popups.loan_information;

import dto.objectdata.LoanDataObject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class LoanInfoController implements Initializable {

    @FXML private Label loanName;

    // Grid elements
    @FXML private Label owner;
    @FXML private Label category;
    @FXML private Label capital;
    @FXML private Label interest;
    @FXML private Label totalTime;
    @FXML private Label interval;
    @FXML private Label status;
    @FXML private ScrollPane scrollPane;

    public void setLoanPopupInfo(LoanDataObject loan) {
        this.loanName.setText(loan.getLoanID());
        this.owner.setText(loan.getOwner());
        this.category.setText(loan.getLoanCategory());
        this.capital.setText(loan.getLoanAmount()+"");
        this.interest.setText(loan.getLoanInterestPerPayment()+"");
        this.totalTime.setText(loan.getLoanTotalTime()+"");
        this.interval.setText(loan.getPaymentInterval() + "");
        this.status.setText(loan.getLoanStatus().toString());

        scrollPane.setContent(new Text(loan.showLoanData(0)));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Text text = new Text();
        scrollPane.setContent(text);
    }
}
