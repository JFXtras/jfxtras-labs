package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleProp;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;

public abstract class VComponentRepeatableBase<T> extends VComponentPrimaryBase<T> implements VComponentRepeatable
{
    /**
     * RDATE
     * Recurrence Date-Times
     * RFC 5545 iCalendar 3.8.5.2, page 120.
     * 
     * This property defines the list of DATE-TIME values for
     * recurring events, to-dos, journal entries, or time zone definitions.
     * 
     * NOTE: DOESN'T CURRENTLY SUPPORT PERIOD VALUE TYPE
     * */
    @Override
    public ObservableList<Recurrences<? extends Temporal>> getRecurrences()
    {
        if (recurrences == null)
        {
            recurrences = FXCollections.observableArrayList();
        }
        return recurrences;
    }
    private ObservableList<Recurrences<? extends Temporal>> recurrences;
    @Override
    public void setRecurrences(ObservableList<Recurrences<? extends Temporal>> recurrences) { this.recurrences = recurrences; }
    /** add comma separated recurrencess into separate recurrences objects */
    public T withRecurrences(String...recurrences)
    {
        Arrays.stream(recurrences).forEach(c -> getRecurrences().add(new Recurrences(c)));
        return (T) this;
    }

    @Override
    public RecurrenceRuleProp getRecurrenceRule()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<RecurrenceRuleProp> recurrenceRuleProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRecurrenceRule(RecurrenceRuleProp rRule)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Stream<Temporal> streamRecurrences(Temporal startTemporal)
    {
        // TODO Auto-generated method stub
        return null;
    }

    
    /*
     * CONSTRUCTORS
     */
    VComponentRepeatableBase() { }
    
    VComponentRepeatableBase(String contentLines)
    {
        super(contentLines);
    }
}
