package jfxtras.labs.icalendarfx.components;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javafx.util.Callback;
import jfxtras.labs.icalendarfx.CalendarComponent;
import jfxtras.labs.icalendarfx.VChild;
import jfxtras.labs.icalendarfx.VParent;
import jfxtras.labs.icalendarfx.VParentBase;
import jfxtras.labs.icalendarfx.content.MultiLineContent;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.Method;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;;

/**
 * <h2>RFC 5545, 3.6. Calendar Components<h2>
 * 
 * <p>The body of the iCalendar object consists of a sequence of calendar
 * properties and one or more calendar components.  The calendar
 * properties are attributes that apply to the calendar object as a
 * whole.  The calendar components are collections of properties that
 * express a particular calendar semantic.  For example, the calendar
 * component can specify an event, a to-do, a journal entry, time zone
 * information, free/busy time information, or an alarm.</p>
 *
 * <p>The body of the iCalendar object is defined by the following
 * notation:
 *
 *<ul>
 *<li>icalbody   = calprops component
 *<li>calprops
 *  <ul>
 *  <li>The following are REQUIRED, but MUST NOT occur more than once.
 *    <ul>
 *    <li>{@link ProductIdentifier PRODID}
 *    <li>{@link Version VERSION}
 *    </ul>
 *  </ul>
 *  <ul>
 *  <li>The following are OPTIONAL, but MUST NOT occur more than once.
 *    <ul>
 *    <li>{@link CalendarScale CALSCALE}
 *    <li>{@link Method METHOD}
 *    </ul>
 *  </ul>
 *  <ul>
 *  <li>The following are OPTIONAL, and MAY occur more than once.
 *    <ul>
 *    <li>{@link NonStandardProperty X-PROP}
 *    <li>{@link IANAProperty IANA-PROP}
 *    </ul>
 *  </ul>
 *<li>component
 *  <ul>
 *  <li>{@link VEvent VEVENT}
 *  <li>{@link VTodo VTODO}
 *  <li>{@link VJournal VJOURNAL}
 *  <li>{@link VFreeBusy VFREEBUSY}
 *  <li>{@link VTimeZone VTIMEZONE}
 *  <li>IANA-Comp (not implemented)
 *  <li>X-Comp (not implemented)
 *  </ul>
 *</ul>
 *
 * <P>An iCalendar object MUST include the {@link ProductIdentifier PRODID} and {@link Version VERSION} calendar
 * properties.  In addition, it MUST include at least one calendar
 * component.  Special forms of iCalendar objects are possible to
 * publish just busy time (i.e., only a {@link VFreeBusy VFREEBUSY} calendar component)
 * or time zone (i.e., only a {@link VTimeZone VTIMEZONE} calendar component)
 * information.  In addition, a complex iCalendar object that is used to
 * capture a complete snapshot of the contents of a calendar is possible
 * (e.g., composite of many different calendar components).  More
 * commonly, an iCalendar object will consist of just a single {@link VEvent VEVENT},
 * {@link VTodo VTODO}, or {@link VJournal VJOURNAL} calendar component.  Applications MUST ignore
 * x-comp and iana-comp values they don't recognize.  Applications that
 * support importing iCalendar objects SHOULD support all of the
 * component types defined in this document, and SHOULD NOT silently
 * drop any components as that can lead to user data loss.</P
 * 
 * @author David Bal
 */
public abstract class VComponentBase extends VParentBase implements VComponent
{
    private static final String FIRST_LINE_PREFIX = "BEGIN:";
    private static final String LAST_LINE_PREFIX = "END:";
    
    private VParent myParent;
    @Override public void setParent(VParent parent) { myParent = parent; }
    @Override public VParent getParent() { return myParent; }
    
    public void copyFrom(VComponent source)
    {
        myParent = source.getParent();
        copyChildrenFrom(source);
    }
        
    @Override
    protected Callback<VChild, Void> copyChildCallback()
    {        
        return (child) ->
        {
            PropertyType type = PropertyType.enumFromClass(child.getClass());
            if (type != null)
            { // Note: if type is null then element is a subcomponent such as a VALARM, STANDARD or DAYLIGHT and copying happens in subclasses
                type.copyProperty(child, this);
            }
            return null;
        };
    }
    
    final private String componentName;
    @Override
    public String name() { return componentName; }

    /*
     * CONSTRUCTORS
     */
    /**
     * Create default component by setting {@link componentName}, and setting content line generator.
     */
    VComponentBase()
    {
        componentName = CalendarComponent.enumFromClass(this.getClass()).toString();
        setContentLineGenerator(new MultiLineContent(
                orderer(),
                FIRST_LINE_PREFIX + componentName,
                LAST_LINE_PREFIX + componentName,
                400));
    }
    
//    /** Parse content lines into calendar component */
//    VComponentBase(String contentLines)
//    {
//        this();
//        parseContent(contentLines);
//    }
    
