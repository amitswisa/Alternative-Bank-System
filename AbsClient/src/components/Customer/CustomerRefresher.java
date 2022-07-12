package components.Customer;

import com.google.gson.Gson;
import dto.objectdata.CustomerAlertData;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.CustomerOperationData;
import dto.objectdata.LoanDataObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import server_con.HttpClientUtil;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class CustomerRefresher extends TimerTask {

    // Hold pointers to customer set functions of those topics.
    private final Gson gson;
    private Consumer<List<CustomerOperationData>> logCustomerConsumer;
    private Consumer<List<LoanDataObject>> investmentListConsumer;
    private Consumer<List<LoanDataObject>> loanListConsumer;
    private Consumer<List<CustomerAlertData>> listOfAlertsConsumer;
    private final String customerName;

    // Constructor
    public CustomerRefresher(Consumer<List<CustomerOperationData>> logCustomerConsumer
            ,Consumer<List<LoanDataObject>> investmentListConsumer
            ,Consumer<List<LoanDataObject>> loanListConsumer
            ,Consumer<List<CustomerAlertData>> listOfAlertsConsumer
            ,String customerName) {
        this.logCustomerConsumer = logCustomerConsumer;
        this.investmentListConsumer = investmentListConsumer;
        this.loanListConsumer = loanListConsumer;
        this.listOfAlertsConsumer = listOfAlertsConsumer;
        this.customerName = customerName;
        gson = new Gson();
    }

    // Method that runs according to timer timing.
    @Override
    public void run() {
        HttpClientUtil.runAsync(HttpClientUtil.PATH + HttpClientUtil.CUSTOMER_UPDATE
                                    , this.customerName, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                CustomerDataObject resValue = gson.fromJson(response.body().string(), CustomerDataObject.class);
                logCustomerConsumer.accept(resValue.getLogCustomer());
                investmentListConsumer.accept(resValue.getInvestmentList());
                loanListConsumer.accept(resValue.getLoanList());
                listOfAlertsConsumer.accept(resValue.getListOfAlerts());
            }
        });
    }
}
