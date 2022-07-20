package http.servlets;

import abs.BankCustomer;
import dto.objectdata.CustomerDataObject;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Sell Loan Servlet", urlPatterns = "/SellLoan")
public class SellLoanServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        // Get engine and customer name.
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        CustomerDataObject customerName = engineManager.getCustomerByName(request.getParameter("customerName"));

        // Customer isnt exist.
        if(customerName == null)
        {
            response.getOutputStream().print("Customer doesnt exist.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            String loanName = request.getParameter("loanName");
            String sellerName = request.getParameter("sellerName");
            System.out.println("Seller name: " + sellerName);
            String ans = engineManager.changeLoanSellStatus(customerName.getName(), sellerName, loanName);
            response.getOutputStream().print(ans);
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }
}
