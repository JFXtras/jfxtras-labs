package jfxtras.labs.icalendar.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.properties.Property;
import jfxtras.labs.icalendar.properties.PropertyEnum;
import jfxtras.labs.icalendar.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendar.properties.component.misc.NonStandardProperty;

/**
 * iCalendar component
 * Contains the following properties:
 * Non-Standard Properties, IANA Properties
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see VTimeZone
 * @see VAlarm
 */
public class VComponentBase<T> implements VComponentNew
{
    /**
     * 3.8.8.2.  Non-Standard Properties
     * Any property name with a "X-" prefix
     * 
     * Example:
     * X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.
     *  org/mysubj.au
     */
    @Override
    public ObservableList<NonStandardProperty> getNonStandardProperties()
    {
        if (nonStandardProps == null)
        {
            nonStandardProps = FXCollections.observableArrayList();
        }
        return nonStandardProps;
    }
    private ObservableList<NonStandardProperty> nonStandardProps;
    @Override
    public void setNonStandardProperties(ObservableList<NonStandardProperty> comments) { this.nonStandardProps = comments; }
    /** add comma separated comments into separate comment objects */
    public T withNonStandardProperty(String...comments)
    {
        Arrays.stream(comments).forEach(c -> getNonStandardProperties().add(new NonStandardProperty(c)));
        return (T) this;
    }
    
    /**
     * 3.8.8.1.  IANA Properties
     * An IANA-registered property name
     * 
     * Examples:
     * NON-SMOKING;VALUE=BOOLEAN:TRUE
     * DRESSCODE:CASUAL
     */
    @Override
    public ObservableList<IANAProperty> getIANAProperties()
    {
        if (IANAProps == null)
        {
            IANAProps = FXCollections.observableArrayList();
        }
        return IANAProps;
    }
    private ObservableList<IANAProperty> IANAProps;
    @Override
    public void setIANAProperties(ObservableList<IANAProperty> comments) { this.IANAProps = comments; }
    /** add comma separated comments into separate comment objects */
    public T withIANAProperty(String...comments)
    {
        Arrays.stream(comments).forEach(c -> getIANAProperties().add(new IANAProperty(c)));
        return (T) this;
    }
    
    /**
     * List of all properties found in component.
     * The list is unmodifiable.
     * 
     * @return - the list of properties
     */
    @Override
    public List<PropertyEnum> properties()
    {
        List<PropertyEnum> populatedProperties = new ArrayList<>();
//      System.out.println("parameters:" + propertyType().possibleParameters().size());
        Iterator<PropertyEnum> i = componentType().allowedProperties().stream().iterator();
        while (i.hasNext())
        {
            PropertyEnum propertyType = i.next();
            Object property = propertyType.getProperty(this);
            if (property != null)
            {
                populatedProperties.add(propertyType);
            }
        }
      return Collections.unmodifiableList(populatedProperties);
    }

    /** 
     * Property sort order map.  Key is property name.  Follows sort order of parsed content.
     * If a property is not present in the map, it is put at the end of the sorted ones in
     * the order appearing in {@link #PropertyEnum} <br>
     * Generally, this map shouldn't be modified.  Only modify it when you want to force
     * a specific property order (e.g. unit testing).
     */
    public Map<String, Integer> propertySortOrder() { return propertySortOrder; }
    final private Map<String, Integer> propertySortOrder = new HashMap<>();
    
    /*
     * CONSTRUCTORS
     */
    VComponentBase() { }
    
