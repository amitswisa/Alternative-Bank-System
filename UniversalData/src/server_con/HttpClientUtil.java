package server_con;

import okhttp3.*;

import java.io.IOException;

public class HttpClientUtil
{
    public static final OkHttpClient client = new OkHttpClient();
    public static final String hostAddress = "http://localhost:8080";
    public static final String BASE_URL = "/";

    // Pages paths
    public static final String userLoginPage = "login";

    public static final String PATH = hostAddress + BASE_URL;

    public static Response sendSyncRequest(Request req) throws IOException {
        Call newCall = client.newCall(req); // Create call object.
        return newCall.execute();
    }



}