package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentGroupImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointmentImplBase;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/**
 * Example LocalTime RepeatableAppointment implementation that includes some I/O methods
 * 
 * @author David Bal
 *
 */
public class RepeatableAppointmentImpl extends RepeatableAppointmentImplBase<RepeatableAppointmentImpl> implements RepeatableAppointment {
    
      /** AppointmentGroupIndex: */
    private int appointmentGroupIndex = 0; // only used privately for I/O - later matched up to an appointmentGroup
    int getAppointmentGroupIndex() { return appointmentGroupIndex; }
    private void setAppointmentGroupIndex(Integer value) { appointmentGroupIndex = value; }
        
    // TODO - REPALCE WITH UID - from iCalendar
    private static int nextKey = 0;

    private static int nextUID = 0;
    private static Map<String, Repeat> repeatUIDKeyMap = new HashMap<String, Repeat>(); // private map of repeats used to match Repeat objects to appointments
    /** create map of Repeat objects and repeat keys.  Its used to find Repeat objects to attach to Appointment objects.
     * Only used when setting up appointments from file */
    public static void setupRepeats(Set<Repeat> set)
    {
        Set<Repeat> myRepeats
            = set.stream().map(a -> (Repeat) a).collect(Collectors.toSet());
        repeatUIDKeyMap = myRepeats.stream()
                           .collect(Collectors.toMap(a -> a.getUID(), a -> a));
    }
    private Repeat myRepeat;

    /** Unique appointment key */
    private Integer key;
    public Integer getKey() { return key; }
    public void setKey(Integer value) { key = value; }
    public boolean hasKey() { return key != null; }
    public RepeatableAppointmentImpl withKey(Integer value) { setKey(value); return this; }
    
    /** Sample custom data field */
    public StringProperty customProperty() { return custom; }
    final private StringProperty custom = new SimpleStringProperty(this, "custom", "");
    public String getCustom() { return custom.get(); }
    public void setCustom(String s) { custom.set(s); }
    public RepeatableAppointmentImpl withCustom(String s) { setCustom(s); return this; }
    
    /** RepeatKey: only used privately */
    private Integer repeatKey;
    private Integer getRepeatKey() { return repeatKey; }
    private void setRepeatKey(Integer value) { repeatKey = value; }
    private boolean hasRepeatKey() { return getRepeatKey() != null; }
    
    private static Map<Integer, Integer> appointmentGroupCount = new HashMap<Integer, Integer>();
    
    /** StartDateTime: */
    public ObjectProperty<LocalDateTime> startLocalDateTimeProperty() { return startLocalDateTime; }
    final private ObjectProperty<LocalDateTime> startLocalDateTime = new SimpleObjectProperty<LocalDateTime>(this, "startDateTime");
    public LocalDateTime getStartLocalDateTime() { return startLocalDateTime.getValue(); }
    public void setStartLocalDateTime(LocalDateTime value) { startLocalDateTime.setValue(value); }
    public RepeatableAppointmentImpl withStartLocalDateTime(LocalDateTime value) { setStartLocalDateTime(value); return this; }
    
    /** EndDateTime: */
    public ObjectProperty<LocalDateTime> endLocalDateTimeProperty() { return endLocalDateTime; }
    protected final ObjectProperty<LocalDateTime> endLocalDateTime = new SimpleObjectProperty<LocalDateTime>(this, "endDateTime");
    public LocalDateTime getEndLocalDateTime() { return endLocalDateTime.getValue(); }
    public void setEndLocalDateTime(LocalDateTime value) { endLocalDateTime.setValue(value); }
    public RepeatableAppointmentImpl withEndLocalDateTime(LocalDateTime value) { setEndLocalDateTime(value); return this; } 

    // Constructors
    public RepeatableAppointmentImpl() { } // used by factory to make new objects
    
