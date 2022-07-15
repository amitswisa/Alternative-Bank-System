package http.servlets;

import abs.BankCustomer;
import com.google.gson.Gson;
import dto.JSON.InvestmentData;
import dto.infodata.DataTransferObject;
import dto.objectdata.CustomerDataObject;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Make Investment Servlet", urlPatterns = "/makeInvestment")
public class MakeInvestmentServlet extends HttpServlet {

    private Gson gson;

    public MakeInvestmentServlet() {
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext()); // Get engine from servlet context.
        InvestmentData investmentData = gson.fromJson(request.getParameter("investmentData"), InvestmentData.class);  // Parse information to relevant object.

        CustomerDataObject investor = engineManager.getCustomerByName(investmentData.getInvestorName());

        // Validation of investor.
        if(investor == null)
        {
            response.getOutputStream().print("Investor " + investmentData.getInvestorName() + " doesnt exist.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {

            try {
                String processResponse = engineManager.makeInvestments(investmentData);
                response.getOutputStream().print(processResponse);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (DataTransferObject e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

    }
}
