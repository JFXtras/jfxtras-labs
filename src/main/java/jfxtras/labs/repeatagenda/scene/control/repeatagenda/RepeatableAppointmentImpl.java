package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentGroupImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointmentImplBase;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/**
 * Example RepeatableAppointment implementation that includes some I/O methods
 * 
 * @author David Bal
 *
 */
public class RepeatableAppointmentImpl extends RepeatableAppointmentImplBase<RepeatableAppointmentImpl> implements RepeatableAppointment {

//    /** WholeDay: */
//    public BooleanProperty wholeDayProperty() { return wholeDayObjectProperty; }
//    final private BooleanProperty wholeDayObjectProperty = new SimpleBooleanProperty(this, "wholeDay", false);
//    public Boolean isWholeDay() { return wholeDayObjectProperty.getValue(); }
//    public void setWholeDay(Boolean value) { wholeDayObjectProperty.setValue(value); }
//    public MyAppointment withWholeDay(Boolean value) { setWholeDay(value); return this; } 
//    
//    /** Summary: */
//    public StringProperty summaryProperty() { return summaryObjectProperty; }
//    final private StringProperty summaryObjectProperty = new SimpleStringProperty(this, "summary", "");
//    public String getSummary() { return summaryObjectProperty.getValue(); }
//    public void setSummary(String value) { summaryObjectProperty.setValue(value); }
//    public MyAppointment withSummary(String value) { setSummary(value); return this; } 
//    
//    /** Description: */
//    public StringProperty descriptionProperty() { return descriptionObjectProperty; }
//    final private StringProperty descriptionObjectProperty = new SimpleStringProperty(this, "description", "");
//    public String getDescription() { return descriptionObjectProperty.getValue(); }
//    public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
//    public MyAppointment withDescription(String value) { setDescription(value); return this; } 
//    
      /** AppointmentGroupIndex: */
    private int appointmentGroupIndex = 0; // only used privately for I/O - later matched up to an appointmentGroup
    int getAppointmentGroupIndex() { return appointmentGroupIndex; }
    private void setAppointmentGroupIndex(Integer value) { appointmentGroupIndex = value; }
//
//    /** AppointmentGroup: */
//    public ObjectProperty<AppointmentGroup> appointmentGroupProperty() { return appointmentGroupObjectProperty; }
//    final private ObjectProperty<AppointmentGroup> appointmentGroupObjectProperty = new SimpleObjectProperty<AppointmentGroup>(this, "appointmentGroup");
//    public AppointmentGroup getAppointmentGroup() { return appointmentGroupObjectProperty.getValue(); }
//    public void setAppointmentGroup(AppointmentGroup value) { appointmentGroupObjectProperty.setValue(value); }
//    public MyAppointment withAppointmentGroup(AppointmentGroup value) { setAppointmentGroup(value); return this; }
//    public void assignAppointmentGroup(ObservableList<AppointmentGroup> appointmentGroups) { setAppointmentGroup(appointmentGroups.get(appointmentGroupIndex));  }
//   
        
    // TODO - REPALCE WITH UID - from iCalendar
    private static int nextKey = 0;
    private static Map<Integer, RepeatImpl> repeatIntegerKeyMap = new HashMap<Integer, RepeatImpl>(); // private map of repeats used to match Repeat objects to appointments
    /** create map of Repeat objects and repeat keys.  Its used to find Repeat objects to attach to Appointment objects.
     * Only used when setting up appointments from file */
    public static void setupRepeats(Set<Repeat> set)
    {
        Set<RepeatImpl> myRepeats
            = set.stream().map(a -> (RepeatImpl) a).collect(Collectors.toSet());
        repeatIntegerKeyMap = myRepeats.stream()
                           .collect(Collectors.toMap(a -> a.getKey(), a -> a));
    }
    private Repeat myRepeat;

    /** Unique appointment key */
    private Integer key;
    public Integer getKey() { return key; }
    public void setKey(Integer value) { key = value; }
    public boolean hasKey() { return key != null; }
    public RepeatableAppointmentImpl withKey(Integer value) { setKey(value); return this; }
    
