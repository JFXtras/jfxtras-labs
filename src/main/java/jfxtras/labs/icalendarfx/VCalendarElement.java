package jfxtras.labs.icalendarfx;

/**
 * Interface for all calendar elements.  These include
 * calendar components (e.g. VEVENT) and calendar properties (e.g. VERSION).
 * 
 * @author David Bal
 *
 */
public interface VCalendarElement
{
    String toContent();
    
    void parseContent(String content);
}
