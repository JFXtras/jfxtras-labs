package jfxtras.labs.icalendarfx.components;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

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
