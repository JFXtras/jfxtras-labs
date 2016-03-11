package jfxtras.labs.icalendar.components;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.util.Pair;
import jfxtras.labs.icalendar.DateTimeUtilities;
import jfxtras.labs.icalendar.components.VEvent.EndType;

/**
 * VEvent specific properties with the following data and methods:
 * iCalendar property name
 * setVComponent - parse string method
 * makeContentLine - toString method
 * isPropertyEqual - tests equality for the property between to VEvents
 * 
 * @author David Bal
 *
 */
public enum VEventProperty
{
    /**
     * DESCRIPTION: RFC 5545 iCalendar 3.8.1.12. page 84
     * This property provides a more complete description of the
     * calendar component than that provided by the "SUMMARY" property.
     * Example:
     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
     *  design.\nHappy Face Conference Room. Phoenix design team
     *  MUST attend this meeting.\nRSVP to team leader.
     */
    DESCRIPTION ("DESCRIPTION", true)
    {
        @Override
        public void parseAndSetProperty(VEvent<?,?> vEvent, String value)
        {
            if (vEvent.getDescription() == null)
            {
                vEvent.setDescription(value);
            } else
            {
                throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
            }
        }

        @Override
        public Object getPropertyValue(VEvent<?,?> vEvent)
        {
            return vEvent.getDescription();
        }

        @Override
        public String makeContentLine(VEvent<?,?> vEvent)
        {
            return ((vEvent.getDescription() == null) || (vEvent.getDescription().isEmpty())) ? null : vEvent.descriptionProperty().getName()
                    + ":" + vEvent.getDescription();
        }

        @Override
        public boolean isPropertyEqual(VEvent<?,?> v1, VEvent<?,?> v2)
        {
            return (v1.getDescription() == null) ? (v2.getDescription() == null) : v1.getDescription().equals(v2.getDescription());
        }

        @Override
        public void copyProperty(VEvent<?,?> source, VEvent<?,?> destination)
        {
            destination.setDescription(source.getDescription());
        }
    } 
    /** 
     * DURATION from RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Can't be used if DTEND is used.  Must be one or the other.
     * */
  , DURATION ("DURATION", true)
    {
        @Override
        public void parseAndSetProperty(VEvent<?,?> vEvent, String value)
        {
            if (vEvent.getDuration() == null)
            {
                if (vEvent.getDateTimeEnd() == null)
                {
                    vEvent.endPriority = EndType.DURATION;
                    vEvent.setDuration(Duration.parse(value));
                } else
                {
                    throw new IllegalArgumentException("Invalid VEvent: Can't contain both DTEND and DURATION.");
                }
            } else
            {
                throw new IllegalArgumentException(toString() + " can only appear once in calendar component");
            }
        }

        @Override
        public Object getPropertyValue(VEvent<?,?> vEvent)
        {
            return vEvent.getDuration();
        }
        
        @Override
        public String makeContentLine(VEvent<?,?> vEvent)
        {
            if (vEvent.getDuration() == null)
            {
                return null;
            } else if (vEvent.endPriority == EndType.DURATION)
            {
                return vEvent.durationProperty().getName() + ":" + vEvent.getDuration();
            } else
            {
                throw new RuntimeException("DURATION and EndPriority don't match");                
            }
        }

        @Override
        public boolean isPropertyEqual(VEvent<?,?> v1, VEvent<?,?> v2)
        {
            return (v1.getDuration() == null) ? (v2.getDuration() == null) : v1.getDuration().equals(v2.getDuration());
        }

        @Override
        public void copyProperty(VEvent<?,?> source, VEvent<?,?> destination)
        {
            destination.setDuration(source.getDuration());
        }
    } 
  /**
   * DTEND, Date-Time End. from RFC 5545 iCalendar 3.8.2.2 page 95
   * Specifies the date and time that a calendar component ends.
   * Can't be used if DURATION is used.  Must be one or the other.
   * Must be same Temporal type as dateTimeStart (DTSTART)
   */
  , DATE_TIME_END ("DTEND", true)
    {
        @Override
        public void parseAndSetProperty(VEvent<?,?> vEvent, String value)
        {
            if (vEvent.getDateTimeEnd() == null)
            {
                if (vEvent.getDuration() == null)
                {
                    vEvent.endPriority = EndType.DTEND;
                    Temporal dateTime = DateTimeUtilities.parse(value);
                    vEvent.setDateTimeEnd(dateTime);
                } else
                {
                    throw new IllegalArgumentException("Invalid VEvent: Can't contain both DTEND and DURATION.");
                }
            } else
            {
                throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                
            }
        }

        @Override
        public Object getPropertyValue(VEvent<?,?> vEvent)
        {
            return vEvent.getDateTimeEnd();
        }

        @Override
        public String makeContentLine(VEvent<?,?> vEvent)
        {
            if (vEvent.getDateTimeEnd() == null)
            {
                return null;
            } else if (vEvent.endPriority == EndType.DTEND)
            {
                String tag = DateTimeUtilities.dateTimePropertyTag(vEvent.dateTimeEndProperty().getName(), vEvent.getDateTimeEnd());
                return tag + DateTimeUtilities.format(vEvent.getDateTimeEnd());
            } else
            {
                throw new RuntimeException("DTEND and EndPriority don't match");
            }
        }

        @Override
        public boolean isPropertyEqual(VEvent<?,?> v1, VEvent<?,?> v2)
        {
            return (v1.getDateTimeEnd() == null) ? (v2.getDateTimeEnd() == null) : v1.getDateTimeEnd().equals(v2.getDateTimeEnd());
        }

        @Override
        public void copyProperty(VEvent<?,?> source, VEvent<?,?> destination)
        {
            destination.setDateTimeEnd(source.getDateTimeEnd());
        }
    }
  /**
   * LOCATION: RFC 5545 iCalendar 3.8.1.12. page 87
   * This property defines the intended venue for the activity
   * defined by a calendar component.
   * Example:
   * LOCATION:Conference Room - F123\, Bldg. 002
   */
  , LOCATION ("LOCATION", true)
    {
        @Override
        public void parseAndSetProperty(VEvent<?,?> vEvent, String value)
        {
            vEvent.setLocation(value);
        }

        @Override
        public Object getPropertyValue(VEvent<?,?> vEvent)
        {
            return vEvent.getLocation();
        }
        
        @Override
        public String makeContentLine(VEvent<?,?> vEvent)
        {
            return ((vEvent.getLocation() == null) || (vEvent.getLocation().isEmpty())) ? null : vEvent.locationProperty().getName()
                    + ":" + vEvent.getLocation();
        }

        @Override
        public boolean isPropertyEqual(VEvent<?,?> v1, VEvent<?,?> v2)
        {
            return (v1.getLocation() == null) ? (v2.getLocation() == null) : v1.getLocation().equals(v2.getLocation());
        }

        @Override
        public void copyProperty(VEvent<?,?> source, VEvent<?,?> destination)
        {
            destination.setLocation(source.getLocation());
        }
    };

