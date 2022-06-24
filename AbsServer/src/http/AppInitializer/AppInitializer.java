package http.AppInitializer;

import http.utils.ServletUtils;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener // Call first when tomcat load.
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // When tomcat run for the first time.
        ServletContextListener.super.contextInitialized(sce);

        // Initialize both Bank and User management systems.
        ServletUtils.getBankManager(sce.getServletContext());
        ServletUtils.getUserManager(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Tomcat shutdown proccess.
        ServletContextListener.super.contextDestroyed(sce);
    }
}
