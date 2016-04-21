package jfxtras.labs.icalendarfx.components;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Resources;

public class VTodo extends VComponentLocatableBase<VTodo> implements VTodoInt<VTodo>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VTODO;
    }
    
    /*
     * CONSTRUCTORS
     */
    public VTodo() { }
    
    public VTodo(String contentLines)
    {
        super(contentLines);
    }

    @Override
    public ObjectProperty<TemporalAmount> durationProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public TemporalAmount getDuration()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setDuration(TemporalAmount duration)
    {
        // TODO Auto-generated method stub
        
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
    public ZonedDateTime getDateTimeCompleted()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<ZonedDateTime> dateTimeCompletedProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setDateTimeCompleted(ZonedDateTime dateTimeCompleted)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Temporal getDateTimeDue()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<Temporal> dateTimeDueProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setDateTimeDue(Temporal dateTimeDue)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getPercentComplete()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IntegerProperty percentCompleteProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setPercentComplete(int percentComplete)
    {
        // TODO Auto-generated method stub
        
    }
}