    // Map to match up string tag to ICalendarProperty enum
    private static Map<String, VEventProperty> propertyFromTagMap = makePropertiesFromNameMap();
    private static Map<String, VEventProperty> makePropertiesFromNameMap()
    {
        Map<String, VEventProperty> map = new HashMap<>();
        VEventProperty[] values = VEventProperty.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    private String name;
    /* indicates if providing a dialog to allow user to confirm edit is required. 
     * False means no confirmation is required or property is only modified by the implementation, not by the user */
    boolean dialogRequired;
    
    VEventProperty(String name, boolean dialogRequired)
    {
        this.name = name;
        this.dialogRequired = dialogRequired;
    }
    
    @Override
    public String toString() { return name; }
    public boolean isDialogRequired() { return dialogRequired; }
    
    /** get VComponentProperty enum from property name */
    public static VEventProperty propertyFromString(String propertyName)
    {
        return propertyFromTagMap.get(propertyName.toUpperCase());
    }
    
    /*
     * ABSTRACT METHODS
     */
    
    /** sets VEvent's property for this VEventProperty to parameter value
     * value is a string that is parsed if necessary to the appropriate type
     */
    public abstract void parseAndSetProperty(VEvent<?,?> vEvent, String value);

    /** gets VEvent's property value for this VEventProperty */
    public abstract Object getPropertyValue(VEvent<?,?> vEvent);
    
    /** makes content line (RFC 5545 3.1) from a VEvent property  */
    public abstract String makeContentLine(VEvent<?,?> vEvent);
    
    /** Checks is corresponding property is equal between v1 and v2 */
    public abstract boolean isPropertyEqual(VEvent<?,?> v1, VEvent<?,?> v2);
    
    /** Copies property value from one v1 to v2 */
    public abstract void copyProperty(VEvent<?,?> source, VEvent<?,?> destination);
    
    /*
     * STATIC METHODS
     */
    
    /**
     * Tests equality between two VEvent objects.  Treats v1 as expected.  Produces a JUnit-like
     * output if objects are not equal.
     * 
     * @param v1 - expected VEvent
     * @param v2 - actual VEvent
     * @param verbose - true = display list of unequal properties, false no display output
     * @return - equality result
     */
    public static <T> boolean isEqualTo(VEvent<?,?> v1, VEvent<?,?> v2, boolean verbose)
    {
        boolean vComponentResult = VComponentProperty.isEqualTo(v1, v2, verbose);
        List<String> changedProperties = new ArrayList<>();
        Arrays.stream(VEventProperty.values())
        .forEach(p -> 
        {
            if (! (p.isPropertyEqual(v1, v2)))
            {
                changedProperties.add(p.toString() + " not equal:" + p.getPropertyValue(v1) + " " + p.getPropertyValue(v2));
            }
        });

        if (changedProperties.size() == 0)
        {
            return vComponentResult;
        } else
        {
            if (verbose)
            {
            System.out.println(changedProperties.stream().collect(Collectors.joining(System.lineSeparator())));
            }
            return false;
        }
    }
    
    /**
     * Parses the property-value pair to the matching property, if a match is found.
     * If no matching property, does nothing.
     * 
     * @param vEvent - object to add property values
     * @param propertyValuePair - property name-value pair (e.g. DTSTART and TZID=America/Los_Angeles:20160214T110000)
     */
    public static void parse(VEvent<?,?> vEvent, Pair<String, String> propertyValuePair)
    {
        String propertyName = propertyValuePair.getKey();
        String value = propertyValuePair.getValue();
        
        // VEvent properties
        VEventProperty vEventProperty = VEventProperty.propertyFromString(propertyName);
        if (vEventProperty != null)
        {
            vEventProperty.parseAndSetProperty(vEvent, value);
        }
    }
}