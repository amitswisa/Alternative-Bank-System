package engine.xmlmanager;

import abs.BankSystem;
import dto.infodata.DataTransferObject;
import xmlgenerated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class XMLManager {

    // Empty constructor -> set currentWorkFile to null.
    public XMLManager() {

    }

    // Validates given xml file and load if valid.
    public AbsDescriptor loadXMLfile(String fileContent, String fileName) throws DataTransferObject {

       /* if(!tempFileHolder.exists())  // Check if file is exist.
            throw new DataTransferObject("Error: file doesnt exist.", BankSystem.getCurrentYaz());*/

        if(!this.isFileHaveXmlExtension(fileName))  // Check if file extension is ".xml"
            throw new DataTransferObject("Error: file must have .xml extension.", BankSystem.getCurrentYaz());

        AbsDescriptor resValue = this.validateXML(fileContent); // XML file content validation.

        return resValue;
    }

    // Check if file extension exist and equal to ".xml"
    private boolean isFileHaveXmlExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");

        // return boolean answer -> is extension equals ".xml".
        return fileName.substring(dotIndex + 1).equals("xml");
    }

    // validateXML function -> gets xml file and validate its content.
    private AbsDescriptor validateXML(String f_xml) throws DataTransferObject {
        AbsDescriptor toValidate = null;
        try {  // Load xml file stream and unmarshall it.
            toValidate = this.deserializeFrom(new StringReader(f_xml)); // deserialize from xml file to object.
        } catch(JAXBException e) {
            throw new DataTransferObject("Error occurred while trying deserialize xml file content.", BankSystem.getCurrentYaz());
        }

        // Adding categories name into Map Object.
        AbsCategories absCats = toValidate.getAbsCategories();
        Map<String, Boolean> catNameStringMap = new HashMap<>();
        for(String cat : absCats.getAbsCategory()) {
            catNameStringMap.put(cat,true);
        }

        // Validate that each loan have existing category.
        for(AbsLoan loan : toValidate.getAbsLoans().getAbsLoan()) {
            // Check if loan has an existing category name.
            if(!catNameStringMap.containsKey(loan.getAbsCategory()))
                throw new DataTransferObject("Loan Error: given category doesn't exist in loan with following id: " + loan.getId(), BankSystem.getCurrentYaz());

            // Validate payment.
            float isValidPayment = (float)loan.getAbsTotalYazTime() / (float)loan.getAbsPaysEveryYaz();
            if(isValidPayment != (int)isValidPayment)
                throw new DataTransferObject("Loan Error: loan "+loan.getId()+" payment isn't divided equally.", BankSystem.getCurrentYaz());
        }

        return toValidate;
    }

    // deserializeFrom function -> gets InputStream and return Object from xml file.
    private AbsDescriptor deserializeFrom(StringReader in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("xmlgenerated"); // Create new instance of schema's generated classes.
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (AbsDescriptor) unmarshaller.unmarshal(in); // return unmarshalled data.
    }

}
