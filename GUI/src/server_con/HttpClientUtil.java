package server_con;

import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class HttpClientUtil
{
    public static final OkHttpClient client = new OkHttpClient();
    public static final String hostAddress = "http://localhost:8080";
    public static final String BASE_URL = "/Abs";
    public static final String CUSTOMER_UPDATE = "/customerUpdates";
    public static final String MAKE_INVESTMENT = "/makeInvestment";

    // Pages paths
    public static final String userLoginPage = "/login";

    public static final String PATH = hostAddress + BASE_URL;

    // Sync HTTP Requests sender method.
    public static Response sendSyncRequest(Request req) throws IOException {
        Call newCall = client.newCall(req); // Create call object.
        return newCall.execute();
    }

    // Async HTTP Post request method.
    public static void runAsync(Request request, Callback callback) {

        Call call = HttpClientUtil.client.newCall(request);

        call.enqueue(callback);
    }



}