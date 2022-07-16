package http.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.infodata.DataTransferObject;
import dto.objectdata.LoanDataObject;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Pay All Debt Servlet", urlPatterns = "/payalldebt")
public class PayAllDebt extends HttpServlet {
    private final Gson gson;

    public PayAllDebt() {
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        String loan = req.getParameter("loan"); // Get customer name from request.
        LoanDataObject loanParsed = gson.fromJson(loan, LoanDataObject.class);

        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Set request status as success.
        if(loanParsed != null) {
            try {
                List<LoanDataObject> inv = new ArrayList<>(1);
                inv.add(loanParsed);

                engineManager.handleCustomerPayAllDebt(inv);
                resp.setStatus(HttpServletResponse.SC_OK); // Set request status as success.
                resp.getOutputStream().print("Loan has been paid, status changed to finished!");
            } catch (DataTransferObject e) {
                resp.getOutputStream().print(e.getMessage());
            }
        }
    }
}
