package jfxtras.labs.icalendarfx.components;


import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;

@Deprecated // put into abstract class when done building VTodo
public interface VJournalInt
{
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
    ObservableList<Description> getDescriptions();
    void setDescriptions(ObservableList<Description> properties);}
