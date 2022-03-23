package manager;

import java.io.File;
import java.util.Scanner;

public class XMLManager extends Throwable {

    private final Scanner scanner;
    private File currentWorkFile = null;

    public XMLManager() {
        scanner = new Scanner(System.in);
    }

    public String loadXMLfile() {

        // Get xml file path from user.
        System.out.print("Enter full path of xml file: ");
        String filePathString = scanner.nextLine();

        // File validation.
        File tempFileHolder = new File(filePathString); // Init temp file var.

        if(!tempFileHolder.exists())  // Check if file is exist.
            return "Error: file doesnt exist.";

        if(!this.isFileHaveXmlExtension(tempFileHolder))  // Check if file extension is ".xml"
            return "Error: file must have .xml extension.";

        // TODO - XML file content validation.

        this.currentWorkFile = tempFileHolder; // Save file instance after validation checks.
        return "File loaded successfully!";
    }

    // Check if file extension exist and equal to ".xml"
    private boolean isFileHaveXmlExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");

        // return boolean answer -> is extension equals ".xml".
        return fileName.substring(dotIndex + 1).equals("xml");
    }

}
