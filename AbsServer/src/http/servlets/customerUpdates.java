package http.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dto.objectdata.*;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet(name = "Customer Data Update Servlet", urlPatterns = "/customerUpdates")
public class customerUpdates extends HttpServlet {

    private final GsonBuilder gsonBuilder;

    public customerUpdates() {
        // Build gsonBuilder and add Serialize Adapters of necessary objects.
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LoanDataObject.class,new LoanDataObject.LoanDataObjectAdapter());
        gsonBuilder.registerTypeAdapter(CustomerOperationData.class,new CustomerOperationData.CustomerOperationDataAdapter());
        gsonBuilder.registerTypeAdapter(CustomerAlertData.class,new CustomerAlertData.CustomerAlertDataAdapter());
        gsonBuilder.registerTypeAdapter(TransactionDataObject.class,new TransactionDataObject.TransactionDataObjectAdapter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        // Get engine and fetch all data to send it by HTTP Request to customer.
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());

        // Get relevant user from CustomerDataObject map.
        String customerName = request.getParameter("customerName"); // Get customer name from request.
        CustomerDataObject curCustomer = engineManager.getCustomerByName(customerName); // Get customer object according to his name.

        // If received customer name doesn't exist.
        if(curCustomer == null) {
            response.getOutputStream().println("Error: " + customerName + " cannot be found.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {

            final Gson gson = gsonBuilder.create(); // Create Gson object with classes adapters.
            String customerJsonString = gson.toJson(curCustomer, CustomerDataObject.class);

            // Write json string as response to client.
            response.getOutputStream().println(customerJsonString);
            response.setStatus(HttpServletResponse.SC_OK); // Set request status as success.
        }

    }
}
