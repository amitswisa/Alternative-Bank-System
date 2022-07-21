package http.servlets;

import abs.BankSystem;
import com.google.gson.GsonBuilder;
import dto.JSON.AdminData;
import dto.objectdata.CustomerAlertData;
import dto.objectdata.CustomerOperationData;
import dto.objectdata.LoanDataObject;
import dto.objectdata.TransactionDataObject;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "End Rewind Servlet", urlPatterns = "/endRewind")
public class EndRewindServlet extends HttpServlet {

    private final GsonBuilder gsonBuilder;

    public EndRewindServlet() {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LoanDataObject.class,new LoanDataObject.LoanDataObjectAdapter());
        gsonBuilder.registerTypeAdapter(CustomerOperationData.class,new CustomerOperationData.CustomerOperationDataAdapter());
        gsonBuilder.registerTypeAdapter(CustomerAlertData.class,new CustomerAlertData.CustomerAlertDataAdapter());
        gsonBuilder.registerTypeAdapter(TransactionDataObject.class,new TransactionDataObject.TransactionDataObjectAdapter());
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        // Get engine and increase yaz.
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        AdminData data = engineManager.endRewind();
        System.out.print("we go back to real Yaz");


        String dataToJson = gsonBuilder.create().toJson(data, AdminData.class);
        System.out.println(dataToJson);
        response.getOutputStream().println(dataToJson);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
