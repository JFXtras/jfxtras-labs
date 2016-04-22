package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.properties.component.time.FreeBusyTime;

/**
 * VFREEBUSY: RFC 5545 iCalendar 3.6.4. page 59
 * currently not supported - ZonedDateTime is providing time zone information
 * 
 * @author David Bal
 *
 */
public class VFreeBusy extends VComponentPersonalBase<VFreeBusy> implements VFreeBusyInt<VFreeBusy>, VComponentDateTimeEnd<VFreeBusy>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VFREEBUSY;
    }
    
    /**
     * DTEND
     * Date-Time End (for local-date)
     * RFC 5545, 3.8.2.2, page 95
     * 
     * This property specifies when the calendar component ends.
     * 
     * The value type of this property MUST be the same as the "DTSTART" property, and
     * its value MUST be later in time than the value of the "DTSTART" property.
     * 
     * Example:
     * DTEND;VALUE=DATE:19980704
     */
    @Override public ObjectProperty<DateTimeEnd<? extends Temporal>> dateTimeEndProperty()
    {
        if (dateTimeEnd == null)
        {
            dateTimeEnd = new SimpleObjectProperty<>(this, PropertyEnum.DATE_TIME_END.toString());
        }
        return dateTimeEnd;
    }
    private ObjectProperty<DateTimeEnd<? extends Temporal>> dateTimeEnd;
    
    /*
     * CONSTRUCTORS
     */
    public VFreeBusy() { }
    
    public VFreeBusy(String contentLines)
    {
        super(contentLines);
    }
    
    @Override
    public void checkDateTimeStartConsistency()
    {
        checkDateTimeStartConsistency2();
    }

    @Override
    public ObservableList<FreeBusyTime> getFreeBusy()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setFreeBusy(ObservableList<FreeBusyTime> properties)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getContact()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringProperty contactProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setContact(String contact)
    {
        // TODO Auto-generated method stub
        
    }
}
