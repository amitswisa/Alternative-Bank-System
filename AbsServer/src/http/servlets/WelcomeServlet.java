package http.servlets;


import abs.BankManager;
import http.utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Welcome Servlet", urlPatterns = "/welcome")
public class WelcomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            BankManager bankManager = ServletUtils.getBankManager(getServletContext());
            response.getOutputStream().print("Welcome!");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }


}
