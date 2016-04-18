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
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleParameter;
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
            // change listener to ensure added recurrence sets match Temporal type of existing recurrence sets
            recurrences.addListener((ListChangeListener<? super Recurrences<? extends Temporal>>)
                    (ListChangeListener.Change<? extends Recurrences<? extends Temporal>> change) ->
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
            });
        }
        return recurrences;
    }
    private ObservableList<Recurrences<? extends Temporal>> recurrences;
//    private void addRecurrences(String recurrences)
//    {
//        if (recurrences.length() > 0)
//        {
//            Temporal t = DateTimeUtilities.temporalFromString(recurrences.split(",")[0]);
//            if (t instanceof LocalDate)
//            {
//                getRecurrences().add(new Recurrences<LocalDate>(recurrences));
//            } else if (t instanceof LocalDateTime)
//            {
//                getRecurrences().add(new Recurrences<LocalDateTime>(recurrences));
//                
//            } else if (t instanceof ZonedDateTime)
//            {
//                getRecurrences().add(new Recurrences<ZonedDateTime>(recurrences));
//            }
//        } else
//        {
//            this.recurrences = FXCollections.observableArrayList();
//        }        
//    }
    @Override
    public void setRecurrences(ObservableList<Recurrences<? extends Temporal>> recurrences) { this.recurrences = recurrences; }
    /** add comma separated recurrences into separate recurrences objects */
    public T withRecurrences(String...recurrences)
    {        
        String recurrencesCollected = Arrays.stream(recurrences).collect(Collectors.joining(","));
//        setRecurrences(recurrencesCollected);
        if (recurrencesCollected.length() > 0)
        {
            Temporal t = DateTimeUtilities.temporalFromString(recurrencesCollected.split(",")[0]);
            if (t instanceof LocalDate)
            {
                getRecurrences().add(new Recurrences<LocalDate>(recurrencesCollected));
            } else if (t instanceof LocalDateTime)
            {
                getRecurrences().add(new Recurrences<LocalDateTime>(recurrencesCollected));
                
            } else if (t instanceof ZonedDateTime)
            {
                getRecurrences().add(new Recurrences<ZonedDateTime>(recurrencesCollected));
            }
        }     
        return (T) this;
    }
    public T withRecurrences(ObservableSet<? extends Temporal> recurrences)
    {
        if (recurrences.size() > 0)
        {
            Temporal t = recurrences.iterator().next();
            if (t instanceof LocalDate)
            {
                ObservableSet<LocalDate> recurrenceCast = (ObservableSet<LocalDate>) recurrences;
                getRecurrences().add(new Recurrences<LocalDate>(recurrenceCast));
            } else if (t instanceof LocalDateTime)
            {
                ObservableSet<LocalDateTime> recurrenceCast = (ObservableSet<LocalDateTime>) recurrences;
                getRecurrences().add(new Recurrences<LocalDateTime>(recurrenceCast));
                
            } else if (t instanceof ZonedDateTime)
            {
                ObservableSet<ZonedDateTime> recurrenceCast = (ObservableSet<ZonedDateTime>) recurrences;
                getRecurrences().add(new Recurrences<ZonedDateTime>(recurrenceCast));
            }
        }    
        return (T) this;
    }
    public T withRecurrences(ObservableList<Recurrences<? extends Temporal>> recurrences) { setRecurrences(recurrences); return (T) this; }

    /**
     * RRULE, Recurrence Rule
     * RFC 5545 iCalendar 3.8.5.3, page 122.
     * This property defines a rule or repeating pattern for recurring events, 
     * to-dos, journal entries, or time zone definitions
     * If component is not repeating the value is null.
     * 
     * Examples:
     * RRULE:FREQ=DAILY;COUNT=10
     * RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH
     */
    @Override public ObjectProperty<RecurrenceRule> recurrenceRuleProperty()
    {
        if (recurrenceRule == null)
        {
            recurrenceRule = new SimpleObjectProperty<>(this, PropertyEnum.UNIQUE_IDENTIFIER.toString());
        }
        return recurrenceRule;
    }
    private ObjectProperty<RecurrenceRule> recurrenceRule;
    @Override
    public RecurrenceRule getRecurrenceRule() { return recurrenceRuleProperty().get(); }
    @Override
    public void setRecurrenceRule(RecurrenceRule recurrenceRule) { recurrenceRuleProperty().set(recurrenceRule); }
    public void setRecurrenceRule(RecurrenceRuleParameter rrule) { recurrenceRuleProperty().set(new RecurrenceRule(rrule)); }
    public T withRecurrenceRule(String rrule) { setRecurrenceRule(new RecurrenceRule(rrule)); return (T) this; }
    public T withRecurrenceRule(RecurrenceRule rrule) { setRecurrenceRule(rrule); return (T) this; }
    public T withRecurrenceRule(RecurrenceRuleParameter rrule) { setRecurrenceRule(rrule); return (T) this; }


    // put in subclass
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
