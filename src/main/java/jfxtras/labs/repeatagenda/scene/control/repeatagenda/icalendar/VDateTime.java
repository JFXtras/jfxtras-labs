package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

/**
 * Class that represents both DATE (3.3.4) and DATE-TIME (3.3.5)
 * if wholeDay is true dateTime is atStartOfDay and time can't be set.
 * from RFC 5545 iCalendar
 * 
 * @author David Bal
 *
 */
public class VDateTime
{
    
    private final static String datePattern = "yyyyMMdd";
    private final static String timePattern = "HHmmss";
    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(datePattern + "'T'" + timePattern);
    public final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(datePattern);
    
    public ObjectProperty<LocalDateTime> dateTimeProperty() { return dateTime; }
    private ObjectProperty<LocalDateTime> dateTime = new SimpleObjectProperty<>();
    public void setLocalDateTime(LocalDateTime dateTime) { this.dateTime.set(dateTime); }
    public LocalDateTime getLocalDateTime() { return dateTime.get(); }

    /** DATE part of DATE-TIME property */
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public LocalDate getLocalDate() { return date.get(); }
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    public void setLocalDate(LocalDate date) { this.date.set(date); }
    
    /** TIME part of DATE-TIME property */
    public ObjectProperty<LocalTime> timeProperty()
    {
        return (wholeDay) ? null : time;
    }
    public LocalTime getLocalTime() { return time.get(); }
    private ObjectProperty<LocalTime> time = new SimpleObjectProperty<LocalTime>();
    public void setLocalTime(LocalTime time)
    {
        if (! isWholeDay())
        {
            this.time.set(time);
        } else throw new InvalidParameterException("Can't set time when wholeDay is true.");
    }
    
    /** This property is true when the this contains a date (no time)
     * example: DTSTART;VALUE=DATE:19971102
     * If this property is true the component can't have DURATION or DTEND
     * If this property is true the time portion of dateTimeStart is set to the start of the day and
     * the time is ignored.
     */
    final private boolean wholeDay;
    public boolean isWholeDay() { return wholeDay; }

    // Listeners
    private final ChangeListener<? super LocalDateTime> dateTimeListener;
    private ChangeListener<? super LocalDate> dateListener;
    private ChangeListener<? super LocalTime> timeListener;
    
    // CONSTRUCTORS
    private VDateTime(boolean wholeDay)
    {
        this.wholeDay = wholeDay;

        // assign listeners
        dateTimeListener = (obs, oldValue, newValue) ->
        {
            dateProperty().removeListener(dateListener);
            setLocalDate(newValue.toLocalDate());
            if (! isWholeDay())
            {
                timeProperty().removeListener(timeListener);
                setLocalTime(newValue.toLocalTime());
                timeProperty().addListener(timeListener);
            }
            dateProperty().addListener(dateListener);
        };
        dateListener = (obs, oldValue, newValue) ->
        {
            dateTimeProperty().removeListener(dateTimeListener);
            if (isWholeDay())
            {
                setLocalDateTime(getLocalDate().atStartOfDay());
            } else
            {
                setLocalDateTime(LocalDateTime.of(getLocalDate(), getLocalTime()));
            }
            dateTimeProperty().addListener(dateTimeListener);
        };
        timeListener = (obs, oldValue, newValue) ->
        {
            dateTimeProperty().removeListener(dateTimeListener);
            setLocalDateTime(LocalDateTime.of(getLocalDate(), getLocalTime()));
            dateTimeProperty().addListener(dateTimeListener);
        };
        dateTimeProperty().addListener(dateTimeListener);
        dateProperty().addListener(dateListener);
        if (! wholeDay) timeProperty().addListener(timeListener);
    }
    
    // CONSTRUCTORS
    /**
     * A representation of DATE-TIME (RFC 5545 iCalendar 3.3.5) initialized with
     * a LocalDateTime.
     */
    public VDateTime(LocalDateTime dateTime)
    {
        this(false);
        setLocalDateTime(dateTime);
    }

    /**
     * A representation of DATE (RFC 5545 iCalendar 3.3.4) initialized with
     * a LocalDate.  This is used for components that use whole days only without a time field.
     * An object instantiated with this constructor can not have the time field set.  Trying to do
     * so will result in an exception being thrown.
     * For convenience, a dateTimeProperty is provided with the time set to the start of the day.
     */
    public VDateTime(LocalDate date)
    {
        this(true);
        setLocalDate(date);
    }
    
    /**
     * Copy constructor for a representation of DATE-TIME (RFC 5545 iCalendar 3.3.5) initialized with
     * a LocalDateTime.
     */
    public VDateTime(VDateTime vDateTime)
    {
        this(vDateTime.isWholeDay());
        copy(vDateTime, this);
    }


    /** Deep copy all fields from source to destination */
    public void copyTo(VDateTime destination)
    {
        copy(this, destination);
    }
    
    /** Deep copy all fields from source to destination */
    private static void copy(VDateTime source, VDateTime destination)
    {
        destination.setLocalDateTime(source.getLocalDateTime()); // date and time are set by listeners
    }

    @Override
    public boolean equals(Object obj)
    {
        if (super.equals(obj)) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        VDateTime testObj = (VDateTime) obj;
        boolean dateEquals = getLocalDate().equals(testObj.getLocalDate());
        boolean timeEquals = (getLocalTime() == null) ?
                (testObj.getLocalTime() == null) : getLocalTime().equals(testObj.getLocalTime());
        return dateEquals && timeEquals;
    }
    
    /**
     * Returns a DATE (3.3.4) and DATE-TIME (3.3.5) string as defined from RFC 5545 iCalendar
     */
    @Override
    public String toString()
    {
        return (wholeDay) ? DATE_FORMATTER.format(getLocalDate()) : DATE_TIME_FORMATTER.format(getLocalDateTime());
    }
        
    /**
     * @param dateTimeString - string with either DATE (3.3.4) and DATE-TIME (3.3.5)
     *  defined from RFC 5545 iCalendar
     * @return - new VDateTime object initialized with parsed dateTimeString
     */
    public static VDateTime parseDateTime(String dateTimeString)
    {
        if (dateTimeString.matches(".+T.+"))
        {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
            return new VDateTime(dateTime);
        } else
        {
            LocalDate date = LocalDate.parse(dateTimeString, DATE_FORMATTER);
            return new VDateTime(date);            
        }
    }

}
