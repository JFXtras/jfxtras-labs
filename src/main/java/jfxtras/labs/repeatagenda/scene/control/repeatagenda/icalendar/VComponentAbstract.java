package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;

/**
 * Top-level calendar component in iCalendar as defined in section 3.6 of RFC 5545 for 
 * an event (VEvent), a to-do (VTodo) or a journal entry (VJournal).
 * The three other components defined in section 3.6 are time zone
 * information, free/busy time information, and alarm are not subclasses of VComponent
 * and are not implemented here.
 * 
 * The implementation status of the following calendar components is below.
 * VEvent - yes
 * VTodo - no
 * VJournal - no
 *
 * The implementation status of component properties:
       3.8.1.  Descriptive Component Properties  . . . . . . . . . .  81
         3.8.1.1.  Attachment  . . . . . . . . . . . . . . . . . . .  81 - NO
         3.8.1.2.  Categories  . . . . . . . . . . . . . . . . . . .  82 - Yes
         3.8.1.3.  Classification  . . . . . . . . . . . . . . . . .  83 - NO
         3.8.1.4.  Comment . . . . . . . . . . . . . . . . . . . . .  84 - Yes
         3.8.1.5.  Description . . . . . . . . . . . . . . . . . . .  85 - Yes (in VEvent)
         3.8.1.6.  Geographic Position . . . . . . . . . . . . . . .  87 - NO
         3.8.1.7.  Location  . . . . . . . . . . . . . . . . . . . .  88 - Yes (in VEvent)
         3.8.1.8.  Percent Complete  . . . . . . . . . . . . . . . .  89 - NO
         3.8.1.9.  Priority  . . . . . . . . . . . . . . . . . . . .  90 - NO
         3.8.1.10. Resources . . . . . . . . . . . . . . . . . . . .  92 - NO
         3.8.1.11. Status  . . . . . . . . . . . . . . . . . . . . .  93 - NO
         3.8.1.12. Summary . . . . . . . . . . . . . . . . . . . . .  94 - Yes
       3.8.2.  Date and Time Component Properties  . . . . . . . . .  95
         3.8.2.1.  Date-Time Completed . . . . . . . . . . . . . . .  95 - NO
         3.8.2.2.  Date-Time End . . . . . . . . . . . . . . . . . .  96 - Yes
         3.8.2.3.  Date-Time Due . . . . . . . . . . . . . . . . . .  97 - NO
         3.8.2.4.  Date-Time Start . . . . . . . . . . . . . . . . .  99 - Yes
         3.8.2.5.  Duration  . . . . . . . . . . . . . . . . . . . . 100 - Yes
         3.8.2.6.  Free/Busy Time  . . . . . . . . . . . . . . . . . 101 - NO
         3.8.2.7.  Time Transparency . . . . . . . . . . . . . . . . 102 - NO
       3.8.3.  Time Zone Component Properties  . . . . . . . . . . . 103 - NO
         3.8.3.1.  Time Zone Identifier  . . . . . . . . . . . . . . 103 - NO
         3.8.3.2.  Time Zone Name  . . . . . . . . . . . . . . . . . 105 - NO
         3.8.3.3.  Time Zone Offset From . . . . . . . . . . . . . . 106 - NO
         3.8.3.4.  Time Zone Offset To . . . . . . . . . . . . . . . 106 - NO
         3.8.3.5.  Time Zone URL . . . . . . . . . . . . . . . . . . 107 - NO
       3.8.4.  Relationship Component Properties . . . . . . . . . . 108
         3.8.4.1.  Attendee  . . . . . . . . . . . . . . . . . . . . 108 - NO
         3.8.4.2.  Contact . . . . . . . . . . . . . . . . . . . . . 111 - NO
         3.8.4.3.  Organizer . . . . . . . . . . . . . . . . . . . . 113 - NO
         3.8.4.4.  Recurrence ID . . . . . . . . . . . . . . . . . . 114 - TODO
         3.8.4.5.  Related To  . . . . . . . . . . . . . . . . . . . 117 - NO
         3.8.4.6.  Uniform Resource Locator  . . . . . . . . . . . . 118 - NO
         3.8.4.7.  Unique Identifier . . . . . . . . . . . . . . . . 119 - Yes
       3.8.5.  Recurrence Component Properties . . . . . . . . . . . 120
         3.8.5.1.  Exception Date-Times  . . . . . . . . . . . . . . 120 - Yes
         3.8.5.2.  Recurrence Date-Times . . . . . . . . . . . . . . 122 - Yes
         3.8.5.3.  Recurrence Rule . . . . . . . . . . . . . . . . . 124 - Yes, in RRule class
       3.8.6.  Alarm Component Properties  . . . . . . . . . . . . . 134
         3.8.6.1.  Action  . . . . . . . . . . . . . . . . . . . . . 134 - NO
         3.8.6.2.  Repeat Count  . . . . . . . . . . . . . . . . . . 135 - NO
         3.8.6.3.  Trigger . . . . . . . . . . . . . . . . . . . . . 135 - NO
       3.8.7.  Change Management Component Properties  . . . . . . . 138
         3.8.7.1.  Date-Time Created . . . . . . . . . . . . . . . . 138 - Yes
         3.8.7.2.  Date-Time Stamp . . . . . . . . . . . . . . . . . 139 - Yes
         3.8.7.3.  Last Modified . . . . . . . . . . . . . . . . . . 140 - Yes
         3.8.7.4.  Sequence Number . . . . . . . . . . . . . . . . . 141 - TODO
       3.8.8.  Miscellaneous Component Properties  . . . . . . . . . 142
         3.8.8.1.  IANA Properties . . . . . . . . . . . . . . . . . 142 - NO
         3.8.8.2.  Non-Standard Properties . . . . . . . . . . . . . 142 - can't be implemented here.  must be in implementing class
         3.8.8.3.  Request Status  . . . . . . . . . . . . . . . . . 144 - NO
         
Alphabetical list of elements for VComponent (some not implemented)
ATTACH - not implemented
ATTENDEE - not implemented
CATEGORIES - yes
CLASS - not implemented
COMMENT - yes
CONTACT - not implemented
CREATED - yes
DTSTAMP - yes
DTSTART - yes
EXDATE - yes
IANA-PROP - not implemented
LAST-MOD - yes
ORGANIZER - not implemented
RDATE - yes
RECURID - TODO
RELATED - not implemented
RESOURCES - not implemented
RRULE - yes
RSTATUS - not implemented
SEQ - TODO
STATUS - not implemented
SUMMARY - yes
UID - yes
URL - not implemented
X-PROP - can't be implemented here.  must be in implementing class

Limitations: COMMENT, EXDATE, RDATE can only exist once per calendar component.  According
to iCalendar a number of properties, including those three, can exist more than once.  Fixing
this limitation is a future goal. - I plan on fixing this problem by combining multiple
instances into one property internally.

 * 
 * @author David Bal
 * @param <U>
 * @see VEvent
 *
 */
