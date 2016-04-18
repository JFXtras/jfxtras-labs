package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
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
 * @see VTodo
 * Note: not VJournal - allows multiple Descriptions
 * 
 * Note: can't be used for VAlarm
 */
public abstract class VComponentDescribableBase<T> extends VComponentBase<T> implements VComponentDescribable
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

 
    @Override
    public Summary getSummary()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<Summary> summaryProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSummary(Summary summary)
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * CONSTRUCTORS
     */
    VComponentDescribableBase() { }
    
    VComponentDescribableBase(String contentLines)
    {
        super(contentLines);
    }
}
