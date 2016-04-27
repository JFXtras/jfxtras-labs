package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;

/**
 * Describable VComponents
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodoOld
 * @see VJournal
 * @see VAlarm
 *  */
public interface VComponentDescribable<T> extends VComponentNew<T>
{
    /**
     * ATTACH: Attachment
     * RFC 5545 iCalendar 3.8.1.1. page 80
     * This property provides the capability to associate a
     * document object with a calendar component.
     * 
     * Example:
     * ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/
     *  reports/r-960812.p
     */
    ObservableList<Attachment<?>> getAttachments();
    void setAttachments(ObservableList<Attachment<?>> properties);
    /** add comma separated attachments into separate comment objects */
    default T withAttachments(ObservableList<Attachment<?>> attachments) { setAttachments(attachments); return (T) this; }
//    default T withAttachments(String...attachments)
//    {
//        Arrays.stream(attachments).forEach(c -> PropertyEnum.ATTACHMENT.parse(this, c));
//        return (T) this;
//    }
    default T withAttachments(Attachment<?>...attachments)
    {
        if (getAttachments() == null)
        {
            setAttachments(FXCollections.observableArrayList(attachments));
        } else
        {
            getAttachments().addAll(attachments);
        }
        return (T) this;
    }
    
    /**
     * SUMMARY:
     * RFC 5545 iCalendar 3.8.1.12. page 83
     * This property defines a short summary or subject for the calendar component 
     * Example:
     * SUMMARY:Department Party
     * */
    ObjectProperty<Summary> summaryProperty();
    default Summary getSummary() { return summaryProperty().get(); }
    default void setSummary(Summary summary) { summaryProperty().set(summary); }
    default T withSummary(Summary summary) { setSummary(summary); return (T) this; }
    default T withSummary(String summary) { PropertyEnum.SUMMARY.parse(this, summary); return (T) this; }
}