public abstract class VComponentAbstract<T> implements VComponent<T>
{
    public String name() { return null; } // not called for abstract class

    /**
     * CATEGORIES: RFC 5545 iCalendar 3.8.1.12. page 81
     * This property defines the categories for a calendar component.
     * Example:
     * CATEGORIES:APPOINTMENT,EDUCATION
     * CATEGORIES:MEETING
     */
    public StringProperty categoriesProperty() { return categoriesProperty; }
    final private StringProperty categoriesProperty = new SimpleStringProperty(this, "CATEGORIES");
    public String getCategories() { return categoriesProperty.get(); }
    public void setCategories(String value) { categoriesProperty.set(value); }
//    public T withCategories(String value) { setCategories(value); return (T)this; }
    
    /**
     *  COMMENT: RFC 5545 iCalendar 3.8.1.12. page 83
     * This property specifies non-processing information intended
      to provide a comment to the calendar user.
     * Example:
     * COMMENT:The meeting really needs to include both ourselves
         and the customer. We can't hold this meeting without them.
         As a matter of fact\, the venue for the meeting ought to be at
         their site. - - John
     * */
    public StringProperty commentProperty() { return commentProperty; }
    final private StringProperty commentProperty = new SimpleStringProperty(this, "COMMENT");
    public String getComment() { return commentProperty.get(); }
    public void setComment(String value) { commentProperty.set(value); }
//    public VEvent withComment(String value) { setComment(value); return this; }

