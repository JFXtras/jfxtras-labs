package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleProp;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

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
            ListChangeListener<? super Recurrences<? extends Temporal>> listener
            = (ListChangeListener.Change<? extends Recurrences<? extends Temporal>> change) ->
            {
                if (recurrences.size() > 1)
                {
                Class<? extends Temporal> firstTemporalClass = recurrences.get(0).getValue().iterator().next().getClass();
                while (change.next())
                    {
                        if (change.wasAdded())
                        {
                            Iterator<? extends Recurrences<? extends Temporal>> i = change.getAddedSubList().iterator();
                            while (i.hasNext())
                            {
                                Recurrences<? extends Temporal> r = i.next();
                                Class<? extends Temporal> myTemporalClass = r.getValue().iterator().next().getClass();
                                if (! myTemporalClass.equals(firstTemporalClass))
                                {
                                    throw new DateTimeException("Added recurrences Temporal class " + myTemporalClass.getSimpleName() +
                                            " doesn't match previous recurrences Temporal class " + firstTemporalClass.getSimpleName());
                                }
                            }
                        }
                    }
                }
            };

            recurrences.addListener(listener);
        }
        return recurrences;
    }
    private ObservableList<Recurrences<? extends Temporal>> recurrences;
    public void setRecurrences(String recurrences)
    {
        if (recurrences.length() > 0)
        {
            Temporal t = DateTimeUtilities.temporalFromString(recurrences.split(",")[0]);
//            String recurrencesCollected = Arrays.stream(recurrences).collect(Collectors.joining(","));
            if (t instanceof LocalDate)
            {
                getRecurrences().add(new Recurrences<LocalDate>(recurrences));
            } else if (t instanceof LocalDateTime)
            {
                getRecurrences().add(new Recurrences<LocalDateTime>(recurrences));
                
            } else if (t instanceof ZonedDateTime)
            {
                getRecurrences().add(new Recurrences<ZonedDateTime>(recurrences));
            }
        } else
        {
            this.recurrences = FXCollections.observableArrayList();
        }        
    }
    @Override
    public void setRecurrences(ObservableList<Recurrences<? extends Temporal>> recurrences) { this.recurrences = recurrences; }
    /** add comma separated recurrences into separate recurrences objects */
    public T withRecurrences(String...recurrences)
    {        
        String recurrencesCollected = Arrays.stream(recurrences).collect(Collectors.joining(","));
        setRecurrences(recurrencesCollected);
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
