package AppAdmin;

import com.google.gson.Gson;
import dto.JSON.SystemUpdates;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import server_con.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AdminRefresher extends TimerTask {

    // Hold pointers to customer set functions of those topics.
    private final Gson gson;
    private Consumer<List<LoanDataObject>> updateLoans;
    private Consumer<Integer> updateYaz;
    private Consumer<List<CustomerDataObject>> customers;


    // Constructor
    public AdminRefresher(Consumer<List<LoanDataObject>> updateLoans
            , Consumer<Integer> updateYaz
            , Consumer<List<CustomerDataObject>> customers) {
        this.updateLoans = updateLoans;
        this.updateYaz = updateYaz;
        this.customers = customers;
        gson = new Gson();
    }

    //TODO - Method that runs according to timer timing.
    @Override
    public void run() {

        Request request = new Request.Builder()
                .url(HttpClientUtil.PATH + HttpClientUtil.ADMIN_UPDATE)
                .get()
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                SystemUpdates resValue = gson.fromJson(response.body().string(), SystemUpdates.class);

                // Extract & Update data.
                CustomerDataObject customerData = resValue.getCurCustomerData();

                updateLoans.accept(resValue.getAll_loans());

                // Run updates by JAT
                Platform.runLater(() -> {
                   // updateUserFunction.accept(customerData);
                    updateYaz.accept(resValue.getTimeInYaz());
                });
            }
        });
    }
}
