package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
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
    default T withAttachments(String...attachments)
    {
        Arrays.stream(attachments).forEach(c -> PropertyEnum.ATTACHMENT.parse(this, c));
        return (T) this;
    }
    
//    /**
//     * DESCRIPTION:
//     * RFC 5545 iCalendar 3.8.1.12. page 84
//     * This property provides a more complete description of the
//     * calendar component than that provided by the "SUMMARY" property.
//     * Example:
//     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
//     *  design.\nHappy Face Conference Room. Phoenix design team
//     *  MUST attend this meeting.\nRSVP to team leader.
//     */
//    public ObjectProperty<Description> descriptionProperty();
//    public Description getDescription();
//    public void setDescription(Description description);
    
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
