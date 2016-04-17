package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;

/**
 * Describable VComponents
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodoOld
 * @see VJournalOld
 * @see VAlarmOld
 *  */
public interface VComponentDescribable
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
    public ObjectProperty<Attachment> attachmentProperty();
    public Attachment getAttachment();
    public void setAttachment(Attachment attachment);
    
    /**
     * DESCRIPTION:
     * RFC 5545 iCalendar 3.8.1.12. page 84
     * This property provides a more complete description of the
     * calendar component than that provided by the "SUMMARY" property.
     * Example:
     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
     *  design.\nHappy Face Conference Room. Phoenix design team
     *  MUST attend this meeting.\nRSVP to team leader.
     */
    public ObjectProperty<Description> descriptionProperty();
    public Description getDescription();
    public void setDescription(Description description);
    
    /**
     * SUMMARY:
     * RFC 5545 iCalendar 3.8.1.12. page 83
     * This property defines a short summary or subject for the calendar component 
     * Example:
     * SUMMARY:Department Party
     * */
    Summary getSummary();
    ObjectProperty<Summary> summaryProperty();
    void setSummary(Summary summary);
}
