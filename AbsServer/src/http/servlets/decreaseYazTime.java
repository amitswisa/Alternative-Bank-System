package http.servlets;

import abs.BankSystem;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Decrease Yaz Time Servlet", urlPatterns = "/DecreaseYaz")
public class decreaseYazTime extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        // Get engine and increase yaz.
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        engineManager.decreaseYaz();

        response.getOutputStream().print(BankSystem.getAdminYazTime());
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
