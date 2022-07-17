package http.servlets;

import abs.BankSystem;
import engine.EngineManager;
import http.constants.Constants;
import http.utils.ServletUtils;
import http.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;

import java.io.IOException;

import static http.constants.Constants.USERNAME;

@WebServlet(name = "Login Admin Servlet", urlPatterns = "/loginadmin")
public class LoginAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession == null) //user is not logged in yet
        {
            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter - not standard situation. it's a conflict
                // stands for conflict in server state
                response.getOutputStream().print("Error: Username field cant be empty.");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
            else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (userManager.isAdminExists()) {
                        String errorMessage = "Admin already exists.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else if(userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else {
                        // Get engine from servlet context.
                        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());

                        //add admin.
                        userManager.addAdmin(usernameFromParameter, engineManager);

                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet create a new one
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);

                        //redirect the request to the chat room - in order to actually change the URL
                        System.out.println("On login, request URI is: " + request.getRequestURI());

                        response.getOutputStream().print(BankSystem.getCurrentYaz());
                        response.setStatus(HttpServletResponse.SC_OK);

                    }
                }
            }
        }
        else
        {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().print("Welcome back, " + usernameFromSession + "!");
        }
    }
}
