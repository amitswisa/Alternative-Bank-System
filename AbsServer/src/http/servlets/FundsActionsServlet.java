package http.servlets;

import dto.infodata.DataTransferObject;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Deposit/Withdraw Servlet", urlPatterns = "/FundsActions")
public class FundsActionsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        try {
            // Try parse 'depositAmount' string to a number.
            int moneyAmount = Integer.parseInt(request.getParameter("moneyAmount"));

            // if is a negative number.
            if(moneyAmount <= 0) {
                throw new NumberFormatException("Please enter positive number.");
            }

            // Handle money and return success status.
            boolean actionResult = false;
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());

            if(request.getParameter("action").equals("Deposit"))
                actionResult = engineManager.depositMoney(request.getParameter("customerName"), moneyAmount);
            else {
                engineManager.withdrawMoney(request.getParameter("customerName"), moneyAmount);
                actionResult = true;
            }

            if(actionResult) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getOutputStream().println(request.getParameter("action") + " " + moneyAmount + " for customer: " + request.getParameter("customerName"));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getOutputStream().println("Customer " + request.getParameter("customerName") + " doesnt exist.");
            }

        } catch(NumberFormatException | DataTransferObject nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getOutputStream().println(nfe.getMessage());
        }

    }

}
