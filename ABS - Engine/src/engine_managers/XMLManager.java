package engine_managers;

import dto.AbsDataTransferObject;

import java.io.File;
import java.util.Scanner;

public class XMLManager extends Throwable {

    private final Scanner scanner;
    private File currentWorkFile;

    // Empty constructor -> Initialize scanner and set currentWorkFile to null.
    public XMLManager() {
        scanner = new Scanner(System.in);
        currentWorkFile = null;
    }

    // Validates given xml file and load if valid.
    public AbsDataTransferObject loadXMLfile(String filePathString) {

        // File validation.
        File tempFileHolder = new File(filePathString); // Init temp file var.

        if(!tempFileHolder.exists())  // Check if file is exist.
            return new AbsDataTransferObject("Error: file doesnt exist.");

        if(!this.isFileHaveXmlExtension(tempFileHolder))  // Check if file extension is ".xml"
            return new AbsDataTransferObject("Error: file must have .xml extension.");

        // TODO - XML file content validation there is no referance to undefinded category.

        // TODO - XML file content validation There is no referance from a loan to an undefined customer.


        this.currentWorkFile = tempFileHolder; // Save file instance after validation checks.
        return new AbsDataTransferObject("File loaded successfully!");
    }

    // Check if file extension exist and equal to ".xml"
    private boolean isFileHaveXmlExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");

        // return boolean answer -> is extension equals ".xml".
        return fileName.substring(dotIndex + 1).equals("xml");
    }

}
