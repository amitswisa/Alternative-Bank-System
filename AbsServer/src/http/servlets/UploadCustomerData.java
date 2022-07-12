package http.servlets;

import com.google.gson.*;
import dto.infodata.DataTransferObject;
import dto.infodata.XmlFileData;
import dto.objectdata.LoanDataObject;
import engine.EngineManager;
import http.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import xmlgenerated.AbsDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "Upload Customer Data Servlet", urlPatterns = "/UploadCustomerData")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadCustomerData extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain"); // Support response with utf-8 format.

        PrintWriter out = response.getWriter(); // Write response data.
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext()); // Get engine from servlet context.

        // Get content of file sent.
        Part part = request.getPart("fileContent"); // Get file part from request.
        InputStream is = part.getInputStream(); // Get file input stream -> file data.
        String fileData = new Scanner(is).useDelimiter("\\Z").next(); // Parse file input stream into a string.

        // Try parse, load and return xml data.
        try {
            // Get xml data.
            List<LoanDataObject> backRes = engineManager.loadXML(fileData,part.getSubmittedFileName(), request.getParameter("customerName"));
            response.setStatus(HttpServletResponse.SC_OK); // Set ok status to request.

            // Parse data into Json and return it to customer to load in client side.
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.registerTypeAdapter(LoanDataObject.class, new LoanDataObject.LoanDataObjectAdapter()).create();
            String myJsonObjects = gson.toJson(backRes);

            out.println(myJsonObjects);

        } catch (DataTransferObject e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            out.println(e.getMessage());
        }

    }

}
