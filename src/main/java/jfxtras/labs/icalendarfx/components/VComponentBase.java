package jfxtras.labs.icalendarfx.components;

import java.util.List;

import javafx.util.Callback;
import jfxtras.labs.icalendarfx.CalendarComponent;
import jfxtras.labs.icalendarfx.VChild;
import jfxtras.labs.icalendarfx.VParent;
import jfxtras.labs.icalendarfx.VParentBase;
import jfxtras.labs.icalendarfx.content.MultiLineContent;
import jfxtras.labs.icalendarfx.properties.Property;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

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
                type.copyProperty((Property<?>) child, this);
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
    public void parseContent(String contentLines)
    {
        if (contentLines == null)
        {
            throw new IllegalArgumentException("Calendar component content string can't be null");
        }
        parseContent(ICalendarUtilities.unfoldLines(contentLines));
    }
    
    /** Parse component from list of unfolded lines */
    public void parseContent(List<String> contentLines)
    {
        for (int index=0; index<contentLines.size(); index++)
        {
            String line = contentLines.get(index);
            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(line);
            String propertyName = line.substring(0, nameEndIndex);
            
            // Parse subcomponent
            if (propertyName.equals("BEGIN"))
            {
                boolean isMainComponent = line.substring(nameEndIndex+1).equals(componentName());
                if  (! isMainComponent)
                {
                    CalendarComponent subcomponentType = CalendarComponent.enumFromName(line.substring(nameEndIndex+1));
                    StringBuilder subcomponentContentBuilder = new StringBuilder(200);
                    subcomponentContentBuilder.append(line + System.lineSeparator());
                    boolean isEndFound = false;
                    do
                    {
                        String subLine = contentLines.get(++index);
                        subcomponentContentBuilder.append(subLine + System.lineSeparator());
                        isEndFound = subLine.subSequence(0, 3).equals("END");
                    } while (! isEndFound);
                    parseSubComponents(subcomponentType, subcomponentContentBuilder.toString());
                }
                
            // parse properties - ignore unknown properties
            } else if (! propertyName.equals("END"))
            {
                PropertyType propertyType = PropertyType.enumFromName(propertyName);
                if (propertyType != null)
                {
//                    propertySortOrder.put(propertyName, sortOrderCounter);
//                    sortOrderCounter += 100; // add 100 to allow insertions in between
                    propertyType.parse(this, line);
                } else
                {
                    // TODO - check IANA properties and X- properties
//                    System.out.println("unknown prop:" );
                    throw new RuntimeException("not implemented");
                }
            }
        }
    }

    /**
     * Parse any subcomponents such as {@link #VAlarm}, {@link #StandardTime} and {@link #DaylightSavingTime}
     * @param subcomponentType 
     * @param string 
     */
    void parseSubComponents(CalendarComponent subcomponentType, String subcomponentcontentLines) { }
    
    @Override
    public String toString()
    {
        return super.toString() + System.lineSeparator() + toContent();
    }
}
