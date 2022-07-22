package http.servlets;

import abs.BankSystem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.JSON.AdminData;
import dto.objectdata.LoanDataObject;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "Increase Yaz Servlet", urlPatterns = "/increaseYaz")
public class IncreaseYazServlet extends HttpServlet {

    private final Gson gson;

    public IncreaseYazServlet() {
        gson = new GsonBuilder().registerTypeAdapter(AdminData.class, new AdminData.AdminDataObjectAdapter()).create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        // Get relevant user from CustomerDataObject map.
        String json = request.getParameter("adminData"); // Get customer name from request.
        AdminData reqValue = gson.fromJson(json, AdminData.class);

        // Get engine and increase yaz.
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        engineManager.increaseYazDate(reqValue);
        System.out.print("Yaz increased, current yaz: " + BankSystem.getCurrentYaz());

        int resInt = (BankSystem.getCurrentYaz() == BankSystem.getAdminYazTime())
                            ? BankSystem.getCurrentYaz() : BankSystem.getAdminYazTime();

        response.getOutputStream().print(resInt);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
