package jfxtras.labs.icalendarfx.mocks;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.properties.component.time.FreeBusyTime;

public class VFreeBusyMock extends VFreeBusy<VFreeBusyMock>
{
    /*
     * CONSTRUCTORS
     */
    public VFreeBusyMock() { }
    
    public VFreeBusyMock(String contentLines)
    {
        super(contentLines);
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
}
