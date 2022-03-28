package engine_managers;

import dto.AbsDataTransferObject;
import org.xml.sax.SAXException;
import xmlgenerated.AbsCategories;
import xmlgenerated.AbsDescriptor;
import xmlgenerated.ObjectFactory;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.URL;
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
    public AbsDataTransferObject loadXMLfile(String filePathString) throws JAXBException, FileNotFoundException {

        // File validation.
        File tempFileHolder = new File(filePathString); // Init temp file var.

        if(!tempFileHolder.exists())  // Check if file is exist.
            return new AbsDataTransferObject("Error: file doesnt exist.");

        if(!this.isFileHaveXmlExtension(tempFileHolder))  // Check if file extension is ".xml"
            return new AbsDataTransferObject("Error: file must have .xml extension.");

        // XML file content validation.
        AbsDataTransferObject validData = this.validateXML(tempFileHolder);
        if(validData != null)
            return validData;

        this.currentWorkFile = tempFileHolder; // Save file instance after validation checks.
        return new AbsDataTransferObject("File is valid, loaded successfully!");
    }

    // Check if file extension exist and equal to ".xml"
    private boolean isFileHaveXmlExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");

        // return boolean answer -> is extension equals ".xml".
        return fileName.substring(dotIndex + 1).equals("xml");
    }

    // validateXML function -> gets xml file and validate its content.
    private AbsDataTransferObject validateXML(File f_xml) {

        try {
            // Load xml file stream and unmarshall it.
            InputStream xml_InfoStream = new FileInputStream(f_xml);
            AbsDescriptor toValid = deserializeFrom(xml_InfoStream);

            // Get AbsDescriptor objects to valid.
            /*AbsCategories absCat = toValid.getAbsCategories();
            for(String node : absCat.getAbsCategory()) {
                System.out.println(node);
            }*/


        } catch(FileNotFoundException | JAXBException e) {
            return new AbsDataTransferObject("Error occurred while trying deserialize xml file.");
        }

        return null;
    }

    // deserializeFrom function -> gets InputStream and return Object from xml file.
    public static AbsDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("xmlgenerated"); // Create new instance of schema's generated classes.
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (AbsDescriptor) unmarshaller.unmarshal(in); // return unmarshalled data.
    }

}
