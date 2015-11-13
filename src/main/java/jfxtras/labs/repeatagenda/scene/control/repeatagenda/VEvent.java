package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.RRule;

/**
 * Parent calendar component, VEVENT
 * Defined in RFC 5545 iCalendar 3.6.1, page 52.
 * 
 * @author David Bal
 *
 */
public class VEvent {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
   
    /**
     * DTSTART from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.
     */
    final private ObjectProperty<LocalDateTime> startLocalDateTime = new SimpleObjectProperty<LocalDateTime>();
    public ObjectProperty<LocalDateTime> startLocalDateTimeProperty() { return startLocalDateTime; }
    public LocalDateTime getStartLocalDateTime() { return startLocalDateTime.getValue(); }
    public void setStartLocalDate(LocalDateTime startDate) { this.startLocalDateTime.set(startDate); }
    public VEvent withStartLocalDate(LocalDateTime startDate) { setStartLocalDate(startDate); return this; }

    /** 
     * DURATION from RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Internally stored a seconds.  Can be set an an integer of seconds or a string as defined by iCalendar which is
     * converted to seconds.  This value is used exclusively internally.  Any specified DTEND is converted to 
     * durationInSeconds,
     * */
    final private ObjectProperty<Integer> durationInSeconds = new SimpleObjectProperty<Integer>(this, "durationProperty");
    public ObjectProperty<Integer> durationInSecondsProperty() { return durationInSeconds; }
    public Integer getDurationInSeconds() { return durationInSeconds.getValue(); }
    public void setDurationInSeconds(Integer value) { durationInSeconds.setValue(value); }
    public void setDurationInSeconds(String value)
    { // parse ISO.8601.2004 duration string into period of seconds (no support for Y (years) or M (months).
        int seconds = 0;
        Pattern p = Pattern.compile("([0-9]+)|([A-Z])");
        Matcher m = p.matcher(value);
        List<String> tokens = new ArrayList<String>();
        while (m.find())
        {
            String token = m.group(0);
            tokens.add(token);
        }
        Iterator<String> tokenIterator = tokens.iterator();
        String firstString = tokenIterator.next();
        if (! tokenIterator.hasNext() || (! firstString.equals("P"))) throw new InvalidParameterException("Invalid DURATION string (" + value + "). Must begin with a P");
        boolean timeFlag = false;
        while (tokenIterator.hasNext())
        {
            String token = tokenIterator.next();
            if (token.matches("\\d+"))
            { // first value is a number means I got a data element
                int n = Integer.parseInt(token);
                String time = tokenIterator.next();
                if (time.equals("W"))
                { // weeks
                    seconds += n * 7 * 24 * 60 * 60;
                } else if (time.equals("D"))
                { // days
                    seconds += n * 24 * 60 * 60;
                } else if (timeFlag && time.equals("H"))
                { // hours
                    seconds += n * 60 * 60;                    
                } else if (timeFlag && time.equals("M"))
                { // minutes
                    seconds += n * 60;                                        
                } else if (timeFlag && time.equals("S"))
                { // seconds
                    seconds += n;                    
                } else
                {
                    throw new InvalidParameterException("Invalid DURATION string time element (" + time + "). Must begin with a P");
                }
            } else if (token.equals("T")) timeFlag = true; // proceeding elements will be hour, minute or second
        }
        durationInSeconds.setValue(seconds);
    }
    public VEvent withDurationInSeconds(Integer value) { setDurationInSeconds(value); return this; } 
    public VEvent withDurationInSeconds(String value) { setDurationInSeconds(value); return this; } 
    
    /**
     * Unique identifier, UID as defined by RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * Included is an example UID generator.  Other UID generators can be provided by
     * setting the UID callback.
     */
    private String UID;
    public String getUID() { return UID; }
    public void setUID(String s) { UID = s; }
    private String UIDGenerator()
    {
        String dateTime = formatter.format(LocalDateTime.now());
        String keyString = getKey().toString();
        String domain = "jfxtras-agenda";
        return dateTime + keyString + domain;
    }
    
    // sequential int key part of UID
    private static Integer nextKey = 0;
    private Integer key;
    Integer getKey() { return key; }
    void setKey(Integer value) { key = value; } 

    
    private RRule rrule;
}