    /**
     * CREATED: Date-Time Created, from RFC 5545 iCalendar 3.8.7.1 page 136
     * This property specifies the date and time that the calendar information was created.
     * This is analogous to the creation date and time for a file in the file system.
     */
    final private ObjectProperty<LocalDateTime> dateTimeCreated = new SimpleObjectProperty<>(this, "CREATED");
    public ObjectProperty<LocalDateTime> dateTimeCreatedProperty() { return dateTimeCreated; }
    public LocalDateTime getDateTimeCreated() { return dateTimeCreated.get(); }
    public void setDateTimeCreated(LocalDateTime dtCreated) { this.dateTimeCreated.set(dtCreated); }
    
    /**
     * DTSTAMP: Date-Time Stamp, from RFC 5545 iCalendar 3.8.7.2 page 137
     * This property specifies the date and time that the instance of the
     * iCalendar object was created
     */
    final private ObjectProperty<LocalDateTime> dateTimeStamp = new SimpleObjectProperty<>(this, "DTSTAMP");
    public ObjectProperty<LocalDateTime> dateTimeStampProperty() { return dateTimeStamp; }
    public LocalDateTime getDateTimeStamp() { return dateTimeStamp.get(); }
    public void setDateTimeStamp(LocalDateTime dtStamp) { this.dateTimeStamp.set(dtStamp); }
    
    /**
     * DTSTART: Date-Time Start, from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.
     * Can contain either a LocalDate (DATE) or LocalDateTime (DATE-TIME)
     * @SEE VDateTime
     */
    public ObjectProperty<Temporal> dateTimeStartProperty() { return dateTimeStart; }
    final private ObjectProperty<Temporal> dateTimeStart = new SimpleObjectProperty<>(this, "DTSTART");
    public LocalDateTime dateTimeStartToLocalDateTime() { return VComponent.localDateTimeFromTemporal(dateTimeStart.get()); }
    @Override public Temporal getDateTimeStart() { return dateTimeStart.get(); }
    @Override public void setDateTimeStart(Temporal dtStart) { VComponent.super.setDateTimeStart(dtStart); dateTimeStart.set(dtStart); }
    boolean isDateTimeStartWholeDay() { return dateTimeStart.get() instanceof LocalDate; }
    
    /**
     * EXDATE: Set of date/times exceptions for recurring events, to-dos, journal entries.
     * 3.8.5.1, RFC 5545 iCalendar
     * Is rarely used, so employs lazy initialization.
     */
    public ObjectProperty<EXDate> exDateProperty()
    {
        if (exDate == null) exDate = new SimpleObjectProperty<>(this, "EXDATE", _exDate);
        return exDate;
    }
    private ObjectProperty<EXDate> exDate;
    private EXDate _exDate;
    public EXDate getExDate() { return (exDate == null) ? _exDate : exDate.getValue(); }
    public void setExDate(EXDate exDate)
    {
        if (this.exDate == null)
        {
            _exDate = exDate;
        } else
        {
            this.exDate.set(exDate);
        }
    }

    /**
     * LAST-MODIFIED: Date-Time Last Modified, from RFC 5545 iCalendar 3.8.7.3 page 138
     * This property specifies the date and time that the information associated with
     * the calendar component was last revised.
     */
    final private ObjectProperty<LocalDateTime> dateTimeLastModified = new SimpleObjectProperty<LocalDateTime>(this, "LAST-MODIFIED");
    public ObjectProperty<LocalDateTime> dateTimeLastModifiedProperty() { return dateTimeLastModified; }
    public LocalDateTime getDateTimeLastModified() { return dateTimeLastModified.getValue(); }
    public void setDateTimeLastModified(LocalDateTime dtLastModified) { this.dateTimeLastModified.set(dtLastModified); }
    
    /**
     * LOCATION: RFC 5545 iCalendar 3.8.1.12. page 87
     * This property defines the intended venue for the activity
     * defined by a calendar component.
     * Example:
     * LOCATION:Conference Room - F123\, Bldg. 002
     */
    public StringProperty locationProperty() { return locationProperty; }
    final private StringProperty locationProperty = new SimpleStringProperty(this, "LOCATION");
    public String getLocation() { return locationProperty.getValue(); }
    public void setLocation(String value) { locationProperty.setValue(value); }
//    public T withLocation(String value) { setLocation(value); return (T)this; }

