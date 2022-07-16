package http.servlets;

import com.google.gson.Gson;
import dto.JSON.SystemUpdates;
import dto.objectdata.LoanDataObject;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@WebServlet(name = "Pay Loan Servlet", urlPatterns = "/payloan")
public class PayLoanServlet extends HttpServlet {

    private Gson gson;

    public PayLoanServlet() {
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/plain");
        resp.setStatus(HttpServletResponse.SC_CONFLICT); // Set request status as success.

        try {
            String loan = req.getParameter("loan"); // Get customer name from request.
            LoanDataObject loanParsed = gson.fromJson(loan, LoanDataObject.class);

            // Get engine from ServletContext.
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());

            if (loanParsed != null) {

                int money = Integer.parseInt(req.getParameter("investAmount")); // Get customer name from request.

                engineManager.handleCustomerLoansPayments(loanParsed, money);
                resp.getOutputStream().print("Loan paid!");
                resp.setStatus(HttpServletResponse.SC_OK); // Set request status as success.
            }

        } catch(NumberFormatException e) {
            resp.getOutputStream().print(e.getMessage());
        }
    }
}
