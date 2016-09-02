package jfxtras.labs.icalendarfx.components;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.util.Callback;
import jfxtras.labs.icalendarfx.CalendarComponent;
import jfxtras.labs.icalendarfx.VChild;
import jfxtras.labs.icalendarfx.VParent;
import jfxtras.labs.icalendarfx.VParentBase;
import jfxtras.labs.icalendarfx.content.MultiLineContent;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;
import jfxtras.labs.icalendarfx.utilities.UnfoldingStringIterator;;

/**
 * <p>Base class implementation of a {@link VComponent}</p>
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
            { /* Note: if type is null then element is a subcomponent such as a VALARM, STANDARD or DAYLIGHT
               * and copying happens in overridden version of this method in subclasses */
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
        UnfoldingStringIterator unfoldedLineIterator = new UnfoldingStringIterator(contentLines.iterator());
        return parseContent(unfoldedLineIterator, useRequestStatus);
    }

    @Override
    public List<String> parseContent(UnfoldingStringIterator unfoldedLineIterator, boolean collectErrorMessages)
    {
        if (unfoldedLineIterator == null)
        {
            throw new IllegalArgumentException("Calendar component content string can't be null");
        }
        // apply UnfoldingStringIterator decorator
//        UnfoldingStringIterator unfoldedLineIterator = new UnfoldingStringIterator(unfoldingLineIterator);
        
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
                    VComponent subcomponent = SimpleVComponentFactory.emptyVComponent(subcomponentName);
                    List<String> subMessages = subcomponent.parseContent(unfoldedLineIterator, collectErrorMessages);
                    statusMessages.addAll(subMessages);
//                    VComponent subcomponent = SimpleVComponentFactory.newVComponent(subcomponentName, unfoldedLineIterator);
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
                                if (collectErrorMessages)
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
                        if (collectErrorMessages)
                        {
                            statusMessages.add("2." + propertyType.ordinal() + ";Success, property can only occur once in a calendar component.  Subsequent property is ignored;" + unfoldedLine);
                        } else
                        {
                            throw new IllegalArgumentException(propertyType + " can only occur once in a calendar component");
                        }
                    }
                } else
                {
                    if (collectErrorMessages)
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
