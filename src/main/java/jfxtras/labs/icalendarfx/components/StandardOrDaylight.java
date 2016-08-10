package jfxtras.labs.icalendarfx.components;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleCache;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneName;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneOffsetFrom;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneOffsetTo;

/**
 * <p>Superclass of {@link DaylightSavingTime} and {@link StandardTime} that
 * contains the following properties:
 *<ul>
 *<li>{@link TimeZoneName TZNAME}
 *<li>{@link TimeZoneOffsetFrom TZOFFSETFROM}
 *<li>{@link TimeZoneOffsetTo TZOFFSETTO}
 *</ul>
 *</p>
 * 
 * @author David Bal
 *
 */
public abstract class StandardOrDaylight<T> extends VComponentRepeatableBase<T>
{
    /*
     * TZNAME
     * Time Zone Name
     * RFC 5545, 3.8.3.2, page 103
     * 
     * This property specifies the customary designation for a time zone description.
     * 
     * EXAMPLES:
     * TZNAME:EST
     * TZNAME;LANGUAGE=fr-CA:HN
     */
    /**
     * <p>Gets the value of the {@code ObservableList<TimeZoneName> }</p>
     * <p>The {@link TimeZoneName} specifies the customary designation for a time zone description.</p>
     */
    public ObservableList<TimeZoneName> getTimeZoneNames() { return timeZoneNames; }
    private ObservableList<TimeZoneName> timeZoneNames;
    /**
     * <p>Sets the value of the {@code ObservableList<TimeZoneName> }</p>
     * <p>The {@link TimeZoneName} specifies the customary designation for a time zone description.</p>
     */
    public void setTimeZoneNames(ObservableList<TimeZoneName> timeZoneNames)
    {
        if (timeZoneNames != null)
        {
            orderer().registerSortOrderProperty(timeZoneNames);
        } else
        {
            orderer().unregisterSortOrderProperty(this.timeZoneNames);
        }
        this.timeZoneNames = timeZoneNames;
    }
    /**
     * <p>Sets the value of the {@code ObservableList<TimeZoneName> }</p>
     * <p>The {@link TimeZoneName} specifies the customary designation for a time zone description.</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneNames(ObservableList<TimeZoneName> timeZoneNames) { setTimeZoneNames(timeZoneNames); return (T) this; }
    /**
     * <p>Sets the value of the {@code ObservableList<TimeZoneName> } by parsing a vararg of time zone name strings</p>
     * <p>The {@link TimeZoneName} specifies the customary designation for a time zone description.</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneNames(String...timeZoneNames)
    {
        Arrays.stream(timeZoneNames).forEach(c -> PropertyType.TIME_ZONE_NAME.parse(this, c));
        return (T) this;
    }
    /**
     * <p>Sets the value of the {@code ObservableList<TimeZoneName> } from a vararg of {@link TimeZoneName} objects.</p>
     * <p>The {@link TimeZoneName} specifies the customary designation for a time zone description.</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneNames(TimeZoneName...timeZoneNames)
    {
//        if (getTimeZoneNames() == null)
//        {
            setTimeZoneNames(FXCollections.observableArrayList(timeZoneNames));
//        } else
//        {
//            getTimeZoneNames().addAll(timeZoneNames);
//        }
        return (T) this;
    }
    
    /*
     * TZOFFSETFROM
     * Time Zone Offset From
     * RFC 5545, 3.8.3.3, page 104
     * 
     * This property specifies the offset that is in use prior to this time zone observance.
     * 
     * EXAMPLES:
     * TZOFFSETFROM:-0500
     * TZOFFSETFROM:+1345
     */
    /**
     * <p>Gets the value of the {@link TimeZoneOffsetFrom} property }</p>
     * <p>This property specifies the offset that is in use prior to this time zone observance.</p>
     */
    public TimeZoneOffsetFrom getTimeZoneOffsetFrom() { return timeZoneOffsetFromProperty().get(); }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetFrom} property }</p>
     * <p>This property specifies the offset that is in use prior to this time zone observance.</p>
     */
    public void setTimeZoneOffsetFrom(TimeZoneOffsetFrom timeZoneOffsetFrom) { timeZoneOffsetFromProperty().set(timeZoneOffsetFrom); }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetFrom} property } by creating a new {@link TimeZoneOffsetFrom} from
     * the {@link ZoneOffset} parameter</p>
     * <p>This property specifies the offset that is in use prior to this time zone observance.</p>
     * 
     * @param zoneOffset  value for new {@link TimeZoneOffsetFrom}
     */
    public void setTimeZoneOffsetFrom(ZoneOffset zoneOffset) { setTimeZoneOffsetFrom(new TimeZoneOffsetFrom(zoneOffset)); }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetFrom} property} by parsing a iCalendar content string</p>
     * <p>This property specifies the offset that is in use prior to this time zone observance.</p>
     */
    public void setTimeZoneOffsetFrom(String timeZoneOffsetFrom) { PropertyType.TIME_ZONE_OFFSET_FROM.parse(this, timeZoneOffsetFrom); }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetFrom} property } by creating a new {@link TimeZoneOffsetFrom} from
     * the {@link ZoneOffset} parameter</p>
     * <p>This property specifies the offset that is in use prior to this time zone observance.</p>
     * 
     * @param zoneOffset  value for new {@link TimeZoneOffsetFrom}
     */
    public T withTimeZoneOffsetFrom(TimeZoneOffsetFrom timeZoneOffsetFrom) { setTimeZoneOffsetFrom(timeZoneOffsetFrom); return (T) this; }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetFrom} property } by creating a new {@link TimeZoneOffsetFrom} from
     * the {@link ZoneOffset} parameter</p>
     * <p>This property specifies the offset that is in use prior to this time zone observance.</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetFrom(ZoneOffset zoneOffset) { setTimeZoneOffsetFrom(zoneOffset); return (T) this; }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetFrom} property } by parsing a iCalendar content string</p>
     * <p>This property specifies the offset that is in use prior to this time zone observance.</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetFrom(String timeZoneOffsetFrom) { setTimeZoneOffsetFrom(timeZoneOffsetFrom); return (T) this; }
    /** A property wrapping the {@link TimeZoneOffsetFrom} value. */
    public ObjectProperty<TimeZoneOffsetFrom> timeZoneOffsetFromProperty()
    {
        if (timeZoneOffsetFrom == null)
        {
            timeZoneOffsetFrom = new SimpleObjectProperty<>(this, PropertyType.TIME_ZONE_OFFSET_FROM.toString());
            orderer().registerSortOrderProperty(timeZoneOffsetFrom);
        }
        return timeZoneOffsetFrom;
    }
    private ObjectProperty<TimeZoneOffsetFrom> timeZoneOffsetFrom;

    /*
     * TZOFFSETTO
     * Time Zone Offset To
     * RFC 5545, 3.8.3.4, page 105
     * 
     * This property specifies the offset that is in use in this time zone observance
     * 
     * EXAMPLES:
     * TZOFFSETTO:-0400
     * TZOFFSETTO:+1245
     */
    /**
     * <p>Gets the value of the {@link TimeZoneOffsetTo} property }</p>
     * <p>This property specifies the offset that is in use in this time zone observance.</p>
     */
    public TimeZoneOffsetTo getTimeZoneOffsetTo() { return timeZoneOffsetToProperty().get(); }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetTo} property }</p>
     * <p>This property specifies the offset that is in use in this time zone observance.</p>
     */
    public void setTimeZoneOffsetTo(TimeZoneOffsetTo timeZoneOffsetTo) { timeZoneOffsetToProperty().set(timeZoneOffsetTo); }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetTo} property } by creating a new {@link TimeZoneOffsetTo} from
     * the {@link ZoneOffset} parameter</p>
     * <p>This property specifies the offset that is in use in this time zone observance.</p>
     * 
     * @param zoneOffset  value for new {@link TimeZoneOffsetTo}
     */
    public void setTimeZoneOffsetTo(ZoneOffset zoneOffset) { setTimeZoneOffsetTo(new TimeZoneOffsetTo(zoneOffset)); }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetTo} property} by parsing a iCalendar content string</p>
     * <p>This property specifies the offset that is in use in this time zone observance.</p>
     */
    public void setTimeZoneOffsetTo(String timeZoneOffsetTo) { PropertyType.TIME_ZONE_OFFSET_TO.parse(this, timeZoneOffsetTo); }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetTo} property }</p>
     * <p>This property specifies the offset that is in use in this time zone observance.</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetTo(TimeZoneOffsetTo timeZoneOffsetTo) { setTimeZoneOffsetTo(timeZoneOffsetTo); return (T) this; }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetTo} property } by creating a new {@link TimeZoneOffsetTo} from
     * the {@link ZoneOffset} parameter</p>
     * <p>This property specifies the offset that is in use in this time zone observance.</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetTo(ZoneOffset zoneOffset) { setTimeZoneOffsetTo(zoneOffset); return (T) this; }
    /**
     * <p>Sets the value of the {@link TimeZoneOffsetTo} property }  by parsing a iCalendar content string</p>
     * <p>This property specifies the offset that is in use in this time zone observance.</p>
     * 
     * @return - this class for chaining
     */
    public T withTimeZoneOffsetTo(String timeZoneOffsetTo) { setTimeZoneOffsetTo(timeZoneOffsetTo); return (T) this; }
    /**
     * A property wrapping the {@link TimeZoneOffsetTo} value.
     */
    public ObjectProperty<TimeZoneOffsetTo> timeZoneOffsetToProperty()
    {
        if (timeZoneOffsetTo == null)
        {
            timeZoneOffsetTo = new SimpleObjectProperty<>(this, PropertyType.TIME_ZONE_OFFSET_TO.toString());
            orderer().registerSortOrderProperty(timeZoneOffsetTo);
        }
        return timeZoneOffsetTo;
    }
    private ObjectProperty<TimeZoneOffsetTo> timeZoneOffsetTo;
    
    /*
     * CONSTRUCTORS
     */
    public StandardOrDaylight() { super(); }

    public StandardOrDaylight(StandardOrDaylight<T> source)
    {
        super(source);
    }

    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (getDateTimeStart() == null)
        {
            errors.add("DTSTART is not present.  DTSTART is REQUIRED and MUST NOT occur more than once");
        }
        
        if (getTimeZoneOffsetFrom() == null)
        {
            errors.add("TZOFFSETFROM is not present.  TZOFFSETFROM is REQUIRED and MUST NOT occur more than once");
        }
        
        if (getTimeZoneOffsetTo() == null)
        {
            errors.add("TZOFFSETTO is not present.  TZOFFSETTO is REQUIRED and MUST NOT occur more than once");
        }
        return Collections.unmodifiableList(errors);
    }
    
    // Recurrence streamer - produces recurrence set
    private RecurrenceRuleCache streamer = new RecurrenceRuleCache(this);
    @Override
    public RecurrenceRuleCache recurrenceStreamer() { return streamer; }
}
