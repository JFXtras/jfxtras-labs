package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.recurrence.PropertyBaseRecurrence;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleParameter;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * Calendar components that can repeat
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodoInt
 * @see VJournalInt
 * @see StandardOrSavings
 */
public interface VComponentRepeatable<T> extends VComponentPrimary<T>
{
    /** ensure additions to {@link #getRecurrences()} are the same Temporal type as former elements */
    static ListChangeListener<PropertyBaseRecurrence<? extends Temporal, ?>> RECURRENCE_CONSISTENCY_LISTENER = (ListChangeListener.Change<? extends PropertyBaseRecurrence<? extends Temporal, ?>> change) ->
    {
        ObservableList<? extends PropertyBaseRecurrence<? extends Temporal, ?>> list = change.getList();
        if (list.size() > 1)
        {
            Class<? extends Temporal> firstTemporalClass = list.get(0).getValue().iterator().next().getClass();
            while (change.next())
            {
                if (change.wasAdded())
                {
                    Iterator<? extends PropertyBaseRecurrence<? extends Temporal, ?>> i = change.getAddedSubList().iterator();
                    while (i.hasNext())
                    {
                        PropertyBaseRecurrence<? extends Temporal, ?> r = i.next();
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

    /** ensure additions to {@link #getRecurrences()} are the same Temporal type as former elements */
//    default void addRecurrenceListener()
//    {
//        if (getRecurrences() != null)
//        {
//            getRecurrences().addListener((ListChangeListener.Change<? extends PropertyBaseRecurrence<? extends Temporal, ?>> change) ->
//            {
//                ObservableList<? extends PropertyBaseRecurrence<? extends Temporal, ?>> list = change.getList();
//                if (list.size() > 1)
//                {
//                    Class<? extends Temporal> firstTemporalClass = list.get(0).getValue().iterator().next().getClass();
//                    while (change.next())
//                    {
//                        if (change.wasAdded())
//                        {
//                            Iterator<? extends PropertyBaseRecurrence<? extends Temporal, ?>> i = change.getAddedSubList().iterator();
//                            while (i.hasNext())
//                            {
//                                PropertyBaseRecurrence<? extends Temporal, ?> r = i.next();
//                                Class<? extends Temporal> myTemporalClass = r.getValue().iterator().next().getClass();
//                                if (! myTemporalClass.equals(firstTemporalClass))
//                                {
//                                    throw new DateTimeException("Added recurrences Temporal class " + myTemporalClass.getSimpleName() +
//                                            " doesn't match previous recurrences Temporal class " + firstTemporalClass.getSimpleName());
//                                }
//                            }
//                        }
//                    }
//                } else
//                {
//                    System.out.println("ensure matching DTSTART");
//                    getRecurrences().
//                }
//            });
//        }
//    }
    
    
    default void validateRecurrencesConsistencyWithDTStart()
    {
        System.out.println("ensure matching DTSTART");
        if ((getRecurrences() != null) && (getDateTimeStart() != null))
        {
//            final boolean isValid;
            Temporal firstRecurrence = getRecurrences().get(0).getValue().iterator().next();
            DateTimeType recurrenceType = DateTimeUtilities.DateTimeType.of(firstRecurrence);
            DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
            if (recurrenceType != dateTimeStartType)
            {
                throw new DateTimeException("Recurrences DateTimeType (" + recurrenceType +
                        ") must be same as the DateTimeType of DateTimeStart (" + dateTimeStartType + ")");
            }

//            if (firstRecurrence.getClass().equals(dateTimeStart.getClass()))
//            {
//                if (firstRecurrence instanceof ZonedDateTime)
//            } else
//            {
//                isValid = false;
//            }
            
        }
    }
    
    default void setupRecurrenceListeners()
    {
        ChangeListener<? super DateTimeStart<? extends Temporal>> RECURRENCE_TYPE_LISTENER = (observable, oldValue, newValue) -> 
        {
            System.out.println("DTSTART change" + newValue.getClass().getSimpleName());
            if (getRecurrences() != null)
            {
                DateTimeType dtStartType = DateTimeUtilities.DateTimeType.of(newValue.getValue());
                Temporal rDateTemporal = getRecurrences().get(0).getValue().iterator().next();
                DateTimeType rDateType = DateTimeUtilities.DateTimeType.of(rDateTemporal);
                if (dtStartType != rDateType)
                {
                    throw new DateTimeException("DateTimeType of DTSTART (" + dtStartType +") and RDATE (" + rDateType + ") must be the same.");
//                    System.out.println("types different" + newValue.getClass().getSimpleName());
                    
//                    // Convert Recurrences to new DTSTART type -  DON'T USE HERE - USE IN IMPLEMENTATION
//                    ObservableList<Recurrences<? extends Temporal>> newRecurrences = FXCollections.observableArrayList();
//                    getRecurrences().stream().forEach(r ->
//                    {
//                        final Recurrences<? extends Temporal> recurrenceProperty;
//                        switch (dtStartType)
//                        {
//                        case DATE:
//                        {
//                            LocalDate[] values = r.getValue().stream().map(t -> dtStartType.from(t)).toArray(LocalDate[]::new);
//                            recurrenceProperty = new Recurrences<LocalDate>(values);
//                            break;
//                        }
//                        case DATE_WITH_LOCAL_TIME:
//                        {
//                            LocalDateTime[] values = r.getValue().stream().map(t -> dtStartType.from(t)).toArray(LocalDateTime[]::new);
//                            recurrenceProperty = new Recurrences<LocalDateTime>(values);
//                            break;
//                        }
//                        case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
//                        case DATE_WITH_UTC_TIME:
//                        {
//                            ZoneId zone = ((ZonedDateTime) newValue.getValue()).getZone();
//                            ZonedDateTime[] values = r.getValue().stream().map(t -> dtStartType.from(t, zone)).toArray(ZonedDateTime[]::new);
//                            recurrenceProperty = new Recurrences<ZonedDateTime>(values);
//                            break;
//                        }
//                        default:
//                            throw new DateTimeException("Unsupported DateTimeType:" + dtStartType);
//                        }
//                        newRecurrences.add(recurrenceProperty);
//                    });
//                    setRecurrences(newRecurrences);
                }
            }
        };
        dateTimeStartProperty().addListener(RECURRENCE_TYPE_LISTENER);
    }

    
    /**
     * RDATE: Recurrence Date-Times
     * Set of date/times for recurring events, to-dos, journal entries.
     * 3.8.5.2, RFC 5545 iCalendar
     * 
     * Examples:
     * RDATE;TZID=America/New_York:19970714T083000
     * RDATE;VALUE=DATE:19970101,19970120,19970217,19970421
     *  19970526,19970704,19970901,19971014,19971128,19971129,1997122
     */
    ObservableList<Recurrences<? extends Temporal>> getRecurrences();
    void setRecurrences(ObservableList<Recurrences<? extends Temporal>> recurrences);
    default T withRecurrences(ObservableList<Recurrences<? extends Temporal>> recurrences)
    {
        setRecurrences(recurrences);
        return (T) this;
    }
    default T withRecurrences(String...recurrences)
    {
        Arrays.stream(recurrences).forEach(s -> PropertyEnum.RECURRENCE_DATE_TIMES.parse(this, s));   
        return (T) this;
    }
    default T withRecurrences(Temporal...recurrences)
    {
        if (recurrences.length > 0)
        {
            final ObservableList<Recurrences<? extends Temporal>> list;
            if (getRecurrences() == null)
            {
                list = FXCollections.observableArrayList();
                setRecurrences(list);
            } else
            {
                list = getRecurrences();
            }
            
            Temporal t = recurrences[0];
            if (t instanceof LocalDate)
            {
                Set<LocalDate> recurrences2 = Arrays.stream(recurrences).map(r -> (LocalDate) r).collect(Collectors.toSet());
                getRecurrences().add(new Recurrences<LocalDate>(FXCollections.observableSet(recurrences2)));
            } else if (t instanceof LocalDateTime)
            {
                getRecurrences().add(new Recurrences<LocalDateTime>(FXCollections.observableSet((LocalDateTime[]) recurrences)));
            } else if (t instanceof ZonedDateTime)
            {
                getRecurrences().add(new Recurrences<ZonedDateTime>(FXCollections.observableSet((ZonedDateTime[]) recurrences)));
            }
        }
        return (T) this;
    }
    default T withRecurrences(Recurrences<?>...recurrences)
    {
        if (getRecurrences() == null)
        {
            setRecurrences(FXCollections.observableArrayList());
            Arrays.stream(recurrences).forEach(r -> getRecurrences().add(r)); // add one at a time to ensure date-time type compliance
        } else
        {
            getRecurrences().addAll(recurrences);
        }
        return (T) this;
    }

    
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
    default RecurrenceRule getRecurrenceRule() { return recurrenceRuleProperty().get(); }
    ObjectProperty<RecurrenceRule> recurrenceRuleProperty();
    default void setRecurrenceRule(RecurrenceRule recurrenceRule) { recurrenceRuleProperty().set(recurrenceRule); }
    default void setRecurrenceRule(RecurrenceRuleParameter rrule) { setRecurrenceRule(new RecurrenceRule(rrule)); }
    default T withRecurrenceRule(String rrule) { setRecurrenceRule(new RecurrenceRule(rrule)); return (T) this; }
    default T withRecurrenceRule(RecurrenceRule rrule) { setRecurrenceRule(rrule); return (T) this; }
    default T withRecurrenceRule(RecurrenceRuleParameter rrule) { setRecurrenceRule(rrule); return (T) this; }
    
    /** Stream of dates or date-times that indicate the series of start date-times of the event(s).
     * iCalendar calls this series the recurrence set.
     * For a VEvent without RRULE or RDATE the stream will contain only one element.
     * In a VEvent with a RRULE the stream should contain more than one date/time element.  It is possible
     * to define a single-event RRULE, but it is not advisable.  The stream will be infinite 
     * if COUNT or UNTIL is not present.  The stream has an end when COUNT or UNTIL condition is met.
     * The stream starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence.
     * 
     * Start date/times are only produced between the ranges set by setDateTimeRanges
     * 
     * @param startTemporal - start dates or date/times produced after this date.  If not on an occurrence,
     * it will be adjusted to be the next occurrence
     * @return - stream of start dates or date/times for the recurrence set
     */
    Stream<Temporal> streamRecurrences(Temporal startTemporal);

}
