package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

import jfxtras.labs.icalendar.DateTimeUtilities.DateTimeType;
import jfxtras.labs.icalendar.VComponent.StartEndRange;

public class DateTimeUtilities
{
    public static String formatRange(Temporal start, Temporal end)
    {
        DateTimeFormatter startFormatter = (DateTimeType.of(start) == DateTimeType.DATE) ? Settings.DATE_FORMAT : Settings.DATE_TIME_FORMAT;
        final String startString = startFormatter.format(start);
        final String endString;
        if (end != null)
        {
            Period days = Period.between(LocalDate.from(start), LocalDate.from(end));
            if (start == end)
            {
                endString = "";
            } else if (days.isZero()) // same day
            {
                endString = " - " + Settings.TIME_FORMAT_END.format(end);
            } else
            {
                DateTimeFormatter endFormatterDifferentDay = (DateTimeType.of(start) == DateTimeType.DATE) ? Settings.DATE_FORMAT : Settings.DATE_TIME_FORMAT;
                endString = " - " + endFormatterDifferentDay.format(end);            
            }
        } else
        {
            endString = (Settings.resources == null) ? " - forever" : " - " + Settings.resources.getString("forever");
        }
        return startString + endString;
    }
    public static String formatRange(StartEndRange range) //Temporal start, Temporal end)
    {
        Temporal start = range.getDateTimeStart();
        Temporal end = range.getDateTimeEnd();
        return formatRange(start, end);
    }
    
//    /** formats by either LocalDate or LocalDateTime Temporal to an easy-to-read format
//     * Example: Dec 5, 2015 - Feb 6, 2016
//     *          Nov 12, 2015 - forever
//     */
//   static String temporalToStringPretty(Temporal temporal)
//    {
//        if (DateTimeType.of(temporal) == DateTimeType.DATE)
//        {
//            return Settings.DATE_TIME_FORMAT.format(temporal);
//        } else
//        {
//            return Settings.DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY.format(temporal);
//        }
//    };
//    
//    /**
//     * Makes easy-to-read string of date range for the VComponents
//     * For ALL edit option (one VComponent)
//     * 
//     * @param vComponent
//     * @return - easy-to-read string of date range for the VComponents
//     */
//   public static String vComponentRangeToString(VComponent<?> vComponent)
//    {
//        return vComponentRangeToString(Arrays.asList(vComponent));
//    }
//    /**
//     * Makes easy-to-read string of date range for the VComponents
//     * Beginning of range is parameter start
//     * For ALL edit option (one VComponent)
//     * 
//     * @param vComponent
//     * @param start - Temporal start date or date/time
//     * @return - easy-to-read string of date range for the VComponents
//     */
//    static String vComponentRangeToString(VComponent<?> vComponent, Temporal start)
//    {
//        return vComponentRangeToString(Arrays.asList(vComponent), start);
//    }
//    /**
//     * For ALL edit option (list of VComponents)
//     * 
//     * @param relatives - list of all related VComponents
//     * @return - easy-to-read string of date range for the VComponents
//     */
//    static String vComponentRangeToString(Collection<VComponent<?>> relatives)
//    {
//        return vComponentRangeToString(relatives, null);
//    }
//    /**
//     * For THIS_AND_FUTURE_ALL edit option
//     * 
//     * @param relatives - list of all related VComponents
//     * @param initial - Temporal start date or date/time
//     * @return - easy-to-read string of date range for the VComponents
//     */
//    static String vComponentRangeToString(Collection<VComponent<?>> relatives, Temporal initial)
//    {
//        if (relatives.size() == 0) return null;
//        Iterator<VComponent<?>> i = relatives.iterator();
//        VComponent<?> v1 = i.next();
//        Temporal start = (initial == null) ? v1.getDateTimeStart() : initial; // set initial start
//        Temporal end = v1.lastStartTemporal();
//        if (i.hasNext())
//        {
//            VComponent<?> v = i.next();
//            if (initial != null) start = (VComponent.isBefore(v.getDateTimeStart(), start)) ? v.getDateTimeStart() : start;
//            if (end != null) // null means infinite
//            {
//                Temporal myEnd = v.lastStartTemporal();
//                if (myEnd == null) end = null;
//                else end = (VComponent.isAfter(myEnd, end)) ? v.lastStartTemporal() : end;
//            }
//        }
//        if (start.equals(end)) return temporalToStringPretty(start); // individual            
//        else if (end == null) return temporalToStringPretty(start) + " - forever"; // infinite
//        else return temporalToStringPretty(start) + " - " + Settings.DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY.format(end); // has finite range (only returns date for end of range)
//    }  
}