    /** StudentKeys: */
    final private ObservableList<Integer> studentKeys = FXCollections.observableArrayList();
    public List<Integer> getStudentKeys() { return studentKeys; }
    public void setStudentKeys(List<Integer> value) { studentKeys.setAll(value); }
    public RepeatableAppointmentImpl withStudentKeys(List<Integer> value) { setStudentKeys(value); return this; }
    public ObservableList<Integer> studentKeysProperty() { return studentKeys; }

//    /** StaffKeys: */
//  private ObservableList<Integer> staffKeys = FXCollections.observableArrayList();
//  public ObservableList<Integer> getStaffKeys() { return staffKeys; }
//  public void setStaffKeys(List<Integer> value) { staffKeys.setAll(value); }
//  public T withStaffKeys(List<Integer> value) { setStaffKeys(value); return (T)this; }
////  public ObservableList<Integer> staffKeysProperty() { return staffKeys; }
//
//  /** StyleKey: */
//  public IntegerProperty styleKeyProperty() { return styleKeyProperty; }
//  final private IntegerProperty styleKeyProperty = new SimpleIntegerProperty(this, "styleKey", -1);
//  public Integer getStyleKey() { return styleKeyProperty.getValue(); }
//  public void setStyleKey(Integer value) { styleKeyProperty.setValue(value); }
//  public T withStyleKey(Integer value) { setStyleKey(value); return (T)this; }
//    
//    /** Location: */
//    public IntegerProperty locationKeyProperty() { return locationKeyProperty; }
//    final private IntegerProperty locationKeyProperty = new SimpleIntegerProperty(this, "locationKey", -1);
//    public Integer getLocationKey() { return locationKeyProperty.getValue(); }
//    public void setLocationKey(Integer value) { locationKeyProperty.setValue(value); }
//    public T withLocationKey(Integer value) { setLocationKey(value); return (T)this; } 
    
    /** RepeatKey: only used privately */
    private Integer repeatKey;
    private Integer getRepeatKey() { return repeatKey; }
    private void setRepeatKey(Integer value) { repeatKey = value; }
    private boolean hasRepeatKey() { return getRepeatKey() != null; }
    
//    /** Repeat rules, null if an individual appointment */
//    private Repeat repeat;
//    public void setRepeat(Repeat repeat) { this.repeat = repeat; }
//    public Repeat getRepeat() { return repeat; }
//    public boolean hasRepeat() { return repeat != null; }
//    public MyAppointment withRepeat(Repeat value) { setRepeat(value); return this; }
//
//    /**
//     * true = a temporary appointment created by a repeat rule
//     * false = a permanent appointment stored on disk
//     */
//    final private BooleanProperty repeatMade = new SimpleBooleanProperty(this, "repeatMade", false);
//    public BooleanProperty repeatMadeProperty() { return repeatMade; }
//    public boolean isRepeatMade() { return repeatMade.getValue(); }
//    public void setRepeatMade(boolean b) {repeatMade.set(b); }
//    public MyAppointment withRepeatMade(boolean b) {repeatMade.set(b); return this; }
    
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
    
    @Override
    public boolean equals(Object obj) {
        RepeatableAppointmentImpl testObj = (RepeatableAppointmentImpl) obj;
        
        System.out.println( "myappointment equals " +          getEndLocalDateTime() + " " + (testObj.getEndLocalDateTime())
            + " " + getEndLocalDateTime().equals(testObj.getEndLocalDateTime())
            + " " + getStartLocalDateTime().equals(testObj.getStartLocalDateTime()));
        
        return super.equals(obj)
            && getStartLocalDateTime().equals(testObj.getStartLocalDateTime())
            && getEndLocalDateTime().equals(testObj.getEndLocalDateTime())
            && getStartLocalDateTime().equals(testObj.getStartLocalDateTime());
    }
    
    @Override
    public boolean repeatFieldsEquals(Object obj) {
//        System.out.println("MyAppointment repeatFieldsEquals " );
        RepeatableAppointmentImpl testObj = (RepeatableAppointmentImpl) obj;
        boolean studentKeysEquals = (getStudentKeys() == null)
                ? (testObj.getStudentKeys() == null) : getStudentKeys().equals(testObj.getStudentKeys());
        return super.equals(obj) && studentKeysEquals;
    }
    
    //    /** Location: */
//    // I'M NOT USING THESE
//    public ObjectProperty<String> locationProperty() { return locationObjectProperty; }
//    final private ObjectProperty<String> locationObjectProperty = new SimpleObjectProperty<String>(this, "location");
//    public String getLocation() { return locationObjectProperty.getValue(); }
//    public void setLocation(String value) { locationObjectProperty.setValue(value); }
//    public MyAppointment withLocation(String value) { setLocation(value); return this; } 
    
    public RepeatableAppointmentImpl() { } // use factory to make new objects
    
    /**
     * Copy constructor
     * 
     * @param appointment
     */
    public RepeatableAppointmentImpl(RepeatableAppointment appointment)
    {
//        setRepeat(RepeatFactory.newRepeat(appointment.getRepeat()));
//        System.out.println("repeataptimpl getAppts " + appointment.getRepeat().getAppointmentData());
        setRepeat(new RepeatImpl(appointment.getRepeat()));
//        MyRepeat newRepeat = RepeatFactory.newRepeat(repeatMap.get(appointment));
//        repeatMap.put(this, newRepeat);
        appointment.copyInto(this);
    }
    
    
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
        myElement.setAttribute("repeatKey", (getRepeat() == null) ? "" : ((RepeatImpl) getRepeat()).getKey().toString());
//        myElement.setAttribute("repeatKey", (repeat == null) ? "" : ((MyRepeat) repeat).getKey().toString());
        final String s = getStudentKeys().stream()
                                         .map(a -> a.toString())
                                         .collect(Collectors.joining(" "));
        myElement.setAttribute("studentKeys", s);
        
