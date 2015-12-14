package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * Class that encapsulates a Temporal object to represent either DATE (3.3.4)
 * or DATE-TIME (3.3.5).  It represents DATE (as LocalDate) if the component is
 * for a whole day and DATE-TIME (as LocalDateTime) for a component that has a 
 * time part.
 * if wholeDay is true dateTime is atStartOfDay and time can't be set.
 * from RFC 5545 iCalendar
 * 
 * @author David Bal
 *
 */
public class VDateTime implements Comparable<VDateTime>
{
    private final static String datePattern = "yyyyMMdd";
    private final static String timePattern = "HHmmss";
    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(datePattern + "'T'" + timePattern);
    public final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(datePattern);
    public final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(timePattern);
    
    private Temporal dateOrDateTime;
    protected Temporal getDateOrDateTime() { return dateOrDateTime; }
    private void setDateOrDateTime(Temporal dateOrDateTime) { this.dateOrDateTime = dateOrDateTime; }
    
    public LocalDate getLocalDate()
    {
        if (dateOrDateTime == null) return null;
        if (dateOrDateTime instanceof LocalDate)
        {
            return (LocalDate) dateOrDateTime;
        } else throw new IllegalArgumentException("Type of temporal is not LocalDate.");
    }
    public Temporal getTemporal()
    {
        if (dateOrDateTime == null) return null;
        if ((dateOrDateTime instanceof LocalDate) || (dateOrDateTime instanceof LocalDateTime))
        {
            return dateOrDateTime;
        } else throw new IllegalArgumentException("Type of temporal is not LocalDate or LocalDateTime.");
    }
    public void setTemporal(Temporal temporal)
    {
        if ((dateOrDateTime instanceof LocalDate) || (dateOrDateTime instanceof LocalDateTime))
        {
            dateOrDateTime = temporal;
        }
    } 
    
    public void setLocalDate(Temporal date)
    {
        if ((dateOrDateTime instanceof LocalDate) || (dateOrDateTime == null))
        {
            dateOrDateTime = date;
        }
    }  
    public LocalDateTime getLocalDateTime()
    {
        if (dateOrDateTime == null) return null;
        if (dateOrDateTime instanceof LocalDateTime)
        {
            return (LocalDateTime) dateOrDateTime;
        } else if (dateOrDateTime instanceof LocalDate)
        {
            return (LocalDateTime) ((LocalDate) dateOrDateTime).atStartOfDay();
        } else
        {
            throw new IllegalArgumentException("Type of temporal is not LocalDate: " + dateOrDateTime);            
        }
    }
//    public void setLocalDateTime(LocalDateTime dateTime)
//    {
//        if ((dateOrDateTime instanceof LocalDateTime) || (dateOrDateTime == null))
//        {
//            dateOrDateTime = dateTime;
//        }
//    }
  
    /** This property is true when this only contains a date, not a time.
     * example: DTSTART;VALUE=DATE:19971102
     * If this property is true the component can't have DURATION or DTEND
     * If this property is true the time portion of dateTimeStart is set to the start of the day and
     * the time is ignored.
     */
    public boolean isWholeDay() { return dateOrDateTime instanceof LocalDate; }
    
    // CONSTRUCTORS
    /**
     * A representation of DATE (3.3.4) or DATE-TIME (3.3.5) defined in RFC 5545 iCalendar 3.3.5.
     * Type of dateOrDateTime must be either either LocalDate or LocalDateTime.
     */
    public VDateTime(Temporal dateOrDateTime)
    {
        if (dateOrDateTime instanceof LocalDate || dateOrDateTime instanceof LocalDateTime)
        {
            this.dateOrDateTime = dateOrDateTime;            
        } else throw new IllegalArgumentException("Type of dateOrDateTime must be LocalDate or LocalDateTime.");
    }
    
    /**
     * Copy constructor for a representation of DATE-TIME (RFC 5545 iCalendar 3.3.5) initialized with
     * a LocalDateTime.
     */
    public VDateTime(VDateTime vDateTime)
    {
        copy(vDateTime, this);
    }

    public VDateTime() { }

    /** Deep copy all fields from source to destination */
    public void copyTo(VDateTime destination)
    {
        copy(this, destination);
    }
    
    /** Deep copy all fields from source to destination */
    private static void copy(VDateTime source, VDateTime destination)
    {
        destination.setDateOrDateTime(source.getDateOrDateTime());
    }

    @Override
    public boolean equals(Object obj)
    {
        if (super.equals(obj)) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        VDateTime testObj = (VDateTime) obj;
        return getDateOrDateTime().equals(testObj.getDateOrDateTime());
    }
    
    @Override
    public int compareTo(VDateTime obj)
    {
        if (getDateOrDateTime() instanceof LocalDate)
        {
            return getLocalDate().compareTo(obj.getLocalDate());            
        } else if (getDateOrDateTime() instanceof LocalDateTime)
        {
            return getLocalDateTime().compareTo(obj.getLocalDateTime());            
        } else throw new IllegalArgumentException("Type of temporal must be LocalDate or LocalDateTime.");
    }
    
    // overriding hashCode is required to ensure Set properly avoids duplicates. 
    @Override
    public int hashCode() {
        return dateOrDateTime.hashCode();
    }
    
    /**
     * Returns a DATE (3.3.4) and DATE-TIME (3.3.5) string as defined from RFC 5545 iCalendar
     */
    @Override
    public String toString()
    {
        return (isWholeDay()) ? "VALUE=DATE:" + DATE_FORMATTER.format(getLocalDate())
                              : DATE_TIME_FORMATTER.format(getLocalDateTime());
    }
        
    /**
     * parses string into LocalDate or LocalDateTime in a new VDateTime object
     * @param dateTimeString - string with either DATE (3.3.4) and DATE-TIME (3.3.5)
     * defined from RFC 5545 iCalendar.
     * 
     * Date-time Examples:
     * VALUE=DATE-TIME:20151115T100000
     * VALUE=DATE-TIME:20151115T100000Z (UTC time ignored currently)
     * 20151115T100000
     * 
     * Date Example:
     * VALUE=DATE:20151115
     *  Currently only handles local time, UTC time and time zone reference is TODO
     * @return - new VDateTime object initialized with parsed dateTimeString
     */
    public static VDateTime parseString(String dateTimeString)
    {
        if (dateTimeString.matches("^(VALUE=DATE-TIME:)?[0-9]{8}T?([0-9]{6})?Z?"))
        {
            String extractedDateTimeString = dateTimeString.substring(dateTimeString.lastIndexOf(":") + 1).trim();
            LocalDateTime dateTime = LocalDateTime.parse(extractedDateTimeString, DATE_TIME_FORMATTER);
            return new VDateTime(dateTime);
        } else if (dateTimeString.matches("^VALUE=DATE:[0-9]{8}"))
        {
            String extractedDateString = dateTimeString.substring(dateTimeString.lastIndexOf(":") + 1).trim();
            LocalDate date = LocalDate.parse(extractedDateString, DATE_FORMATTER);
            return new VDateTime(date);            
        } else
        {
            throw new IllegalArgumentException("String does not match DATE or DATE-TIME pattern: " + dateTimeString);
        }
    }
}
