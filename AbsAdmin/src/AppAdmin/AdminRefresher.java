package AppAdmin;

import com.google.gson.Gson;
import dto.JSON.AdminData;
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
    private Consumer<AdminData> data;


    // Constructor
    public AdminRefresher(Consumer<AdminData> data) {
        this.data = data;
        gson = new Gson();
    }

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
                AdminData resValue = gson.fromJson(response.body().string(), AdminData.class);

                Platform.runLater(() -> {
                    data.accept(resValue);
                });
            }
        });
    }
}
