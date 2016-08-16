package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jfxtras.labs.icalendarfx.CalendarComponent;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

/**
 * <p>Parses calendar content text to a {@link VComponent}</p>
 * 
 * <p>Creates the correct calendar component depending on the text following the "BEGIN" keyword.
 * For example BEGIN:VEVENT will result in a new {@link VEvent}</p>
 * 
 * @author David Bal
 *
 */
public class SimpleVComponentFactory
{
    /** Create new VComponent from component name and parsing iCalendar content text */
    public static VComponent newVComponent(String componentName, Iterator<String> contentIterator, List<String> errors)
    {
        final VComponent myComponent;
        CalendarComponent component = CalendarComponent.enumFromName(componentName.toString());
        if (component == null)
        {
            throw new IllegalArgumentException(componentName + " is not a valid calendar component name.");            
        }
        switch (component)
        {
        case VEVENT:
            myComponent = new VEvent();
            break;
        case VFREEBUSY:
            myComponent = new VFreeBusy();
            break;
        case VJOURNAL:
            myComponent = new VJournal();
            break;
        case VTIMEZONE:
            myComponent = new VTimeZone();
            break;
        case VTODO:
            myComponent = new VTodo();
            break;
        default:
            throw new IllegalArgumentException("Unsupported component:" + component);
        }
        myComponent.parseContent(contentIterator, errors);
        return myComponent;
    }
    
    /** Create new VComponent by parsing iCalendar content text */
    public static VComponent newVComponent(Iterator<String> contentIterator)
    {
        final VComponent myComponent;
        String line = contentIterator.next();
        int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(line);
        String propertyName = line.substring(0, nameEndIndex);
        
        // Parse component
        if ("BEGIN".contentEquals(propertyName))
        {
            // make new component
            String componentName = line.substring(nameEndIndex+1, line.length());
            myComponent = newVComponent(componentName, contentIterator, null);
//            CalendarComponent component = CalendarComponent.enumFromName(componentName);
//            switch (component)
//            {
//            case VEVENT:
//                myComponent = new VEvent();
//                break;
//            case VFREEBUSY:
//                myComponent = new VFreeBusy();
//                break;
//            case VJOURNAL:
//                myComponent = new VJournal();
//                break;
//            case VTIMEZONE:
//                myComponent = new VTimeZone();
//                break;
//            case VTODO:
//                myComponent = new VTodo();
//                break;
//            default:
//                throw new IllegalArgumentException("Unsupported component:" + component);
//            }
//            myComponent.parseContent(contentIterator);
        } else
        {
            throw new IllegalArgumentException("First content line MUST start with \"BEGIN\" not:" + line);
        }        
        return myComponent;
    }
    
    public static VComponent newVComponent(String contentText)
    {
        List<String> contentLines = Arrays.asList(contentText.split(System.lineSeparator()));
        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(contentLines).iterator();
        return newVComponent(unfoldedLines);
    }
}
