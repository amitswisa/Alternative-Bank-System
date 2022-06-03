package generalObjects;

import dto.infodata.DataTransferObject;
import dto.objectdata.LoanDataObject;
import engine.EngineManager;
import generalObjects.Triple;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.time.Duration;
import java.util.List;

public class LoanTask extends Task<Boolean> {

    private final long SLEEP_TIME = 1;
    private final List<Triple<String,Integer,String>> loansToInvest;
    private final EngineManager engine;
    private final int amountToInvest;
    private final String user_name;
    private int investedSoFar;
    private int maxToInvest;
    private final Alert alertDialog;

    public LoanTask(List<Triple<String,Integer,String>> loansToInvest,
                    EngineManager engine, int amountToInvest, String user_name, Alert alertDialog) {
        this.loansToInvest = loansToInvest;
        this.engine = engine;
        this.amountToInvest = amountToInvest;
        this.user_name = user_name;
        investedSoFar = 0;
        maxToInvest = loansToInvest.size();
        this.alertDialog = alertDialog;
    }

    @Override
    protected Boolean call() throws Exception  {
        // make investments!
        updateMessage("Trying to invest...");

        try {
            String res = this.engine.makeInvestments(user_name, amountToInvest, loansToInvest, this);
            updateMessage(res);
        } catch (DataTransferObject e) {
            updateMessage(e.getMessage());
            return false;
        }

        return true;
    }

    public void progressUpdate() {
        this.investedSoFar++;
        this.updateProgress(this.investedSoFar, this.maxToInvest);
    }

    public void sleepForAWhile() {
        try {
            Thread.sleep(Duration.ofSeconds(SLEEP_TIME).toMillis()); // 2 Sec
        } catch (InterruptedException ignored) {

        }
    }

    public void setMessage(String res) {
        updateMessage(res);
    }
}


