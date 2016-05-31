package jfxtras.labs.icalendarfx.components;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleCache;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneName;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneOffsetFrom;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneOffsetTo;

/**
 * Subcomponent of VAlarm
 * Either StandardTime or DaylightSavingsTime.
 * Both classes have identical methods.
 * 
 * @author David Bal
 *
 */
public abstract class StandardOrDaylightBase<T> extends VComponentRepeatableBase<T> implements StandardOrDaylight<T>
{
    /**
     * TZNAME
     * Time Zone Name
     * RFC 5545, 3.8.3.2, page 103
     * 
     * This property specifies the customary designation for a time zone description.
     * 
     * This property specifies the text value that uniquely
     * identifies the "VTIMEZONE" calendar component in the scope of an
     * 
     * EXAMPLES:
     * TZNAME:EST
     * TZNAME;LANGUAGE=fr-CA:HN
     */
    @Override
    public ObservableList<TimeZoneName> getTimeZoneNames() { return timeZoneNames; }
    private ObservableList<TimeZoneName> timeZoneNames;
    @Override
    public void setTimeZoneNames(ObservableList<TimeZoneName> timeZoneNames) { this.timeZoneNames = timeZoneNames; }
    
    /**
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
    @Override
    public ObjectProperty<TimeZoneOffsetFrom> timeZoneOffsetFromProperty()
    {
        if (timeZoneOffsetFrom == null)
        {
            timeZoneOffsetFrom = new SimpleObjectProperty<>(this, PropertyType.TIME_ZONE_OFFSET_FROM.toString());
        }
        return timeZoneOffsetFrom;
    }
    private ObjectProperty<TimeZoneOffsetFrom> timeZoneOffsetFrom;

    /**
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
    @Override
    public ObjectProperty<TimeZoneOffsetTo> timeZoneOffsetToProperty()
    {
        if (timeZoneOffsetTo == null)
        {
            timeZoneOffsetTo = new SimpleObjectProperty<>(this, PropertyType.TIME_ZONE_OFFSET_TO.toString());
        }
        return timeZoneOffsetTo;
    }
    private ObjectProperty<TimeZoneOffsetTo> timeZoneOffsetTo;
    
    /*
     * CONSTRUCTORS
     */
    public StandardOrDaylightBase() { super(); }
    
    public StandardOrDaylightBase(String contentLines)
    {
        super(contentLines);
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
        return errors;
    }
    
    // Recurrence streamer - produces recurrence set
    private RecurrenceRuleCache streamer = new RecurrenceRuleCache(this);
    @Override
    public RecurrenceRuleCache recurrenceStreamer() { return streamer; }
}
