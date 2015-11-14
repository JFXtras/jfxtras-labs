package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.RRule;

/**
 * Parent calendar component, VEVENT
 * Defined in RFC 5545 iCalendar 3.6.1, page 52.
 * 
 * @author David Bal
 *
 */
public class VEvent {
   
    /**
     * Date-Time Start, DTSTART from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.
     */
    final private ObjectProperty<LocalDateTime> dateTimeStart = new SimpleObjectProperty<LocalDateTime>();
    public ObjectProperty<LocalDateTime> dateTimeStartProperty() { return dateTimeStart; }
    public LocalDateTime getDateTimeStart() { return dateTimeStart.getValue(); }
    public void setDateTimeStart(LocalDateTime dtStart) { this.dateTimeStart.set(dtStart); }
    // TODO - ADD SET METHOD FOR ICALENDAR COMPLIANT STRINGS
    public VEvent withDateTimeStart(LocalDateTime startDate) { setDateTimeStart(startDate); return this; }

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
    { // parse ISO.8601.2004 period string into period of seconds (no support for Y (years) or M (months).
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
     * Date-Time End, DTEND from RFC 5545 iCalendar 3.8.2.2 page 95
     * Specifies the date and time that a calendar component ends.
     * If entered this value is used to calculate the durationInSeconds, which is used
     * internally.
     */
    public void setDateTimeEnd(LocalDateTime dtEnd)
    {
        long seconds = ChronoUnit.SECONDS.between(getDateTimeStart(), dtEnd);
        setDurationInSeconds((int) seconds);
    }
    public void setDateTimeEnd(String dtEnd)
    {
        LocalDateTime dt = iCalendarDateTimeToLocalDateTime(dtEnd);
        setDateTimeEnd(dt);
    }
    public LocalDateTime getDateTimeEnd() { return getDateTimeStart().plusSeconds(getDurationInSeconds()); }

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
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
            date = LocalDate.parse(dateToken, dateFormatter);
        } else throw new InvalidParameterException("Invalid Date-Time string: " + dt);           
        if (tokens.size() == 2)
        { // find date if another token is available
            String timeToken = tokens.get(1);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timePattern);
            LocalTime time = LocalTime.parse(timeToken, timeFormatter);
            return LocalDateTime.of(date, time);
        }
        return date.atStartOfDay();
    }
    
    /**
     * Unique identifier, UID as defined by RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * Included is an example UID generator.  Other UID generators can be provided by
     * setting the UID callback.
     * Uses lazy initialization of property because UID doesn't often require advanced features.
     */
    public StringProperty UIDProperty()
    {
        if (uid == null) uid = new SimpleStringProperty(this, "UID", _uid);
        return uid;
    }
    private StringProperty uid;
    public String getUID() { return (uid == null) ? _uid : uid.getValue(); }
    private String _uid;
    public void setUID(String s)
    {
        if (uid == null)
        {
            _uid = s;
        } else
        {
            uid.set(s);
        }
    }
    public VEvent withUID(String uid) { setUID(uid); return this; }

    /** Callback for creating unique uid values */
    public Callback<Void, String> getUidGeneratorCallback() { return uidGeneratorCallback; }
    private static String datePattern = "yyyyMMdd";
    private static String timePattern = "HHmmss";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern + "'T'" + timePattern);
    private static Integer nextKey = 0;
    private Callback<Void, String> uidGeneratorCallback = (Void) ->
    { // default UID generator callback
        String dateTime = formatter.format(LocalDateTime.now());
        String domain = "jfxtras-agenda";
        return dateTime + nextKey++ + domain;
    };
    public void setUidGeneratorCallback(Callback<Void, String> uidCallback) { this.uidGeneratorCallback = uidCallback; }
    /** assign uid by calling the uidGeneratorCallback */
    public void makeUid()
    {
        setUID(uidGeneratorCallback.call(null));
    }
 

    
    private RRule rrule;
}
