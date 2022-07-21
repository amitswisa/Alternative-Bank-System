package http.servlets;


import abs.BankSystem;
import com.google.gson.Gson;
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

@WebServlet(name = "Rewind Yaz Servlet", urlPatterns = "/rewindYaz")
public class RewindYazServlet extends HttpServlet {

    private final Gson gson;
    private final GsonBuilder gsonBuilder;

    public RewindYazServlet() {
        gson = new GsonBuilder().registerTypeAdapter(AdminData.class, new AdminData.AdminDataObjectAdapter()).create();
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LoanDataObject.class,new LoanDataObject.LoanDataObjectAdapter());
        gsonBuilder.registerTypeAdapter(CustomerOperationData.class,new CustomerOperationData.CustomerOperationDataAdapter());
        gsonBuilder.registerTypeAdapter(CustomerAlertData.class,new CustomerAlertData.CustomerAlertDataAdapter());
        gsonBuilder.registerTypeAdapter(TransactionDataObject.class,new TransactionDataObject.TransactionDataObjectAdapter());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        // Get relevant user from CustomerDataObject map.
        Integer choosenYaz = Integer.parseInt(request.getParameter("choosenYaz")); // Get customer name from request.

        // Get relevant user from CustomerDataObject map.
        String json = request.getParameter("adminData"); // Get customer name from request.
        AdminData reqValue = gson.fromJson(json, AdminData.class);

        // Get engine and increase yaz.
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        AdminData data = engineManager.rewindYazDate(choosenYaz, reqValue);
        System.out.print("we go back to Yaz: " + BankSystem.getCurrentYaz());

        String dataToJson = gsonBuilder.create().toJson(data, AdminData.class);
        System.out.println(dataToJson);
        response.getOutputStream().println(dataToJson);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
