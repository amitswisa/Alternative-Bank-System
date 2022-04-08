package engine.xmlmanager;

import dto.DataTransferObject;
import xmlgenerated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class XMLManager {

    private File currentWorkFile;

    // Empty constructor -> set currentWorkFile to null.
    public XMLManager() {
        currentWorkFile = null;
    }

    // Validates given xml file and load if valid.
    public AbsDescriptor loadXMLfile(String filePathString) throws DataTransferObject {

        // File validation.
        File tempFileHolder = new File(filePathString); // Init temp file var.

        if(!tempFileHolder.exists())  // Check if file is exist.
            throw new DataTransferObject("Error: file doesnt exist.");

        if(!this.isFileHaveXmlExtension(tempFileHolder))  // Check if file extension is ".xml"
            throw new DataTransferObject("Error: file must have .xml extension.");

        AbsDescriptor resValue = this.validateXML(tempFileHolder); // XML file content validation.

        this.currentWorkFile = tempFileHolder; // Save file instance after validation checks.
        return resValue;
    }

    // Check if file extension exist and equal to ".xml"
    private boolean isFileHaveXmlExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");

        // return boolean answer -> is extension equals ".xml".
        return fileName.substring(dotIndex + 1).equals("xml");
    }

    // validateXML function -> gets xml file and validate its content.
    private AbsDescriptor validateXML(File f_xml) throws DataTransferObject {
        AbsDescriptor toValidate = null;
        try {  // Load xml file stream and unmarshall it.
            InputStream xml_InfoStream = new FileInputStream(f_xml); // open new file stream.
            toValidate = this.deserializeFrom(xml_InfoStream); // deserialize from xml file to object.
        } catch(FileNotFoundException | JAXBException e) {
            throw new DataTransferObject("Error occurred while trying deserialize xml file.");
        }

        // Adding categories name into Map Object.
        AbsCategories absCats = toValidate.getAbsCategories();
        Map<String, Boolean> catNameStringMap = new HashMap<>();
        for(String cat : absCats.getAbsCategory()) {
            catNameStringMap.put(cat,true);
        }

        // Validate that each Customer have different name.
        AbsCustomers absCusts = toValidate.getAbsCustomers();
        Map<String, Boolean> customersNameMap = new HashMap<>();
        for(AbsCustomer customer : absCusts.getAbsCustomer()) {
            if(customersNameMap.containsKey(customer.getName()))
                throw new DataTransferObject("Error: customer " + customer.getName() + "exists more then once.");

            customersNameMap.put(customer.getName(), true);
        }

        // Validate that each loan have existing category.
        for(AbsLoan loan : toValidate.getAbsLoans().getAbsLoan()) {
            // Check if loan has an existing category name.
            if(!catNameStringMap.containsKey(loan.getAbsCategory()))
                throw new DataTransferObject("Loan Error: given category doesn't exist in loan with following id: " + loan.getId());

            // Check if loan has an existing customer name.
            if(!customersNameMap.containsKey(loan.getAbsOwner()))
                throw new DataTransferObject("Loan Error: loan " + loan.getId() + " holds customer that doesnt exist.");

            // Validate payment.
            float isValidPayment = (float)loan.getAbsTotalYazTime() / (float)loan.getAbsPaysEveryYaz();
            if(isValidPayment != (int)isValidPayment)
                throw new DataTransferObject("Loan Error: loan "+loan.getId()+" payment isn't divided equally.");
        }

        return toValidate;
    }

    // deserializeFrom function -> gets InputStream and return Object from xml file.
    private AbsDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("xmlgenerated"); // Create new instance of schema's generated classes.
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (AbsDescriptor) unmarshaller.unmarshal(in); // return unmarshalled data.
    }

    public boolean isFileLoaded() {
        return (this.currentWorkFile != null);
    }
}
