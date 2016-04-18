package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleParameter;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public abstract class VComponentDisplayableBase<T, I> extends VComponentPersonalBase<T> implements VComponentDisplayable<I>, VComponentRepeatable, VComponentDescribable
{
    /**
     * ATTACH
     * Attachment
     * RFC 5545, 3.8.1.1, page 80
     * 
     * This property provides the capability to associate a document object with a calendar component.
     * 
     * Examples:
     * ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com
     * ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/
     *  reports/r-960812.ps
     * */
    @Override
    public ObservableList<Attachment<?>> getAttachments()
    {
        return attachments;
    }
    private ObservableList<Attachment<?>> attachments;
    @Override
    public void setAttachments(ObservableList<Attachment<?>> attachments) { this.attachments = attachments; }
    /** add comma separated attachments into separate comment objects */
    public T withAttachments(ObservableList<Attachment<?>> attachments) { setAttachments(attachments); return (T) this; }
    public T withAttachments(String...attachments)
    {
        Arrays.stream(attachments).forEach(c -> PropertyEnum.ATTACHMENT.parse(this, c));
        return (T) this;
    }
    
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
    @Override
    public void setRecurrences(ObservableList<Recurrences<? extends Temporal>> recurrences) { this.recurrences = recurrences; }
    /** add comma separated recurrences into separate recurrences objects */
    public T withRecurrences(String...recurrences)
    {        
        String recurrencesCollected = Arrays.stream(recurrences).collect(Collectors.joining(","));
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
                ObservableSet<LocalDate> recurrencesCast = (ObservableSet<LocalDate>) recurrences;
                getRecurrences().add(new Recurrences<LocalDate>(recurrencesCast));
            } else if (t instanceof LocalDateTime)
            {
                ObservableSet<LocalDateTime> recurrencesCast = (ObservableSet<LocalDateTime>) recurrences;
                getRecurrences().add(new Recurrences<LocalDateTime>(recurrencesCast));
                
            } else if (t instanceof ZonedDateTime)
            {
                ObservableSet<ZonedDateTime> recurrencesCast = (ObservableSet<ZonedDateTime>) recurrences;
                getRecurrences().add(new Recurrences<ZonedDateTime>(recurrencesCast));
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

    /**
     * SUMMARY
     * RFC 5545 iCalendar 3.8.1.12. page 93
     * 
     * This property defines a short summary or subject for the calendar component.
     * 
     * Example:
     * SUMMARY:Department Party
     */
    @Override public ObjectProperty<Summary> summaryProperty()
    {
        if (summary == null)
        {
            summary = new SimpleObjectProperty<>(this, PropertyEnum.SUMMARY.toString());
        }
        return summary;
    }
    private ObjectProperty<Summary> summary;
    @Override public Summary getSummary() { return summaryProperty().get(); }
    @Override
    public void setSummary(Summary summary) { summaryProperty().set(summary); }
    public T withSummary(Summary summary) { setSummary(summary); return (T) this; }
    public T withSummary(String summary) { PropertyEnum.SUMMARY.parse(this, summary); return (T) this; }

    
    /*
     * CONSTRUCTORS
     */
    public VComponentDisplayableBase() { }
    
    public VComponentDisplayableBase(String contentLines)
    {
        super(contentLines);
    }
}
