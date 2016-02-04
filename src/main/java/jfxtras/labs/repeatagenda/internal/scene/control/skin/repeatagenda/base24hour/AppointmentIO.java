package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.IOUtilities;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

// TODO - OUT OF DATE - DOESN'T USE ICALENDAR

// TODO - FOLLOW OTHER DATA IO PATTERN - MARSHAL AND UNMARSHAL GETS NEW OBJECT
// readMap for reading whole map 
@Deprecated
public final class AppointmentIO {
    
    private AppointmentIO() {}

    private static Map<Integer, Integer> appointmentGroupCount = new HashMap<Integer, Integer>();
    

    
    public static void writeAppointmentGroups(Collection<AppointmentGroup> appointmentGroups
            , Path file)
    {
        // XML document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
//            Main.log.log(Level.SEVERE, "Can't build appointmentGroups factory" , e);
        }
        Document doc = builder.newDocument();

        Element rootElement = doc.createElement("appointments");
        doc.appendChild(rootElement);
        
        // Appointment Groups
        Element appointmentGroupElement = null;
            appointmentGroupElement = doc.createElement("appointmentGroups");
            StringBuilder appointmentGroupsSB = new StringBuilder();
        {
            for (AppointmentGroup myAppointmentGroup: appointmentGroups) {
                Element groupElement = doc.createElement("group");
                groupElement.setAttribute("name", myAppointmentGroup.getDescription());
                groupElement.setAttribute("style", myAppointmentGroup.getStyleClass());
                appointmentGroupElement.appendChild(groupElement);
            }
            appointmentGroupElement.setAttribute("AppointmentGroups", appointmentGroupsSB.toString().trim());
            rootElement.appendChild(appointmentGroupElement);
        }
        
        try {
            IOUtilities.writeDocument(doc, file);
        } catch (TransformerException e) {
//            Main.log.log(Level.SEVERE, "Can't write appointmentGroups file=" + file, e);
        }
    }
    
    public static ObservableList<AppointmentGroup> readAppointmentGroups(File file) throws ParserConfigurationException
    {
        ObservableList<AppointmentGroup> appointmentGroups = FXCollections.observableArrayList();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        try
        {
            Document doc = builder.parse(file);
            
            Map<String, String> groupAttributes;
            String errorMessage = "File: " + file;
            
            NodeList groupNodeList = doc.getElementsByTagName("group");
            for (int groupNodeCounter=0;
                    groupNodeCounter < groupNodeList.getLength();
                    groupNodeCounter++) {
                Node groupNode = groupNodeList.item(groupNodeCounter);
                if (groupNode.hasAttributes()) {
                    groupAttributes = IOUtilities.getAttributes(groupNode, "group");
                    
                    int myCount = (appointmentGroupCount.get(groupNodeCounter) == null)
                            ? 0 : appointmentGroupCount.get(groupNodeCounter);
    
//                    AppointmentGroupImpl aGroup = new AppointmentGroupImpl(null)
//                        .withDescription(IOUtilities.myGet(groupAttributes, "name", errorMessage))
//                        .withStyleClass(IOUtilities.myGet(groupAttributes, "style", errorMessage));
    //                    .withAppointmentCount(myCount);
//                    appointmentGroups.add(aGroup);
    
                }
            }
        } catch (SAXException | IOException e) {
//            System.out.println("Missing file: " + file.toString());
//          Main.log.log(Level.WARNING, "Missing file: " + inputFile.toString(), e);
        }
        return appointmentGroups;
        
    }


}
