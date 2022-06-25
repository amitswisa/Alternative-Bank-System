package HttpClientUtil;

import okhttp3.*;

import java.io.IOException;

public class HttpClientUtil
{
    // Create first client-server connection.
    public static final OkHttpClient client = new OkHttpClient();

    // Represent yaz in time.
    public static int YAZ_TIME = 1;

    // Pages paths
    public static final String hostAddress = "http://localhost:8080";
    public static final String BASE_URL = "/Abs";
    public static final String userLoginPage = "/login";
    public static final String PATH = hostAddress + BASE_URL;

    // Method send request to server (SYNC) and returns response.
    public static Response sendSyncRequest(Request req) throws IOException {
        Call newCall = client.newCall(req); // Create call object.
        return newCall.execute();
    }

    // Get yaz time.
    public static int getYazTime() {
        return YAZ_TIME;
    }



}