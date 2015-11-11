package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Frequency;

/** Recurrence Rule, RRULE, as defined in RFC 5545 3.8.5.3, page 122 */
public class RRule {

    /** DTSTART from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule */
    final private ObjectProperty<LocalDateTime> startLocalDateTime = new SimpleObjectProperty<LocalDateTime>();
    public ObjectProperty<LocalDateTime> startLocalDateTimeProperty() { return startLocalDateTime; }
    public LocalDateTime getStartLocalDateTime() { return startLocalDateTime.getValue(); }
    public void setStartLocalDate(LocalDateTime startDate) { this.startLocalDateTime.set(startDate); }
    public RRule withStartLocalDate(LocalDateTime startDate) { setStartLocalDate(startDate); return this; }

    /** FREQ rule as defined in RFC 5545 iCalendar 3.3.10 p37 (i.e. Daily, Weekly, Monthly, etc.) */
    public Frequency getFrequency() { return frequency; }
    private Frequency frequency;
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }
    
    /** COUNT: (RFC 5545 iCalendar 3.3.10, page 41) number of events to occur before repeat rule ends */
    // Uses lazy initialization of property because often count stays as the default value of 0
    public IntegerProperty countProperty()
    {
        if (count == null) count = new SimpleIntegerProperty(this, "count", _count);
        return count;
    }
    private IntegerProperty count;
    public Integer getCount() { return (count == null) ? _count : count.getValue(); }
    private int _count = 0;
    public void setCount(Integer i)
    {
        if (i > 0)
        {
            if (count == null)
            {
                _count = i;
            } else
            {
                count.set(i);
            }
        } else
        {
            throw new InvalidParameterException("COUNT can't be less than 1. (" + i + ")");
        }
    }
    public RRule withCount(int count) { setCount(count); return this; }
    
    /** Resulting stream of date/times by applying rules 
     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. */
    public Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        return (getCount() == 0) ? frequency.stream(startDateTime) : frequency.stream(startDateTime).limit(getCount());

    };

    /** Resulting stream of date/times by applying rules 
     * Uses startLocalDateTime - first date/time in sequence (DTSTART) as a default starting point */
    public Stream<LocalDateTime> stream()
    {
        return stream(getStartLocalDateTime());
    }


}