    /**
     * Copy constructor
     * 
     * @param appointment
     */
    public RepeatableAppointmentImpl(RepeatableAppointment appointment)
    {
        super(appointment);
        RepeatableAppointmentImpl appointment2 = (RepeatableAppointmentImpl) appointment;
        setCustom(appointment2.getCustom());
        setEndLocalDateTime(appointment.getEndLocalDateTime());
        setStartLocalDateTime(appointment.getStartLocalDateTime());
//        setRepeat(RepeatFactory.newRepeat(appointment.getRepeat()));
//        System.out.println("Repeat7 " + repeat);
//        Repeat repeat = repeatMap.get(appointment);
//        Repeat newRepeat = new RepeatImpl(repeat, RepeatableAppointmentImpl.class);
//        repeatMap.put(this, newRepeat);
//        Repeat repeat = appointment.getRepeat();
//        setRepeat(new RepeatImpl(repeat, RepeatableAppointmentImpl.class));
//        MyRepeat newRepeat = RepeatFactory.newRepeat(repeatMap.get(appointment));
//        repeatMap.put(this, newRepeat);
//        appointment.copyInto(this);
    }
    
    @Override
    public boolean equals(Object obj) {
        RepeatableAppointmentImpl testObj = (RepeatableAppointmentImpl) obj;
        
//        System.out.println("test obj3 " + obj);
        boolean startEquals = (getStartLocalDateTime() == null) ?
                (testObj.getStartLocalDateTime() == null) : getStartLocalDateTime().equals(testObj.getStartLocalDateTime());
        boolean endEquals = (getEndLocalDateTime() == null) ?
                (testObj.getEndLocalDateTime() == null) : getEndLocalDateTime().equals(testObj.getEndLocalDateTime());

        System.out.println( "RepeatableAppointmentImpl equals " + startEquals + " " + endEquals); // getStartLocalDateTime() + " " + testObj.getStartLocalDateTime() + getEndLocalDateTime() + " " + testObj.getEndLocalDateTime());
        
        return super.equals(obj) && startEquals && endEquals;
    }
    
//    @Override
//    public RepeatableAppointment copyNonDateFieldsInto(RepeatableAppointment appointment) {
//        System.out.println("appointment5 " + appointment);
//        RepeatableAppointmentImpl appointment2 = (RepeatableAppointmentImpl) appointment;
//        appointment2.setCustom(getCustom());
////        List<String> s = ((RepeatableAppointmentImpl) appointment2).getCustomList();
////        this.getCustomList().addAll(s);
//        return RepeatableAppointment.super.copyNonDateFieldsInto(appointment);
//    }

    
    
    public static void writeToFile(Collection<Appointment> appointments, Path file)
    {
        // XML document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
//            Main.log.log(Level.SEVERE, "Can't build appointmentMap factory" , e);
        }
        Document doc = builder.newDocument();

        Element rootElement = doc.createElement("appointments");
        doc.appendChild(rootElement);
        
        // Appointments
        System.out.println("writetoFile appointments " + appointments);
        for (Appointment myAppointment : appointments)
        {
//            if (myAppointment instanceof RepeatableAppointment)
//            {
                RepeatableAppointment repeatableAppointment = (RepeatableAppointment) myAppointment;
                if (repeatableAppointment.isRepeatMade()) continue; // skip appointments that are made by repeat rules
//            }
            Element appointmentElement = doc.createElement("appointment");
//            Repeat repeat = repeatMap.get(myAppointment);
            ((RepeatableAppointmentImpl) myAppointment).marshal(appointmentElement);
            rootElement.appendChild(appointmentElement);
        }

        Set<RepeatableAppointmentImpl> myAppointments = appointments
                .stream()
                .map(a -> (RepeatableAppointmentImpl) a)
                .collect(Collectors.toSet());
        String repeatKeys = myAppointments
                .stream()
                .filter(a -> ! a.isRepeatMade())
                .map(a -> a.getKey().toString()).collect(Collectors.joining(" "));
        rootElement.setAttribute("keys", repeatKeys);
        
