package jfxtras.labs.icalendarfx.components;

/**
 * Interface for all top-level calendar elements.  These include
 * calendar components (e.g. VEVENT) and calendar properties (e.g. VERSION).
 * 
 * @author David Bal
 *
 */
public interface VCalendarElement
{
    CharSequence toContent();    
}