    /**
     * Creates a deep copy of a component
     */
    VComponentBase(VComponentBase source)
    {
        this();
        copyFrom(source);
    }
    
    @Override
    public List<String> parseContent(String content)
    {
        return parseContent(content, false);        
    }
    
    public List<String> parseContent(String content, boolean useRequestStatus)
    {
        if (content == null)
        {
            throw new IllegalArgumentException("Calendar component content string can't be null");
        }
        content.indexOf(System.lineSeparator());
        List<String> contentLines = Arrays.asList(content.split(System.lineSeparator()));
        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(contentLines).iterator();
        return parseContent(unfoldedLines, useRequestStatus);        
    }

    @Override
    public List<String> parseContent(Iterator<String> unfoldedLineIterator, boolean useRequestStatus)
    {
        if (unfoldedLineIterator == null)
        {
            throw new IllegalArgumentException("Calendar component content string can't be null");
        }
        
        // handle exceptions in JavxFx threads by rethrowing
        Thread t = Thread.currentThread();
        UncaughtExceptionHandler originalExceptionHandler = t.getUncaughtExceptionHandler();
        t.setUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
        List<String> statusMessages = new ArrayList<>();
        while (unfoldedLineIterator.hasNext())
        {
            String unfoldedLine = unfoldedLineIterator.next();
            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(unfoldedLine);
            String propertyName = (nameEndIndex > 0) ? unfoldedLine.substring(0, nameEndIndex) : "";
            // Parse subcomponent
            if (propertyName.equals("BEGIN"))
            {
                boolean isMainComponent = unfoldedLine.substring(nameEndIndex+1).equals(name());
                if  (! isMainComponent)
                {
                    String subcomponentName = unfoldedLine.substring(nameEndIndex+1);
                    VComponent subcomponent = SimpleVComponentFactory.newVComponent(subcomponentName, unfoldedLineIterator);
                    addSubcomponent(subcomponent);
                }
            } else if (propertyName.equals("END"))
            {
                break; // exit when end found
            } else
            {  // parse properties - ignore unknown properties
                PropertyType propertyType = PropertyType.enumFromName(propertyName);
                if (propertyType != null)
                {
                    Object existingProperty = propertyType.getProperty(this);
                    if (existingProperty == null || existingProperty instanceof List)
                    {
                        try
                        {
                            try
                            {
                                propertyType.parse(this, unfoldedLine);
                            } catch (Exception e)
                            {
                                if (useRequestStatus)
                                {
                                    if (propertyType.isRequired(this))
                                    {
                                        statusMessages.add("3." + propertyType.ordinal() + ";Invalid property value;" + unfoldedLine);
                                    } else
                                    {
                                        statusMessages.add("2." + propertyType.ordinal() + ";Success, Invalid property is ignored;" + unfoldedLine);                                
                                    }
                                } else
                                {
                                    throw e;
                                }
                            }
                        } catch (Exception e)
                        { // exceptions from JavaFX thread
                            if (propertyType.isRequired(this))
                            {
                                statusMessages.add("3." + propertyType.ordinal() + ";Invalid property value;" + unfoldedLine);
                            } else
                            {
                                statusMessages.add("2." + propertyType.ordinal() + ";Success, Invalid property is ignored;" + unfoldedLine);                                
                            }
                        }
                    } else
                    {
                        if (useRequestStatus)
                        {
                            statusMessages.add("2." + propertyType.ordinal() + ";Success, property can only occur once in a calendar component.  Subsequent property is ignored;" + unfoldedLine);
                        } else
                        {
                            throw new IllegalArgumentException(propertyType + " can only occur once in a calendar component");
                        }
                    }
                } else
                {
                    if (useRequestStatus)
                    {
                        statusMessages.add("2.0" + ";Success, unknown property is ignored;" + unfoldedLine);
                    } else
                    {
                        // unknown line
                        throw new IllegalArgumentException("Unknown property can't be processed:" + unfoldedLine);
                    }
                }
            }
        }
        if (statusMessages.isEmpty())
        {
            statusMessages.add("2.0;Success");
        }
        t.setUncaughtExceptionHandler(originalExceptionHandler); // return original exception handler
        return statusMessages;
    }

    /**
     * Hook to add subcomponent such as {@link #VAlarm}, {@link #StandardTime} and {@link #DaylightSavingTime}
     * 
     * @param subcomponent
     */
    void addSubcomponent(VComponent subcomponent) { };
            
    @Override
    public String toString()
    {
        return super.toString() + System.lineSeparator() + toContent();
    }
}
