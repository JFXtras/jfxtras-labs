package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.StringProperty;

@Deprecated // put into abstract class when done building VEvent
public interface VEventNewInt<T,I> extends VComponentLocatable<T,I>, VComponentDateTimeEnd<T>
{    
    /**
     * TRANSP: Time Transparency
     * RFC 5545 iCalendar 3.8.2.7. page 101
     * This property defines whether or not an event is transparent to busy time searches.
     * "OPAQUE"- Blocks or opaque on busy time searches.
     * "TRANSPARENT" - Transparent on busy time searches.
     * Default value is OPAQUE
     * 
     * Example:
     * CLASS:PUBLIC
     */
    String getTimeTransparency();
    StringProperty timeTransparencyProperty();
    void setTimeTransparency(String transparency);

}
