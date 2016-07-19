package jfxtras.labs.icalendarfx.components.deleters;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.components.revisors.ChangeDialogOption;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/**
 * Handles deleting one or all recurrences of a {@link VComponentDisplayable}
 * (e.g. {@link VEvent}, {@link VTodo}, {@link VJournal})
 * 
 * @author David Bal
 *
 * @param <U> VComponent class
 */
public class DeleterDisplayable<T, U extends VComponentDisplayable<?>> extends Deleter
{
    private U vComponent;

    public DeleterDisplayable(U vComponent)
    {
        this.vComponent = vComponent;
    }

    public List<U> getVComponents() { return vComponents; }
    private List<U> vComponents;
    public void setVComponents(List<U> vComponents) { this.vComponents = vComponents; }
    public T withVComponents(List<U> vComponents) { setVComponents(vComponents); return (T) this; }

    public Temporal getStartOriginalRecurrence() { return startOriginalRecurrence; }
    private Temporal startOriginalRecurrence;
    public void setStartOriginalRecurrence(Temporal startOriginalRecurrence) { this.startOriginalRecurrence = startOriginalRecurrence; }
    public T withStartOriginalRecurrence(Temporal startOriginalRecurrence) { setStartOriginalRecurrence(startOriginalRecurrence); return (T) this; }
    
    public Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> getDialogCallback() { return dialogCallback; }
    private Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> dialogCallback;    
    public void setDialogCallback(Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> dialogCallback) { this.dialogCallback = dialogCallback; }
    public T withDialogCallback(Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> dialogCallback)
    {
        setDialogCallback(dialogCallback);
        return (T) this;
    }
    
    private boolean isValid()
    {
        if (getStartOriginalRecurrence() == null)
        {
            System.out.println("startOriginalRecurrence must not be null");
            return false;
        }
        if (getDialogCallback() == null)
        {
            System.out.println("dialogCallback must not be null");
            return false;
        }
        if (getVComponents() == null)
        {
            System.out.println("getVComponents must not be null");
            return false;
        }
        return true;   
    }
    
    /** Main method to delete all or part of a VEvent or VTodo or VJournal */
    @Override
    public boolean delete()
    {
        if (! isValid())
        {
            throw new RuntimeException("Invalid parameters for component revision:");
        }
        
        boolean hasRRule = vComponent.getRecurrenceRule() != null;
        if (hasRRule)
        {
            Map<ChangeDialogOption, Pair<Temporal,Temporal>> choices = ChangeDialogOption.makeDialogChoices(
                    vComponent,
                    startOriginalRecurrence);
            ChangeDialogOption changeResponse = dialogCallback.call(choices);
            switch (changeResponse)
            {
            case ALL:
                getVComponents().remove(vComponent);
                return true;
            case CANCEL:
                return false;
            case ONE:
                // Add recurrence to exception list
                getVComponents().remove(vComponent);
                final ExceptionDates exceptionDates;
                if (vComponent.getExceptionDates() == null)
                {
                    exceptionDates = new ExceptionDates(FXCollections.observableSet());
                    vComponent.setExceptionDates(FXCollections.observableArrayList(exceptionDates));
                } else
                {
                    exceptionDates = vComponent.getExceptionDates().get(vComponent.getExceptionDates().size()); // get last ExceptionDate
                }
//                System.out.println("null check1:" + exceptionDates.getValue() + " " + startOriginalRecurrence);
                exceptionDates.getValue().add(startOriginalRecurrence);
                break;
            case THIS_AND_FUTURE:
                // add UNTIL
                Temporal previous = vComponent.previousStreamValue(getStartOriginalRecurrence());
                final Temporal until;
                if (previous.isSupported(ChronoUnit.NANOS))
                {
                    until = DateTimeUtilities.DateTimeType.DATE_WITH_UTC_TIME.from(previous);
                } else
                {
                    until = LocalDate.from(previous);                    
                }
                vComponent.getRecurrenceRule().getValue().setUntil(until);
                getVComponents().remove(vComponent);
                break;
            default:
                throw new RuntimeException("Unsupprted response:" + changeResponse);          
            }
        } else
        { // delete individual component
            getVComponents().remove(vComponent);
            return true;
        }

        if (! vComponent.isValid())
        {
            throw new RuntimeException("Invalid component:" + System.lineSeparator() + 
                    vComponent.errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                    vComponent.toContent());
        }
        getVComponents().add(vComponent);
        return true;
    }
}
