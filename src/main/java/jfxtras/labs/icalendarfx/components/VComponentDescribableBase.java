package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;

/**
 * Going to become a superclass of VEvent, VTodo and VJournal
 * 
 * @author david
 *
 * @param <T>
 * @see VEventNewInt
 * @see VTodoInt
 * Note: not VJournal - allows multiple Summarys
 * 
 * Note: can't be used for VAlarm
 */
@Deprecated

public abstract class VComponentDescribableBase<T> extends VComponentBase<T> implements VComponentDescribable<T>
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
    VComponentDescribableBase() { }
    
    VComponentDescribableBase(String contentLines)
    {
        super(contentLines);
    }
}
