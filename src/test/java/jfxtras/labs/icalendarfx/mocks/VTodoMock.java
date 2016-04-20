package jfxtras.labs.icalendarfx.mocks;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExDate;
import jfxtras.labs.icalendarfx.properties.component.relationship.RelatedTo;

public class VTodoMock extends VTodo<VTodoMock, InstanceMock>
{
    /*
     * CONSTRUCTORS
     */
    public VTodoMock() { }
    
    public VTodoMock(String contentLines)
    {
        super(contentLines);
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
    public Pair<Double, Double> getGeographicPosition()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<Pair<Double, Double>> geographicPositionProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setGeographicPosition(ObjectProperty<Pair<Double, Double>> geo)
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
    public ExDate getExDate()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<ExDate> exDateProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setExDate(ExDate exDate)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Temporal getDateTimeRecurrence()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<Temporal> dateTimeRecurrenceProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setDateTimeRecurrence(Temporal recurrence)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public RelatedTo getRelatedTo()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<RelatedTo> relatedToProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setRelatedTo(RelatedTo relatedTo)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getSequence()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IntegerProperty sequenceProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setSequence(int value)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getStatus()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public StringProperty statusProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setStatus(String classification)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Stream<Temporal> streamLimitedByRange()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public Temporal getStartRange()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setStartRange(Temporal start)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Temporal getEndRange()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setEndRange(Temporal end)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Collection<InstanceMock> makeInstances(Temporal start, Temporal end)
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public Collection<InstanceMock> makeInstances()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public Collection<InstanceMock> instances()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public Stream<Temporal> streamRecurrences(Temporal startTemporal)
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

}
