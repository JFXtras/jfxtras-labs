package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Resources;

public class VEventNew extends VComponentLocatableBase<VEventNew> implements VEventNewInt<VEventNew>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VEVENT;
    }
    
    /*
     * CONSTRUCTORS
     */
    public VEventNew() { }
    
    public VEventNew(String contentLines)
    {
        super(contentLines);
    }


    @Override
    public Location getLocation()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<Location> locationProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setLocation(Location location)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getPriority()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IntegerProperty priorityProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setPriority(int priority)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Resources getResources()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<Resources> resourcesProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setResources(Resources resources)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Stream<Temporal> streamRecurrences(Temporal startTemporal)
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public Temporal getDateTimeEnd()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<Temporal> dateTimeEndProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setDateTimeEnd(Temporal dtEnd)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getTimeTransparency()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public StringProperty timeTransparencyProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setTimeTransparency(String transparency)
    {
        // TODO Auto-generated method stub
        
    }
}