    /**
     * RDATE: Set of date/times for recurring events, to-dos, journal entries.
     * 3.8.5.2, RFC 5545 iCalendar
    */
    // Is rarely used, so employs lazy initialization.
    public ObjectProperty<RDate> rDateProperty()
    {
        if (rDate == null) rDate = new SimpleObjectProperty<RDate>(this, "RDATE", _rDate);
        return rDate;
    }
    private ObjectProperty<RDate> rDate;
    private RDate _rDate;
    public RDate getRDate() { return (rDate == null) ? _rDate : rDate.getValue(); }
    public void setRDate(RDate rDate)
    {
        if (this.rDate == null)
        {
            _rDate = rDate;
        } else
        {
            this.rDate.set(rDate);
        }
    }

    /**
     * RECURRENCE-ID: Date-Time recurrence, from RFC 5545 iCalendar 3.8.4.4 page 112
     * The property value is the original value of the "DTSTART" property of the 
     * recurrence instance.
     */
    // Is rarely used, so lazy initialization is used.
    public ObjectProperty<LocalDateTime> dateTimeRecurrenceProperty()
    {
        if (dateTimeRecurrence == null) dateTimeRecurrence = new SimpleObjectProperty<LocalDateTime>(this, "RECURRENCE-ID", _dateTimeRecurrence);
        return dateTimeRecurrence;
    }
    private ObjectProperty<LocalDateTime> dateTimeRecurrence;
    private LocalDateTime _dateTimeRecurrence;
    public LocalDateTime getDateTimeRecurrence()
    {
        return (dateTimeRecurrence == null) ? _dateTimeRecurrence : dateTimeRecurrence.getValue();
    }
    public void setDateTimeRecurrence(LocalDateTime dtRecurrence)
    {
        if (dateTimeRecurrence == null)
        {
            _dateTimeRecurrence = dtRecurrence;
        } else
        {
            dateTimeRecurrence.set(dtRecurrence);
        }
    }
    
    /**
     * Recurrence Rule, RRULE, as defined in RFC 5545 iCalendar 3.8.5.3, page 122.
     * If event is not repeating value is null
     */
    public ObjectProperty<RRule> rRuleProperty() { return rRule; }
    final private ObjectProperty<RRule> rRule = new SimpleObjectProperty<RRule>(this, "RRULE");
    public RRule getRRule() { return rRule.get(); }
    public void setRRule(RRule rRule) { this.rRule.set(rRule); }
//    public void setRRule(String rRuleString) { setRRule(RRule.parseRRule(rRuleString)); }
    
    /**
     *  SUMMARY: RFC 5545 iCalendar 3.8.1.12. page 83
     * This property defines a short summary or subject for the calendar component 
     * Example:
     * SUMMARY:Department Party
     * */
    public StringProperty summaryProperty() { return summaryProperty; }
    final private StringProperty summaryProperty = new SimpleStringProperty(this, "SUMMARY");
    public String getSummary() { return summaryProperty.getValue(); }
    public void setSummary(String value) { summaryProperty.setValue(value); }
//    public T withSummary(String value) { setSummary(value); return (T)this; } 
    
    /**
     * Unique identifier, UID as defined by RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * Included is an example UID generator.  Other UID generators can be provided by
     * setting the UID callback.
     */
    public StringProperty uniqueIdentifierProperty() { return uniqueIdentifier; }
    final private StringProperty uniqueIdentifier = new SimpleStringProperty(this, "UID");
    public String getUniqueIdentifier() { return uniqueIdentifier.getValue(); }
    public void setUniqueIdentifier(String s) { uniqueIdentifier.set(s); }
//    public T withUniqueIdentifier(String uid) { setUniqueIdentifier(uid); return (T)this; }

    /** Callback for creating unique uid values - not implemented - TODO */
    public Callback<Void, String> getUidGeneratorCallback() { return uidGeneratorCallback; }
    private static Integer nextKey = 0;
    private Callback<Void, String> uidGeneratorCallback = (Void) ->
    { // default UID generator callback
        String dateTime = VDateTime.DATE_TIME_FORMATTER.format(LocalDateTime.now());
        String domain = "jfxtras.org";
        return dateTime + "-" + nextKey++ + domain;
    };
    public void setUidGeneratorCallback(Callback<Void, String> uidCallback) { this.uidGeneratorCallback = uidCallback; }
    
    /**
     * Start of range for which recurrence instances are generated.  Should match the dates displayed on the calendar.
     * This is not a part of an iCalendar VEvent
     */
    public LocalDateTime getDateTimeRangeStart() { return dateTimeRangeStart; }
    private LocalDateTime dateTimeRangeStart;
    public void setDateTimeRangeStart(LocalDateTime dateTimeRangeStart) { this.dateTimeRangeStart = dateTimeRangeStart; }
    