        myElement.setAttribute(endLocalDateTimeProperty().getName(), DataUtilities.myFormatLocalDateTime(getEndLocalDateTime()));
        myElement.setAttribute(startLocalDateTimeProperty().getName(), DataUtilities.myFormatLocalDateTime(getStartLocalDateTime()));
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
        Map<String, String> rootAttributes = DataUtilities.getAttributes(doc.getFirstChild(), "repeatRules");
        List<Integer> keys = DataUtilities.myGetList(rootAttributes, "keys", "");
        Iterator<Integer> keyIterator = keys.iterator();
        
        NodeList appointmentNodeList = doc.getElementsByTagName("appointment");
        for (int appointmentNodeCounter=0; appointmentNodeCounter < appointmentNodeList.getLength(); appointmentNodeCounter++)
        {
            Node appointmentNode = appointmentNodeList.item(appointmentNodeCounter);
            if (appointmentNode.hasAttributes())
            {
                Integer expectedKey = keyIterator.next();
                appointmentAttributes = (HashMap<String, String>) DataUtilities.getAttributes(appointmentNode, "appointment");
                String appointmentName = DataUtilities.myGet(appointmentAttributes, "summary", file.toString());
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
        setDescription(DataUtilities.myGet(appointmentAttributes, "description", errorMessage));
        setAppointmentGroupIndex(Integer.parseInt(DataUtilities.myGet(appointmentAttributes, "groupIndex", errorMessage)));
//System.out.println("getAppointmentGroupIndex " + getAppointmentGroupIndex());
        //        setLocationKey(Integer.parseInt(DataUtilities.myGet(appointmentAttributes, "locationKey", errorMessage)));
//        setStaffKeys(DataUtilities.myGetList(appointmentAttributes, "staffKeys", errorMessage));
//        setStyleKey(Integer.parseInt( DataUtilities.myGet(appointmentAttributes, "styleKey", errorMessage)));
        setSummary( DataUtilities.myGet(appointmentAttributes, "summary", errorMessage));
        setWholeDay(DataUtilities.myParseBoolean(DataUtilities.myGet(appointmentAttributes, "wholeDay", errorMessage)));
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
  
        setRepeatKey(DataUtilities.myParseInt(DataUtilities.myGet(appointmentAttributes, "repeatKey", errorMessage)));
        setKey(Integer.parseInt(DataUtilities.myGet(appointmentAttributes, "key", errorMessage)));
        if (! (getKey() == expectedKey)) {
//            Main.log.log(Level.WARNING, "Appointment key does not match expected key. Appointment key = " + getKey()
//                    + " Expected appointment key = " + expectedKey + ". Using expected appointment key.", new IllegalArgumentException());
        }
        nextKey = Math.max(nextKey, getKey()) + 1;
        if (hasRepeatKey()) this.myRepeat = (repeatIntegerKeyMap.get(getRepeatKey()));
//        if (hasRepeatKey()) setRepeat(repeatMap.get(getRepeatKey()));
        setStudentKeys(DataUtilities.myGetList(appointmentAttributes, "studentKeys", errorMessage));

        
      setEndLocalDateTime(LocalDateTime.parse(DataUtilities.myGet(appointmentAttributes,endLocalDateTime.getName(), errorMessage), Settings.DATE_FORMAT_AGENDA));
      setStartLocalDateTime(LocalDateTime.parse( DataUtilities.myGet(appointmentAttributes, startLocalDateTime.getName(), errorMessage), Settings.DATE_FORMAT_AGENDA));
      return this;
    }
    @Override
    public RepeatableAppointment copyNonDateFieldsInto(RepeatableAppointment appointment) {
        System.out.println("appointment 5" + appointment);
        List<Integer> s = ((RepeatableAppointmentImpl) appointment).getStudentKeys();
        getStudentKeys().addAll(s);
        return RepeatableAppointment.super.copyNonDateFieldsInto(appointment);
    }
    
    
    // TODO - DO SOMETHING ABOUT THE BELOW STUBS
    @Override
    public Calendar getStartTime() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setStartTime(Calendar c) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public Calendar getEndTime() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setEndTime(Calendar c) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public ZonedDateTime getStartZonedDateTime() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setStartZonedDateTime(ZonedDateTime v) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public ZonedDateTime getEndZonedDateTime() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setEndZonedDateTime(ZonedDateTime v) {
        // TODO Auto-generated method stub
        
    }

}
