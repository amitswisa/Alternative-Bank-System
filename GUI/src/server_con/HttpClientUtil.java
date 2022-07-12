package server_con;

import okhttp3.*;

import java.io.IOException;

public class HttpClientUtil
{
    public static final OkHttpClient client = new OkHttpClient();
    public static final String hostAddress = "http://localhost:8080";
    public static final String BASE_URL = "/Abs";
    public static final String CUSTOMER_UPDATE = "/customerUpdates";

    // Pages paths
    public static final String userLoginPage = "/login";

    public static final String PATH = hostAddress + BASE_URL;

    // Sync HTTP Requests sender method.
    public static Response sendSyncRequest(Request req) throws IOException {
        Call newCall = client.newCall(req); // Create call object.
        return newCall.execute();
    }

    // Async HTTP Post request method.
    public static void runAsync(String finalUrl, String customerName, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(new FormBody.Builder()
                        .add("customerName", customerName).build())
                .build();

        Call call = HttpClientUtil.client.newCall(request);

        call.enqueue(callback);
    }



}