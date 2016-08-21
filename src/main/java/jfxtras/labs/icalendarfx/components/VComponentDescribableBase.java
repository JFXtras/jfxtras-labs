package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;

/**
 * 
 * @author David Bal
 *
 * @param <T> - concrete subclass
 * 
 * Components
 * @see VEvent
 * @see VTodo
 * @see VAlarm
 * @see VJournal
 * 
 */
public abstract class VComponentDescribableBase<T> extends VComponentCommonBase<T> implements VComponentDescribable<T>
{
    /**
     * This property provides the capability to associate a document object with a calendar component.
     */
    @Override
    public ObjectProperty<ObservableList<Attachment<?>>> attachmentsProperty()
    {
        if (attachments == null)
        {
            attachments = new SimpleObjectProperty<>(this, PropertyType.ATTACHMENT.toString());
        }
        return attachments;
    }
    @Override
    public ObservableList<Attachment<?>> getAttachments()
    {
        return (attachments == null) ? null : attachments.get();
    }
    private ObjectProperty<ObservableList<Attachment<?>>> attachments;
    @Override
    public void setAttachments(ObservableList<Attachment<?>> attachments)
    {
        if (attachments != null)
        {
            orderer().registerSortOrderProperty(attachments);
        } else
        {
            orderer().unregisterSortOrderProperty(attachmentsProperty().get());
        }
        attachmentsProperty().set(attachments);
    }
    
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
            summary = new SimpleObjectProperty<>(this, PropertyType.SUMMARY.toString());
            orderer().registerSortOrderProperty(summary);
        }
        return summary;
    }
    @Override
    public Summary getSummary() { return (summary == null) ? null : summaryProperty().get(); }
    private ObjectProperty<Summary> summary;
    
    /*
     * CONSTRUCTORS
     */
    VComponentDescribableBase() { }
    
//    VComponentDescribableBase(String contentLines)
//    {
//        super(contentLines);
//    }

    public VComponentDescribableBase(VComponentDescribableBase<T> source)
    {
        super(source);
    }
}
