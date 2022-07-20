package http.servlets;

import dto.infodata.DataTransferObject;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Buy Loan Servlet", urlPatterns = "/BuyLoan")
public class BuyLoanServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        // Get engine and loan owner name.
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String loanOwner = request.getParameter("loanOwner");
        String sellerName = request.getParameter("sellerName");
        String buyerName = request.getParameter("buyerName");
        String loanName = request.getParameter("loanName");


        try {
            engineManager.handleLoanBuying(loanOwner, sellerName, buyerName, loanName);
        } catch(DataTransferObject e) {
            response.getOutputStream().print(e.getMessage());
        }

    }
}
