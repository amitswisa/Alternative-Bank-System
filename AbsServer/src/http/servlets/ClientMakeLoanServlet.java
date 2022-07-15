package http.servlets;

import abs.BankCustomer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.deploy.cache.JarSigningData;
import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerDataObject;
import dto.objectdata.LoanDataObject;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Client Make Loan Servlet", urlPatterns = "/ClientMakeLoanServlet")
public class ClientMakeLoanServlet extends HttpServlet {

    private final Gson gson;

    public ClientMakeLoanServlet() {
        gson = new GsonBuilder().registerTypeAdapter(LoanDataObject.class, new LoanDataObject.LoanDataObjectAdapter()).create();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        // Get engine and fetch all data to send it by HTTP Request to customer.
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());

        // Get relevant user from CustomerDataObject map.
        String json = request.getParameter("loanData"); // Get customer name from request.
        LoanDataObject reqValue = gson.fromJson(json, LoanDataObject.class);

        CustomerDataObject customer = engineManager.getCustomerByName(reqValue.getOwner());

        // If received customer name doesn't exist.
        if(customer == null) {
            response.getOutputStream().print("Error: " + reqValue.getOwner() + " cannot be found.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            boolean loanExist = engineManager.addLoan(customer, reqValue);
            if(!loanExist){
                response.getOutputStream().print("Loan " + reqValue.getLoanID() + " already exist.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else{
                response.getOutputStream().print("Loan added successfully!");
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }



    }
}
