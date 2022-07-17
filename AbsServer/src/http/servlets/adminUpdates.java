package http.servlets;

import abs.BankSystem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.JSON.AdminData;
import dto.objectdata.*;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Admin Updates Servlet", urlPatterns = "/adminUpdates")
public class adminUpdates extends HttpServlet {

    private final GsonBuilder gsonBuilder;

    public adminUpdates() {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LoanDataObject.class,new LoanDataObject.LoanDataObjectAdapter());
        gsonBuilder.registerTypeAdapter(CustomerOperationData.class,new CustomerOperationData.CustomerOperationDataAdapter());
        gsonBuilder.registerTypeAdapter(CustomerAlertData.class,new CustomerAlertData.CustomerAlertDataAdapter());
        gsonBuilder.registerTypeAdapter(TransactionDataObject.class,new TransactionDataObject.TransactionDataObjectAdapter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        // Get engine from servletContext.
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());

        // Create transfer data object.
        AdminData data = new AdminData(engineManager.getAllCustomerData(), engineManager.getAllLoansData(), BankSystem.getCurrentYaz());

        String dataToJson = gsonBuilder.create().toJson(data, AdminData.class);
        System.out.println(dataToJson);
        response.getOutputStream().println(dataToJson);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
