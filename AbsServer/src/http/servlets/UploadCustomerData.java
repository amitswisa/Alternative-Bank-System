package http.servlets;

import dto.infodata.DataTransferObject;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@WebServlet(name = "Upload Customer Data Servlet", urlPatterns = "/UploadCustomerData")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadCustomerData extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain"); // Support response with utf-8 format.

        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext()); // Get engine from servlet context.

        // Get content of file sent.
        Part part = request.getPart("fileContent"); // Get file part from request.
        InputStream is = part.getInputStream(); // Get file input stream -> file data.
        String fileData = new Scanner(is).useDelimiter("\\Z").next(); // Parse file input stream into a string.

        DataTransferObject backRes = engineManager.loadXML(fileData,part.getSubmittedFileName(), request.getParameter("customerName"));

        if(backRes.getMessage().equals("File loaded successfully!"))
            response.setStatus(HttpServletResponse.SC_OK);
        else
            response.setStatus(HttpServletResponse.SC_CONFLICT);

            response.getOutputStream().print(backRes.getMessage());
    }
}