    /** Parse content lines into calendar component */
    VComponentBase(String contentLines)
    {
        
        Integer counter = 0;
//        Iterator<String> i = Arrays.asList(contentLines.split(System.lineSeparator())).iterator();
        Iterator<String> i = unfoldLines(contentLines).iterator();
        while (i.hasNext())
        {
            String line = i.next();
            List<Integer> indices = new ArrayList<>();
            indices.add(line.indexOf(':'));
            indices.add(line.indexOf(';'));
            int nameEndIndex = indices
                    .stream()
                    .filter(v -> v > 0)
                    .min(Comparator.naturalOrder())
                    .get();
            String propertyName = line.substring(0, nameEndIndex);
            if (! (propertyName.equals("BEGIN") || propertyName.equals("END")))
            {
                propertySortOrder.put(propertyName, counter++);
                PropertyEnum propertyType = PropertyEnum.enumFromName(propertyName);
                
                // parse property
                if (propertyType != null)
                {
                    propertyType.parse(this, line);
                } else if (propertyName.substring(0, PropertyEnum.NON_STANDARD.toString().length()).equals(PropertyEnum.NON_STANDARD.toString()))
                {
                    PropertyEnum.NON_STANDARD.parse(this, line);
                } else if (IANAProperty.REGISTERED_IANA_PROPERTY_NAMES.contains(propertyName))
                {
                    PropertyEnum.IANA_PROPERTY.parse(this, line);
                } else
                {
                    // ignores unknown properties - change if other behavior desired
                }
            }
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        Iterator<PropertyEnum> i = properties().iterator();
        while (i.hasNext())
        {
            Object property = i.next().getProperty(this);
            hash = (31 * hash) + property.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        VComponentBase<?> testObj = (VComponentBase<?>) obj;
        
        final boolean propertiesEquals;
        List<PropertyEnum> properties = properties(); // make properties local to avoid creating list multiple times
        List<PropertyEnum> testProperties = testObj.properties(); // make properties local to avoid creating list multiple times
        if (properties.size() == testProperties.size())
        {
            Iterator<PropertyEnum> i1 = properties.iterator();
            Iterator<PropertyEnum> i2 = testProperties.iterator();
            boolean isFailure = false;
            while (i1.hasNext())
            {
                Object p1 = i1.next().getProperty(this);
                Object p2 = i2.next().getProperty(testObj);
//                System.out.println("p1,p2:" + p1 + " " + p2 + " " + p1.equals(p2));
                if (! p1.equals(p2))
                {
                    isFailure = true;
                    break;
                }
            }
            propertiesEquals = ! isFailure;
        } else
        {
            propertiesEquals = false;
        }
//        System.out.println("equals:" + valueEquals + " " + otherParametersEquals + " " + parametersEquals);
        return propertiesEquals;
    }
    
    @Override
    public String toContentLines()
    {
//        System.out.println("prop num:" + properties().size());
        // make map of property name/content
        Map<String, List<CharSequence>> propertyNameContentMap = new HashMap<>();
        properties().stream()
                .map(e -> e.getProperty(this))
                .flatMap(prop -> 
                {
                    if (prop instanceof Property<?>)
                    {
                        return Arrays.asList((Property<?>) prop).stream();
                    } else if (prop instanceof List<?>)
                    {
                        return ((List<? extends Property<?>>) prop).stream();
                    } else
                    {
                        throw new IllegalArgumentException("Invalid property type:" + prop.getClass());
                    }
                })
                .filter(property -> property != null)
                .forEach(property -> 
                {
                    if (propertyNameContentMap.get(property.getPropertyName()) == null)
                    {
                        List<CharSequence> list = new ArrayList<>(Arrays.asList(property.toContentLine()));
                        propertyNameContentMap.put(property.getPropertyName(), list);
                    } else
                    {
                        List<CharSequence> list = propertyNameContentMap.get(property.getPropertyName());
                        list.add(property.toContentLine());
                    }
                });

        
        StringBuilder builder = new StringBuilder(400);
        builder.append(firstContentLine() + System.lineSeparator());
        
        // restore property sort order if properties were parsed from content
        propertyNameContentMap.entrySet().stream()
                .sorted((Comparator<? super Entry<String, List<CharSequence>>>) (e1, e2) -> 
                {
                    Integer s1 = propertySortOrder.get(e1.getKey());
                    Integer sort1 = (s1 == null) ? Integer.MAX_VALUE : s1;
                    Integer s2 = propertySortOrder.get(e2.getKey());
                    Integer sort2 = (s2 == null) ? Integer.MAX_VALUE : s2;
                    return sort1.compareTo(sort2);
                })
                .forEach(p -> 
                {
                    p.getValue().stream()
                            .forEach(s -> builder.append(foldLine(s) + System.lineSeparator()));
                });

        builder.append(lastContentLine());
        return builder.toString();
    }

    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VEVENT; // for testing
    }

    /**
     * Starting with lines-separated list of content lines, the lines are unwrapped and 
     * converted into a list of property name and property value list wrapped in a Pair.
     * DTSTART property is put on top, if present.
     * 
     * This method unwraps multi-line content lines as defined in iCalendar RFC5545 3.1, page 9
     * 
     * For example:
     * string is
     * BEGIN:VEVENT
     * SUMMARY:test1
     * DTSTART;TZID=Etc/GMT:20160306T080000Z
     * DTEND;TZID=Etc/GMT:20160306T093000Z
     * RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1
     * ORGANIZER;CN=David Bal;SENT-BY="mailto:ddbal1@yahoo.com":mailto:ddbal1@yaho
     *  o.com
     * UID:fc3577e0-8155-4fa2-a085-a15bdc50a5b4
     * DTSTAMP:20160313T053147Z
     * END:VEVENT
     *  
     *  results in list
     *  Element 1, BEGIN:VEVENT
     *  Element 2, SUMMARY:test1
     *  Element 3, DTSTART;TZID=Etc/GMT:20160306T080000Z
     *  Element 4, DTEND;TZID=Etc/GMT:20160306T093000Z
     *  Element 5, RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1
     *  Element 5, ORGANIZER;CN=David Bal;SENT-BY="mailto:ddbal1@yahoo.com":mailto:ddbal1@yahoo.com
     *  Element 6, UID:fc3577e0-8155-4fa2-a085-a15bdc50a5b4
     *  Element 7, DTSTAMP:20160313T053147Z
     *  Element 8, END:VEVENT
     *  
     *  Note: the list of Pair<String,String> is used instead of a Map<String,String> because some properties
     *  can appear more than once resulting in duplicate key values.
     *  
     * @param componentString
     * @return
     */
    // TODO - MOVE THIS TO A UTILITY CLASS
//    final private static char[] SPECIAL_CHARACTERS = new char[] {',' , ';' , '\\' , 'n', 'N' };
//    final private static char[] REPLACEMENT_CHARACTERS = new char[] {',' , ';' , '\\' , '\n', 'n'};
    public static List<String> unfoldLines(String componentString)
    {
        List<String> propertyLines = new ArrayList<>();
        String storedLine = "";
        Iterator<String> lineIterator = Arrays.stream( componentString.split(System.lineSeparator()) ).iterator();
        while (lineIterator.hasNext())
        {
            // unwrap lines by storing previous line, adding to it if next is a continuation
            // when no more continuation lines are found loop back and start with last storedLine
            String startLine;
            if (storedLine.isEmpty())
            {
                startLine = lineIterator.next();
            } else
            {
                startLine = storedLine;
                storedLine = "";
            }
            StringBuilder builder = new StringBuilder(startLine);
            while (lineIterator.hasNext())
            {
                String anotherLine = lineIterator.next();
                if ((anotherLine.charAt(0) == ' ') || (anotherLine.charAt(0) == '\t'))
                { // unwrap anotherLine into line
                    builder.append(anotherLine.substring(1, anotherLine.length()));
                } else
                {
                    storedLine = anotherLine; // save for next iteration
                    break;  // no continuation line, exit while loop
                }
            }
            
//            // UNESCAPE SPECIAL CHARACTERS \ , ; \n (newline)
//            StringBuilder builder2 = new StringBuilder(builder.length()); 
//            for (int i=0; i<builder.length(); i++)
//            {
//                char charToAdd = builder.charAt(i);
//                if (builder.charAt(i) == '\\')
//                {
//                    char nextChar = builder.charAt(i+1);
//                    for (int j=0;j<SPECIAL_CHARACTERS.length; j++)
//                    {
//                        if (nextChar == SPECIAL_CHARACTERS[j])
//                        {
//                            charToAdd = REPLACEMENT_CHARACTERS[j];
//                            i++;
//                            break;
//                        }
//                    }
//                }
//                builder2.append(charToAdd);                    
//            }

            String line = builder.toString();
            propertyLines.add(line);
        }
//        Collections.sort(propertyLines, DTSTART_FIRST_COMPARATOR); // put DTSTART property on top of list (so I can get its Temporal type)
        return propertyLines;
    }
    // SHOULD SPECIAL CHARACTER HANDLING BE IN STRING CONVERTERS? - ONLY FOR TEXT?
    //TODO - HANDLE SPECIAL CHARACTERS
    
    /**
     * Folds lines at character 75 into multiple lines.  Follows rules in
     * RFC 5545, 3.1 Content Lines, page 9.
     * A space is added to the first character of the subsequent lines.
     * does't break lines at escape characters
     * 
     * @param line - content line
     * @return - folded content line
     */
    public static CharSequence foldLine(CharSequence line)
    {
        // first position is 0
        final int maxLineLength = 75;
        if (line.length() <= maxLineLength)
        {
            return line;
        } else
        {
            StringBuilder builder = new StringBuilder(line.length()+20);
            int leadingSpaceAdjustment = 0;
            String leadingSpace = "";
            int startIndex = 0;
            while (startIndex < line.length())
            {
                int endIndex = Math.min(startIndex+maxLineLength-leadingSpaceAdjustment, line.length());
                if (endIndex < line.length())
                {
                    System.out.println(line.charAt(endIndex-1));
                    System.out.println(line.charAt(endIndex-1) == '\\');
                    // ensure escaped characters are not broken up
                    if (line.charAt(endIndex-1) == '\\')
                    {
                        endIndex = endIndex-1; 
                    }
                }
                builder.append(leadingSpace + line.subSequence(startIndex, endIndex) + System.lineSeparator());
                startIndex = endIndex;
                leadingSpaceAdjustment = 1;
                leadingSpace = " ";
            }
            return builder;
        }
    }
    
//    public static CharSequence unfoldLine(CharSequence line)
//    {
//        String storedLine = "";
//        String startLine;
//        if (storedLine.isEmpty())
//        {
//            startLine = lineIterator.next();
//        } else
//        {
//            startLine = storedLine;
//            storedLine = "";
//        }
//        StringBuilder builder = new StringBuilder(startLine);
//        while (lineIterator.hasNext())
//        {
//            String anotherLine = lineIterator.next();
//            if ((anotherLine.charAt(0) == ' ') || (anotherLine.charAt(0) == '\t'))
//            { // unwrap anotherLine into line
//                builder.append(anotherLine.substring(1, anotherLine.length()));
//            } else
//            {
//                storedLine = anotherLine; // save for next iteration
//                break;  // no continuation line, exit while loop
//            }
//        }
//    }
//    
    final private static Comparator<? super String> DTSTART_FIRST_COMPARATOR = (p1, p2) ->
    {
        int endIndex = PropertyEnum.DATE_TIME_START.toString().length();
        String myString = p1.substring(0, endIndex);
        return (myString.equals(PropertyEnum.DATE_TIME_START.toString())) ? -1 : 1;
    };
    
}
