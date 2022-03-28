import org.xml.sax.SAXException;
import userinterface.UserInterface;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class main {
    public static void main(String[] args) throws JAXBException, FileNotFoundException {
        UserInterface ui = new UserInterface();
        ui.init(); // Print program menu.
    }
}