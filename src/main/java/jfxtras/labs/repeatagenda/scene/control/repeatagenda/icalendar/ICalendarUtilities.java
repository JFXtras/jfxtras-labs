package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.Pair;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.Settings;

public final class ICalendarUtilities
{
    private ICalendarUtilities() { };
    
    final private static Comparator<? super Pair<String, String>> DTSTART_COMPARATOR = (p1, p2) ->
    {
        String propertyName1 = p1.getKey();
        return (propertyName1.equals("DTSTART")) ? -1 : 1;
    };
    
    /*
     * Returns list of property name and property value list wrapped in a Pair.  DTSTART
     * property is put on top if present.
     */
    public static List<Pair<String,String>> ComponentStringToPropertyNameAndValueList(String string)
    {        
        return Arrays
            .stream(string.split(System.lineSeparator()))
            .map(line -> {
                int propertyValueSeparatorIndex = 0;
                for (int i=0; i<line.length(); i++)
                {
                    if ((line.charAt(i) == ';') || (line.charAt(i) == ':'))
                    {
                        propertyValueSeparatorIndex = i;
                        break;
                    }
                }
                if (propertyValueSeparatorIndex == 0)
                {
                    return null; // line doesn't contain a property, skip
                }
                String propertyName = line.substring(0, propertyValueSeparatorIndex);
                String value = line.substring(propertyValueSeparatorIndex + 1).trim();
                if (value.isEmpty())
                { // skip empty properties
                    return null;
                }
                return new Pair<String,String>(propertyName, value);
            })
            .filter(p -> p != null)
            .sorted(DTSTART_COMPARATOR)
            .collect(Collectors.toList());
    }
    
    /** formats by either LocalDate or LocalDateTime Temporal to an easy-to-read format
     * Example: Dec 5, 2015 - Feb 6, 2016
     *          Nov 12, 2015 - forever
     */
    static String temporalToStringPretty(Temporal temporal)
    {
        if (temporal.isSupported(ChronoUnit.NANOS))
        {
            return Settings.DATE_TIME_FORMAT.format(temporal);
        } else
        {
            return Settings.DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY.format(temporal);
        }
    };
    
    // TODO - PROBABLY THESE METHODS SHOULD GO TO ICALENDARUTILITIES
    /**
     * Makes easy-to-read string of date range for the VComponents
     * For ALL edit option (one VComponent)
     * 
     * @param vComponent
     * @return - easy-to-read string of date range for the VComponents
     */
    public static <U> String rangeToString(VComponent<U> vComponent)
    {
        return rangeToString(Arrays.asList(vComponent));
    }
    /**
     * Makes easy-to-read string of date range for the VComponents
     * Beginning of range is parameter start
     * For ALL edit option (one VComponent)
     * 
     * @param vComponent
     * @param start - Temporal start date or date/time
     * @return - easy-to-read string of date range for the VComponents
     */
    static <U> String rangeToString(VComponent<U> vComponent, Temporal start)
    {
        return rangeToString(Arrays.asList(vComponent), start);
    }
    /**
     * For ALL edit option (list of VComponents)
     * 
     * @param relatives - list of all related VComponents
     * @return - easy-to-read string of date range for the VComponents
     */
    static <U> String rangeToString(Collection<VComponent<U>> relatives)
    {
        return rangeToString(relatives, null);
    }
    /**
     * For THIS_AND_FUTURE_ALL edit option
     * 
     * @param relatives - list of all related VComponents
     * @param start - Temporal start date or date/time
     * @return - easy-to-read string of date range for the VComponents
     */
    static <U> String rangeToString(Collection<VComponent<U>> relatives, Temporal start)
    {
        if (relatives.size() == 0) return null;
        Iterator<VComponent<U>> i = relatives.iterator();
        VComponent<U> v1 = i.next();
        Temporal start2 = (start == null) ? v1.getDateTimeStart() : start; // set initial start
        Temporal end = v1.lastStartTemporal();
        if (i.hasNext())
        {
            VComponent<U> v = i.next();
            if (start != null) start2 = (VComponent.isBefore(v.getDateTimeStart(), start2)) ? v.getDateTimeStart() : start2;
            if (end != null) // null means infinite
            {
                Temporal myEnd = v.lastStartTemporal();
                if (myEnd == null) end = null;
                else end = (VComponent.isAfter(myEnd, end)) ? v.lastStartTemporal() : end;
            }
        }
        if (start2.equals(end)) return temporalToStringPretty(start2); // individual            
        else if (end == null) return temporalToStringPretty(start2) + " - forever"; // infinite
        else return temporalToStringPretty(start2) + " - " + Settings.DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY.format(end); // has finite range (only returns date for end of range)
    }   
    
    /**
     * Options available when editing or deleting a repeatable appointment.
     * Sometimes all options are not available.  For example, a one-part repeating
     * event doesn't have the SEGMENT option.
     */
    public enum ChangeDialogOption
    {
        ONE                  // individual instance
      , ALL                  // entire series
      , THIS_AND_FUTURE      // same as THIS_AND_FUTURE_ALL, but has a shorter text.  It is used when THIS_AND_FUTURE_SEGMENT does not appear
      , CANCEL;              // do nothing
                
        @Override
        public String toString() {
            return Settings.REPEAT_CHANGE_CHOICES.get(this);
        }
    }
}
