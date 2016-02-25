package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent.EndPriority;

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
        public boolean parseAndSetProperty(VEvent<?> vEvent, String value)
        {
            if (vEvent.getDescription() == null)
            {
                vEvent.setDescription(value);
                return true;
            } else
            {
                throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
            }
        }

        @Override
        public Object getPropertyValue(VEvent<?> vEvent)
        {
            return vEvent.getDescription();
        }

        @Override
        public String makeContentLine(VEvent<?> vEvent)
        {
            return ((vEvent.getDescription() == null) || (vEvent.getDescription().isEmpty())) ? null : vEvent.descriptionProperty().getName()
                    + ":" + vEvent.getDescription();
        }

        @Override
        public boolean isPropertyEqual(VEvent<?> v1, VEvent<?> v2)
        {
            return (v1.getDescription() == null) ? (v2.getDescription() == null) : v1.getDescription().equals(v2.getDescription());
        }
    } 
    /** 
     * DURATION from RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Can't be used if DTEND is used.  Must be one or the other.
     * */
  , DURATION ("DURATION", true)
    {
        @Override
        public boolean parseAndSetProperty(VEvent<?> vEvent, String value)
        {
            if (vEvent.getDuration() == null)
            {
                if (vEvent.getDateTimeEnd() == null)
                {
                    vEvent.endPriority = EndPriority.DURATION;
                    vEvent.setDuration(Duration.parse(value));
                    return true;
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
        public Object getPropertyValue(VEvent<?> vEvent)
        {
            return vEvent.getDuration();
        }
        
        @Override
        public String makeContentLine(VEvent<?> vEvent)
        {
            return (vEvent.getDuration() == null) ? null : vEvent.durationProperty().getName() + ":"
                    + vEvent.getDuration();
        }

        @Override
        public boolean isPropertyEqual(VEvent<?> v1, VEvent<?> v2)
        {
            return (v1.getDuration() == null) ? (v2.getDuration() == null) : v1.getDuration().equals(v2.getDuration());
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
        public boolean parseAndSetProperty(VEvent<?> vEvent, String value)
        {
            if (vEvent.getDateTimeEnd() == null)
            {
                if (vEvent.getDuration() == null)
                {
                    vEvent.endPriority = EndPriority.DTEND;
                    Temporal dateTime = VComponent.parseTemporal(value);
                    vEvent.setDateTimeEnd(dateTime);
                    return true;
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
        public Object getPropertyValue(VEvent<?> vEvent)
        {
            return vEvent.getDateTimeEnd();
        }

        @Override
        public String makeContentLine(VEvent<?> vEvent)
        {
            if (vEvent.getDateTimeEnd() == null)
            {
                return null;
            } else
            {
                String tag = VComponent.makeDateTimePropertyTag(vEvent.dateTimeEndProperty().getName(), vEvent.getDateTimeEnd());
                return tag + VComponent.temporalToString(vEvent.getDateTimeEnd());
            }
        }

        @Override
        public boolean isPropertyEqual(VEvent<?> v1, VEvent<?> v2)
        {
            return (v1.getDateTimeEnd() == null) ? (v2.getDateTimeEnd() == null) : v1.getDateTimeEnd().equals(v2.getDateTimeEnd());
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
        public boolean parseAndSetProperty(VEvent<?> vEvent, String value)
        {
            vEvent.setLocation(value);
            return true;
        }

        @Override
        public Object getPropertyValue(VEvent<?> vEvent)
        {
            return vEvent.getLocation();
        }
        
        @Override
        public String makeContentLine(VEvent<?> vEvent)
        {
            return ((vEvent.getLocation() == null) || (vEvent.getLocation().isEmpty())) ? null : vEvent.locationProperty().getName()
                    + ":" + vEvent.getLocation();
        }

        @Override
        public boolean isPropertyEqual(VEvent<?> v1, VEvent<?> v2)
        {
            return (v1.getLocation() == null) ? (v2.getLocation() == null) : v1.getLocation().equals(v2.getLocation());
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
    
    /** sets VEvent's property for this VEventProperty to parameter value
     * value is a string that is parsed if necessary to the appropriate type
     * returns true, if property was found and set */
    public abstract boolean parseAndSetProperty(VEvent<?> vEvent, String value);

    /** gets VEvent's property value for this VEventProperty */
    public abstract Object getPropertyValue(VEvent<?> vEvent);
    
    /** makes content line (RFC 5545 3.1) from a VEvent property  */
    public abstract String makeContentLine(VEvent<?> vEvent);
    
    /** Checks is corresponding property is equal between v1 and v2 */
    public abstract boolean isPropertyEqual(VEvent<?> v1, VEvent<?> v2);       

}