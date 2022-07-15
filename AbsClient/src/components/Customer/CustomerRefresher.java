package components.Customer;

import com.google.gson.Gson;
import dto.JSON.SystemUpdates;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import javafx.application.Platform;
import javafx.scene.control.Label;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import server_con.HttpClientUtil;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.function.Consumer;

public class CustomerRefresher extends TimerTask {

    // Hold pointers to customer set functions of those topics.
    private final Gson gson;
    private Consumer<CustomerDataObject> updateUserFunction;
    private Consumer<Integer> updateYaz;
    private Consumer<List<LoanDataObject>> all_loans;
    private Consumer<Set<String>> addCategories;
    private final String customerName;

    // Constructor
    public CustomerRefresher(Consumer<CustomerDataObject> updateUserFunction
            , Consumer<Integer> updateYaz
            , Consumer<List<LoanDataObject>> all_loans
            ,Consumer<Set<String>> addCategories
            , String customerName) {
        this.updateUserFunction = updateUserFunction;
        this.all_loans = all_loans;
        this.updateYaz = updateYaz;
        this.addCategories = addCategories;
        this.customerName = customerName;
        gson = new Gson();
    }

    // Method that runs according to timer timing.
    @Override
    public void run() {

        Request request = new Request.Builder()
                .url(HttpClientUtil.PATH + HttpClientUtil.CUSTOMER_UPDATE)
                .post(new FormBody.Builder()
                        .add("customerName", this.customerName).build())
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

                all_loans.accept(resValue.getAll_loans());
                addCategories.accept(resValue.getBankCategories());

                // Run updates by JAT
                Platform.runLater(() -> {
                    updateUserFunction.accept(customerData);
                    updateYaz.accept(resValue.getTimeInYaz());
                });
            }
        });
    }
}
