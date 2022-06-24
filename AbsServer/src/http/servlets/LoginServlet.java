package http.servlets;

import http.constants.Constants;
import http.utils.SessionUtils;
import http.utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import users.UserManager;
import java.io.IOException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static http.constants.Constants.USERNAME;

@WebServlet(name = "Login Servlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

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
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else {
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter);
                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet create a new one
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);

                        //redirect the request to the chat room - in order to actually change the URL
                        System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.getOutputStream().print("Logged in successfully!");
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