    /**
     * End of range for which recurrence instances are generated.  Should match the dates displayed on the calendar.
     */
    public LocalDateTime getDateTimeRangeEnd() { return dateTimeRangeEnd; }
    private LocalDateTime dateTimeRangeEnd;
    public void setDateTimeRangeEnd(LocalDateTime dateTimeRangeEnd) { this.dateTimeRangeEnd = dateTimeRangeEnd; }
    
    /** Method to convert DTSTART or DTEND to LocalDateTime
     * Currently ignores time zones */
    public LocalDateTime iCalendarDateTimeToLocalDateTime(String dt)
    {
        Pattern p = Pattern.compile("([0-9]+)");
        Matcher m = p.matcher(dt);
        List<String> tokens = new ArrayList<String>();
        while (m.find())
        {
            String token = m.group(0);
            tokens.add(token);
        }
        LocalDate date;
        if (tokens.size() > 0)
        {
            String dateToken = tokens.get(0);
//            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(VDateTime.datePattern);
            date = LocalDate.parse(dateToken, VDateTime.DATE_FORMATTER);
        } else throw new IllegalArgumentException("Invalid Date-Time string: " + dt);           
        if (tokens.size() == 2)
        { // find date if another token is available
            String timeToken = tokens.get(1);
//            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timePattern);
            LocalTime time = LocalTime.parse(timeToken, VDateTime.TIME_FORMATTER);
            return LocalDateTime.of(date, time);
        }
        return date.atStartOfDay();
    }

    // CONSTRUCTORS
    /** Copy constructor */
    public VComponentAbstract(VComponentAbstract<T> vcomponent)
    {
        copy(vcomponent, this);
    }
    
    public VComponentAbstract()
    {
    }

