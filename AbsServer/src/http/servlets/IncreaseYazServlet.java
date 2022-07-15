package http.servlets;

import abs.BankSystem;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "Increase Yaz Servlet", urlPatterns = "/increaseYaz")
public class IncreaseYazServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        // Get engine and increase yaz.
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        engineManager.increaseYazDate();
        System.out.print("Yaz increased, current yaz: " + BankSystem.getCurrentYaz());

        response.getOutputStream().print(BankSystem.getCurrentYaz());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
