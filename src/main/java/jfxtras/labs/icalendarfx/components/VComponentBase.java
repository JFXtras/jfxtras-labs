package jfxtras.labs.icalendarfx.components;

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
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;;

/**
 * Base iCalendar component
 * 
 * @author David Bal
 *
 * @param <T> - concrete subclass
 */
public abstract class VComponentBase extends VParentBase implements VComponent
{
    private static String firstContentLine = "BEGIN:";
    private static String lastContentLine = "END:";
    
    private VParent myParent;
    @Override public void setParent(VParent parent) { myParent = parent; }
    @Override public VParent getParent() { return myParent; }
    
    @Override
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
    public String componentName() { return componentName; }

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
                firstContentLine + componentName,
                lastContentLine + componentName,
                400));
    }
    
    /** Parse content lines into calendar component */
    VComponentBase(String contentLines)
    {
        this();
        parseContent(contentLines);
    }
    
    /** Copy constructor */
    public VComponentBase(VComponentBase source)
    {
        this();
        copyFrom(source);
    }
    
    /** Parse content lines into calendar component */
    @Override
    public void parseContent(String content)
    {
        if (content == null)
        {
            throw new IllegalArgumentException("Calendar component content string can't be null");
        }
        content.indexOf(System.lineSeparator());
        List<String> contentLines = Arrays.asList(content.split(System.lineSeparator()));
        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(contentLines).iterator();
        parseContent(unfoldedLines, false);
        
    }

    /** Parse unfolded content lines into calendar component. */
    @Override
    public List<String> parseContent(Iterator<String> unfoldedLineIterator, boolean useRequestStatus)
    {
        if (unfoldedLineIterator == null)
        {
            throw new IllegalArgumentException("Calendar component content string can't be null");
        }
        List<String> errors = new ArrayList<>();
        while (unfoldedLineIterator.hasNext())
        {
            String unfoldedLine = unfoldedLineIterator.next();
            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(unfoldedLine);
            String propertyName = unfoldedLine.substring(0, nameEndIndex);
            // Parse subcomponent
            if (propertyName.equals("BEGIN"))
            {
                boolean isMainComponent = unfoldedLine.substring(nameEndIndex+1).equals(componentName());
                if  (! isMainComponent)
                {
                    CalendarComponent subcomponentType = CalendarComponent.enumFromName(unfoldedLine.substring(nameEndIndex+1));
                    StringBuilder subcomponentContentBuilder = new StringBuilder(200);
                    subcomponentContentBuilder.append(unfoldedLine + System.lineSeparator());
                    boolean isSubcomponentEndFound = false;
                    do
                    {
                        unfoldedLine = unfoldedLineIterator.next();
                        subcomponentContentBuilder.append(unfoldedLine + System.lineSeparator());
                        isSubcomponentEndFound = unfoldedLine.subSequence(0, 3).equals("END");
                    } while (! isSubcomponentEndFound);
                    parseSubComponents(subcomponentType, subcomponentContentBuilder.toString());
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
                            propertyType.parse(this, unfoldedLine);
                        } catch (Exception e)
                        {
                            if (useRequestStatus)
                            {
                                if (propertyType.isRequired(this))
                                {
                                    errors.add("3." + propertyType.ordinal() + ";Invalid property value;" + unfoldedLine);
                                } else
                                {
                                    errors.add("2." + propertyType.ordinal() + ";Success, Invalid property is ignored;" + unfoldedLine);                                
                                }
                            } else
                            {
                                throw e;
                            }
                        }
                    } else
                    {
                        if (useRequestStatus)
                        {
                            errors.add("2." + propertyType.ordinal() + ";Success, property can only occur once in a calendar component.  Subsequent property is ignored;" + unfoldedLine);
                        } else
                        {
                            throw new IllegalArgumentException(propertyType + " can only occur once in a calendar component");
                        }
                    }
                } else
                {
                    if (useRequestStatus)
                    {
                        errors.add("2.0" + ";Success, unknown property is ignored;" + unfoldedLine);
                    } else
                    {
                        throw new RuntimeException(propertyName + " is not implemented"); // shouldn't get here - unknown property should be used
                    }
                }
            }
        }
        return (useRequestStatus) ? errors : null;
    }
    
//    public void parseContent(List<String> contentLines)
//    {
//        parseContent(contentLines.iterator(), null);
//    }    
    
//    /** Parse component from list of unfolded lines */
//    public void parseContent(List<String> contentLines)
//    {
//        for (int index=0; index<contentLines.size(); index++)
//        {
//            String line = contentLines.get(index).toString();
//            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(line);
//            String propertyName = line.substring(0, nameEndIndex);
//            
//            // Parse subcomponent
//            if (propertyName.equals("BEGIN"))
//            {
//                boolean isMainComponent = line.substring(nameEndIndex+1).equals(componentName());
//                if  (! isMainComponent)
//                {
//                    CalendarComponent subcomponentType = CalendarComponent.enumFromName(line.substring(nameEndIndex+1));
//                    StringBuilder subcomponentContentBuilder = new StringBuilder(200);
//                    subcomponentContentBuilder.append(line + System.lineSeparator());
//                    boolean isEndFound = false;
//                    do
//                    {
//                        String subLine = contentLines.get(++index);
//                        subcomponentContentBuilder.append(subLine + System.lineSeparator());
//                        isEndFound = subLine.subSequence(0, 3).equals("END");
//                    } while (! isEndFound);
//                    parseSubComponents(subcomponentType, subcomponentContentBuilder.toString());
//                }
//                
//            // parse properties - ignore unknown properties
//            } else if (! propertyName.equals("END"))
//            {
//                PropertyType propertyType = PropertyType.enumFromName(propertyName);
//                if (propertyType != null)
//                {
//                    Object existingProperty = propertyType.getProperty(this);
//                    if (existingProperty == null || existingProperty instanceof List)
//                    {
//                        propertyType.parse(this, line);
//                        // TODO - CHECK NEW PROPERTY FOR VALIDITY - IF REQUIRED LOG SEVERE AND FAIL
//                    } else
//                    {
//                        // THIS IMPLEMENTATION WILL NOT WORK WITH MULTI-THREADING, pass logger?
//                        LOGGER.log(Level.WARNING, "2." + propertyType.ordinal() + 
//                                ";Success, property can only occur once in a calendar component.  Subsequent property is ignored;" + line);
////                        throw new IllegalArgumentException(propertyType + " can only occur once in a calendar component");
//                    }
//                } else
//                {
//                    LOGGER.log(Level.WARNING, "2.0" + ";Success, unknown property is ignored;" + line);
////                    throw new RuntimeException(propertyName + " is not implemented"); // shouldn't get here - unknown property should be used
//                }
//            }
//        }
//    }

    /**
     * Parse any subcomponents such as {@link #VAlarm}, {@link #StandardTime} and {@link #DaylightSavingTime}
     * @param subcomponentType 
     * @param string 
     */
    void parseSubComponents(CalendarComponent subcomponentType, String subcomponentContentLines) { } // no opp

    /**
     * Parse any subcomponents such as {@link #VAlarm}, {@link #StandardTime} and {@link #DaylightSavingTime}
     * @param subcomponentType 
     * @param string 
     */
    void parseSubComponents(CalendarComponent subcomponentType, Iterator<String> subcomponentContentIterator) { } // no opp

        
    @Override
    public String toString()
    {
        return super.toString() + System.lineSeparator() + toContent();
    }
}