        try {
            writeDocument(doc, file);
        } catch (TransformerException e) {
//            Main.log.log(Level.SEVERE, "Can't write appointmentMap file=" + file, e);
        }
    }
    
    /**
     * Writes a org.w3c.dom.Document to a output file.
     * 
     * @param doc
     * @param file
     * @throws TransformerException
     */
    public static void writeDocument(Document doc, Path file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        if (Settings.PRETTY_XML) {
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        }
        
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file.toFile());
        transformer.transform(source, result);
    }
    
    public Element marshal(Element myElement)
    {
//        super.marshal(myElement);
        myElement.setAttribute("wholeDay", Boolean.toString(isWholeDay()));
        myElement.setAttribute("summary", getSummary());
        myElement.setAttribute("description", getDescription());
        myElement.setAttribute("custom", getCustom());
//        myElement.setAttribute("locationKey", Integer.toString(getLocationKey()));

        // TODO - TRY TO REMOVE CAST TO AppointmentGroupImpl
        myElement.setAttribute("groupIndex", Integer.toString(((AppointmentGroupImpl) getAppointmentGroup()).getKey()));
//        final String s = getStaffKeys().stream()
//                                 .map(a -> a.toString())
//                                 .collect(Collectors.joining(" "));
//        myElement.setAttribute("staffKeys", s);
//        myElement.setAttribute("styleKey", DataUtilities.myInt2String(getStyleKey()));
        
        if (getKey() == null) setKey(nextKey++); // if it has no key (meaning its new) give it the next one
        myElement.setAttribute("key", getKey().toString());
        //TODO - FIND A WAY TO PUT REPEAT KEY HERE
//        myElement.setAttribute("repeatKey", (getRepeat() == null) ? "" : ((RepeatImpl) getRepeat()).getKey().toString());
//        myElement.setAttribute("repeatKey", (repeat == null) ? "" : ((MyRepeat) repeat).getKey().toString());
//        final String s = this.getCustomList().stream()
//                                         .map(a -> a.toString())
//                                         .collect(Collectors.joining(" "));
//        myElement.setAttribute("customList", s);
        
        myElement.setAttribute(endLocalDateTimeProperty().getName(), IOUtilities.myFormatLocalDateTime(getEndLocalDateTime()));
        myElement.setAttribute(startLocalDateTimeProperty().getName(), IOUtilities.myFormatLocalDateTime(getStartLocalDateTime()));
        return myElement;
    }
    
    
    /**
     * Read in an appointment map from a file.  Adds elements to appointments
     * 
     * @param file
     * @param appointmentGroups 
     * @param appointments
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Collection<RepeatableAppointment> readFromFile(File file
            , List<AppointmentGroup> appointmentGroups
            , Collection<RepeatableAppointment> appointments)
            throws ParserConfigurationException, SAXException 
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = null;
        try {
            doc = builder.parse(file);
        } catch (IOException e) {
//            Main.log.log(Level.WARNING, "Appointment file not found: " + file);
            return null;
        }
        
        Map<String, String> appointmentAttributes;
        Map<String, String> rootAttributes = IOUtilities.getAttributes(doc.getFirstChild(), "repeatRules");
        List<Integer> keys = IOUtilities.myGetList(rootAttributes, "keys", "");
        Iterator<Integer> keyIterator = keys.iterator();
        
        NodeList appointmentNodeList = doc.getElementsByTagName("appointment");
        for (int appointmentNodeCounter=0; appointmentNodeCounter < appointmentNodeList.getLength(); appointmentNodeCounter++)
        {
            Node appointmentNode = appointmentNodeList.item(appointmentNodeCounter);
            if (appointmentNode.hasAttributes())
            {
                Integer expectedKey = keyIterator.next();
                appointmentAttributes = (HashMap<String, String>) IOUtilities.getAttributes(appointmentNode, "appointment");
                String appointmentName = IOUtilities.myGet(appointmentAttributes, "summary", file.toString());
                String errorMessage = ", file: " + file + " summary: " + appointmentName;
                RepeatableAppointment anAppointment = new RepeatableAppointmentImpl()
                        .unmarshal(appointmentAttributes, expectedKey, errorMessage);
                Integer i = ((RepeatableAppointmentImpl) anAppointment).getAppointmentGroupIndex();
//              System.out.println("getAppointmentGroupIndex " + i);
                anAppointment.setAppointmentGroup(appointmentGroups.get(i));
                appointments.add(anAppointment);
            }
        }
        return appointments;
    }

    /**
     * Unmarshal only repeatable fields
     */
    public RepeatableAppointmentImpl unmarshal(Map<String, String> appointmentAttributes, String errorMessage)
    {
        setDescription(IOUtilities.myGet(appointmentAttributes, "description", errorMessage));
        setAppointmentGroupIndex(Integer.parseInt(IOUtilities.myGet(appointmentAttributes, "groupIndex", errorMessage)));
//System.out.println("getAppointmentGroupIndex " + getAppointmentGroupIndex());
        //        setLocationKey(Integer.parseInt(DataUtilities.myGet(appointmentAttributes, "locationKey", errorMessage)));
//        setStaffKeys(DataUtilities.myGetList(appointmentAttributes, "staffKeys", errorMessage));
//        setStyleKey(Integer.parseInt( DataUtilities.myGet(appointmentAttributes, "styleKey", errorMessage)));
        setSummary(IOUtilities.myGet(appointmentAttributes, "summary", errorMessage));
        setCustom(IOUtilities.myGet(appointmentAttributes, "custom", errorMessage));
        setWholeDay(IOUtilities.myParseBoolean(IOUtilities.myGet(appointmentAttributes, "wholeDay", errorMessage)));
//        System.out.println("groupIndex " + getAppointmentGroupIndex());

        return this;
    }
    
    
    /**
     * Unmarshalls a MyAppointment object from a Map<String,String> of appointmentAttributes
     * @param myKey 
     */
    public RepeatableAppointment unmarshal(Map<String, String> appointmentAttributes
//            , Map<Appointment, Repeat> repeatMap
            , Integer expectedKey
            , String errorMessage)
    {
        unmarshal(appointmentAttributes, errorMessage);
  
        setRepeatKey(IOUtilities.myParseInt(IOUtilities.myGet(appointmentAttributes, "repeatKey", errorMessage)));
        setKey(Integer.parseInt(IOUtilities.myGet(appointmentAttributes, "key", errorMessage)));
        if (! (getKey() == expectedKey)) {
//            Main.log.log(Level.WARNING, "Appointment key does not match expected key. Appointment key = " + getKey()
//                    + " Expected appointment key = " + expectedKey + ". Using expected appointment key.", new IllegalArgumentException());
        }
        nextKey = Math.max(nextKey, getKey()) + 1;
        if (hasRepeatKey()) this.myRepeat = (repeatUIDKeyMap.get(getRepeatKey()));
//        if (hasRepeatKey()) setRepeat(repeatMap.get(getRepeatKey()));
//        setCustomList(IOUtilities.myGetList(appointmentAttributes, "studentKeys", errorMessage));

        
      setEndLocalDateTime(LocalDateTime.parse(IOUtilities.myGet(appointmentAttributes,endLocalDateTime.getName(), errorMessage), Settings.DATE_FORMAT_AGENDA));
      setStartLocalDateTime(LocalDateTime.parse( IOUtilities.myGet(appointmentAttributes, startLocalDateTime.getName(), errorMessage), Settings.DATE_FORMAT_AGENDA));
      return this;
    }
    @Override
    public LocalDateTime getDTStamp() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setDTStamp(LocalDateTime dtStamp) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public LocalDateTime getCreated() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setCreated(LocalDateTime created) {
        // TODO Auto-generated method stub
        
    }

}