    /** Deep copy all fields from source to destination */
    private static void copy(VComponentAbstract<?> source, VComponentAbstract<?> destination)
    {
        destination.setCategories(source.getCategories());
        destination.setComment(source.getComment());
        destination.setDateTimeStamp(source.getDateTimeStamp());
        destination.setDateTimeStart(source.getDateTimeStart());
        destination.setLocation(source.getLocation());
        destination.setSummary(source.getSummary());
        destination.setUniqueIdentifier(source.getUniqueIdentifier());
        if (source.getRRule() != null)
        {
            if (destination.getRRule() == null)
            { // make new RRule object for destination if necessary
                try {
                    RRule newRRule = source.getRRule().getClass().newInstance();
                    destination.setRRule(newRRule);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            source.getRRule().copyTo(destination.getRRule());
        }
        if (source.getExDate() != null)
        {
            if (destination.getExDate() == null)
            { // make new EXDate object for destination if necessary
                try {
                    EXDate newEXDate = source.getExDate().getClass().newInstance();
                    destination.setExDate(newEXDate);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            source.getExDate().copyTo(destination.getExDate());
        }
        if (source.getRDate() != null)
        {
            if (destination.getRDate() == null)
            { // make new RDate object for destination if necessary
                try {
                    RDate newRDate = source.getRDate().getClass().newInstance();
                    destination.setRDate(newRDate);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            source.getRDate().copyTo(destination.getRDate());
        }
    }
    
    /** Deep copy all fields from source to destination */
    @Override
    public void copyTo(VComponent<T> destination)
    {
        copy(this, (VComponentAbstract<T>) destination);
    }

    @Override
    public boolean equals(Object obj)
    {
        @SuppressWarnings("unchecked")
        VComponentAbstract<T> testObj = (VComponentAbstract<T>) obj;

        boolean categoriesEquals = (getCategories() == null) ?
                (testObj.getCategories() == null) : getCategories().equals(testObj.getCategories());
        boolean commentEquals = (getComment() == null) ?
                (testObj.getComment() == null) : getComment().equals(testObj.getComment());
        boolean dateTimeStampEquals = (getDateTimeStamp() == null) ?
                (testObj.getDateTimeStamp() == null) : getDateTimeStamp().equals(testObj.getDateTimeStamp());
        boolean dateTimeStartsEquals = (getDateTimeStart() == null) ?
                (testObj.getDateTimeStart() == null) : getDateTimeStart().equals(testObj.getDateTimeStart());
        boolean locationEquals = (getLocation() == null) ?
                (testObj.getLocation() == null) : getLocation().equals(testObj.getLocation());
        boolean summaryEquals = (getSummary() == null) ?
                (testObj.getSummary() == null) : getSummary().equals(testObj.getSummary());
        boolean uniqueIdentifierEquals = (getUniqueIdentifier() == null) ?
                (testObj.getUniqueIdentifier() == null) : getUniqueIdentifier().equals(testObj.getUniqueIdentifier());
        boolean rruleEquals = (getRRule() == null) ?
                (testObj.getRRule() == null) : getRRule().equals(testObj.getRRule());
        boolean eXDatesEquals = (getExDate() == null) ?
                (testObj.getExDate() == null) : getExDate().equals(testObj.getExDate());
        boolean rDatesEquals = (getRDate() == null) ?
                (testObj.getRDate() == null) : getRDate().equals(testObj.getRDate());
        System.out.println("Vcomponent equals: " + categoriesEquals + " " + commentEquals + " " + dateTimeStampEquals + " " + dateTimeStartsEquals + " " 
                + locationEquals + " " + summaryEquals + " " + uniqueIdentifierEquals + " " + rruleEquals + " " + eXDatesEquals + " " + rDatesEquals);
        return categoriesEquals && commentEquals && dateTimeStampEquals && dateTimeStartsEquals && locationEquals
                && summaryEquals && uniqueIdentifierEquals && rruleEquals && eXDatesEquals && rDatesEquals;
    }

    /** Make map of properties and string values for toString method in subclasses (like VEvent) */
    @SuppressWarnings("rawtypes")
    Map<Property, String> makePropertiesMap()
    {
        Map<Property, String> properties = new HashMap<Property, String>();

        if (getCategories() != null) properties.put(categoriesProperty(), getCategories().toString());
        if (getComment() != null) properties.put(commentProperty(), getComment().toString());
        if (getDateTimeCreated() != null) properties.put(dateTimeCreatedProperty(), VDateTime.DATE_TIME_FORMATTER.format(getDateTimeCreated()));
        properties.put(dateTimeStampProperty(), VDateTime.DATE_TIME_FORMATTER.format(getDateTimeStamp())); // required property
        if (getDateTimeRecurrence() != null) properties.put(dateTimeRecurrenceProperty(), VDateTime.DATE_TIME_FORMATTER.format(getDateTimeRecurrence()));
        if (getDateTimeStart() != null) properties.put(dateTimeStartProperty(), VComponent.temporalToString(getDateTimeStart()));
        if (getDateTimeLastModified() != null) properties.put(dateTimeLastModifiedProperty(), VDateTime.DATE_TIME_FORMATTER.format(getDateTimeLastModified()));
        if (getExDate() != null) properties.put(exDateProperty(), getExDate().toString());
        if (getLocation() != null) properties.put(locationProperty(), getLocation().toString());
        if (getRDate() != null) properties.put(rDateProperty(), getRDate().toString());
        if (getRRule() != null) properties.put(rRuleProperty(), getRRule().toString());
        if (getSummary() != null) properties.put(summaryProperty(), getSummary().toString());
        properties.put(uniqueIdentifierProperty(), getUniqueIdentifier()); // required property
        return properties;
    }
    
    /**
     * Checks to see if object contains required properties.  Returns empty string if it is
     * valid.  Returns string of errors if not valid..
     */
    public String validityCheck()
    {
        StringBuilder errorsBuilder = new StringBuilder();
        if (getDateTimeStart() == null) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  DTSTART must not be null.");
        if (getDateTimeStamp() == null) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  DTSTAMP must not be null.");
        if (getUniqueIdentifier() == null) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  UID must not be null.");
        return errorsBuilder.toString();
    }
    
    /** Convert a list of strings containing properties of a iCalendar component and
     * populate its properties.  Used to make a new object from a List<String>.
     * @param <U>
     * @param s
     */
    protected static <U> VComponentAbstract<U> parseVComponent(VComponentAbstract<U> vComponent, List<String> strings)
    {
        Iterator<String> stringsIterator = strings.iterator();
        while (stringsIterator.hasNext())
        {
            String line = stringsIterator.next();
            String property = line.substring(0, line.indexOf(":"));
            String value = line.substring(line.indexOf(":") + 1).trim();
            if (property.equals(vComponent.categoriesProperty().getName()))
            { // CATEGORIES
//                System.out.println("found categories:" + value);
                vComponent.setCategories(value);
                stringsIterator.remove();
            } else if (property.equals(vComponent.dateTimeCreatedProperty().getName()))
            { // CREATED
                LocalDateTime dateTime = LocalDateTime.parse(value,VDateTime.DATE_TIME_FORMATTER);
                vComponent.setDateTimeCreated(dateTime);
                stringsIterator.remove();
            } else if (property.equals(vComponent.dateTimeStampProperty().getName()))
            { // DTSTAMP
                LocalDateTime dateTime = LocalDateTime.parse(value,VDateTime.DATE_TIME_FORMATTER);
                vComponent.setDateTimeStamp(dateTime);
                stringsIterator.remove();
            } else if (property.equals(vComponent.dateTimeStartProperty().getName()))
            { // DTSTART
//                VDateTime dateTime = VDateTime.parseString(value);
                Temporal dateTime = VComponent.parseTemporal(value);
                vComponent.setDateTimeStart(dateTime);
                stringsIterator.remove();
            } else if (property.equals(vComponent.exDateProperty().getName()))
            { // EXDATE
                Collection<Temporal> dateTimeCollection = RecurrenceComponentAbstract.parseDates(value);
                if (vComponent.getExDate() == null)
                {
                    vComponent.setExDate(new EXDate());
                }                  
                vComponent.getExDate().getTemporals().addAll(dateTimeCollection);
                stringsIterator.remove();
            } else if (property.equals(vComponent.dateTimeLastModifiedProperty().getName()))
            { // LAST-MODIFIED
                LocalDateTime dateTime = LocalDateTime.parse(value,VDateTime.DATE_TIME_FORMATTER);
                vComponent.setDateTimeLastModified(dateTime);
                stringsIterator.remove();
            } else if (property.equals(vComponent.rDateProperty().getName()))
            { // RDATE
                Collection<Temporal> dateTimeCollection = RecurrenceComponentAbstract.parseDates(value);
                if (vComponent.getRDate() == null)
                {
                    vComponent.setRDate(new RDate());
                }                  
                vComponent.getRDate().getTemporals().addAll(dateTimeCollection);
                stringsIterator.remove();

            } else if (property.equals(vComponent.rRuleProperty().getName()))
            { // RRULE
                vComponent.setRRule(RRule.parseRRule(value));
                stringsIterator.remove();
            } else if (property.equals(vComponent.summaryProperty().getName()))
            { // SUMMARY
                vComponent.setSummary(value);
                stringsIterator.remove();
            } else if (property.equals(vComponent.uniqueIdentifierProperty().getName()))
            { // UID
                vComponent.setUniqueIdentifier(value);
                stringsIterator.remove();
            }
        }
        return vComponent;
    }
        
    /** Stream of date/times that indicate the start of the event(s).
     * For a VEvent without RRULE the stream will contain only one date/time element.
     * A VEvent with a RRULE the stream contains more than one date/time element.  It will be infinite 
     * if COUNT or UNTIL is not present.  The stream has an end when COUNT or UNTIL condition is met.
     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. */
    @Override
    public Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        Stream<LocalDateTime> stream1;
        if (getRRule() == null)
        { // if individual event
            stream1 = Arrays.asList(dateTimeStartToLocalDateTime())
                    .stream()
                    .filter(d -> ! d.isBefore(startDateTime));
////            if (! startDateTime.isBefore(getDateTimeStart()))
//            if (! startDateTime.isBefore(getDateTimeStart().getLocalDateTime()))
//            {
////                stream1 = Arrays.asList(getDateTimeStart()).stream();
//                stream1 = Arrays.asList(getDateTimeStart().getLocalDateTime()).stream();
//            } else
//            { // if dateTimeStart is before startDateTime
////                System.out.println("empty stream");
//                stream1 = new ArrayList<LocalDateTime>().stream(); // empty stream
//            }
        } else
        { // if has recurrence rule
            stream1 = getRRule().stream(startDateTime);
        }
        Stream<LocalDateTime> stream2 = (getRDate() == null) ? stream1 : getRDate().stream(stream1, startDateTime); // add recurrence list
        Stream<LocalDateTime> stream3 = (getExDate() == null) ? stream2 : getExDate().stream(stream2, startDateTime); // remove exceptions
        return stream3;
    }

}
