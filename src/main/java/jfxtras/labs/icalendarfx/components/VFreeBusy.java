package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.time.FreeBusyTime;

/**
 * VFREEBUSY: RFC 5545 iCalendar 3.6.4. page 59
 * currently not supported - ZonedDateTime is providing time zone information
 * 
 * @author David Bal
 *
 */
public class VFreeBusy extends VComponentPersonalBase<VFreeBusy> implements VFreeBusyInt
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VFREEBUSY;
    }
    
    /*
     * CONSTRUCTORS
     */
    public VFreeBusy() { }
    
    public VFreeBusy(String contentLines)
    {
        super(contentLines);
    }

    @Override
    public Temporal getDateTimeEnd()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<Temporal> dateTimeEndProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDateTimeEnd(Temporal dtEnd)
    {
        // TODO Auto-generated method stub
        
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
