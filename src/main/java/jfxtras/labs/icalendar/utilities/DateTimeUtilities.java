package jfxtras.labs.icalendar.utilities;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;


/**
 * Temporal date and date-time types supported by iCalendar.
 *  DATE
 *  DATE_WITH_LOCAL_TIME
 *  DATE_WITH_LOCAL_TIME_AND_TIME_ZONE
 *  DATE_WITH_UTC_TIME:
 * see iCalendar RFC 5545, page 32-33
 * 
 * includes methods to format a Temporal representing a DateTimeType as a String
 * 
 * @author David Bal
 *
 */
public final class DateTimeUtilities
{
    private DateTimeUtilities() { }
    
    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault(); // default ZoneId to convert to
    /** 
     * Default DateTimeType to use when none is specified.  For example, when a date-only component is converted to
     * a date-time one.
     */
    public static final DateTimeType DEFAULT_DATE_TIME_TYPE = DateTimeType.DATE_WITH_UTC_TIME;

    final static DateTimeFormatter LOCAL_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)
            .toFormatter();
    final static DateTimeFormatter LOCAL_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendValue(SECOND_OF_MINUTE, 2)
            .toFormatter();
    public final static DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(LOCAL_DATE_FORMATTER)
            .appendLiteral('T')
            .append(LOCAL_TIME_FORMATTER)
            .toFormatter();
    public final static DateTimeFormatter ZONED_DATE_TIME_UTC_FORMATTER = new DateTimeFormatterBuilder()
            .append(LOCAL_DATE_TIME_FORMATTER)
            .appendOffsetId()
            .toFormatter();
    @Deprecated
//    public final static DateTimeFormatter ZONED_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
//            .optionalStart()
//            .parseCaseInsensitive()
//            .appendLiteral("TZID=")
//            .appendZoneRegionId()
//            .appendLiteral(':')
//            .optionalEnd()
//            .append(LOCAL_DATE_TIME_FORMATTER)
//            .optionalStart()
//            .appendOffsetId()            
//            .optionalEnd()
//            .toFormatter();
    final static DateTimeFormatter ZONE_FORMATTER = new DateTimeFormatterBuilder()
            .optionalStart()
            .parseCaseInsensitive()
            .appendLiteral("TZID=")
            .appendZoneRegionId()
            .optionalEnd()
            .toFormatter();
    
    /** Compares two temporals of the same type */
    public final static Comparator<Temporal> TEMPORAL_COMPARATOR = (t1, t2) -> 
    {
        LocalDateTime ld1 = (t1.isSupported(ChronoUnit.NANOS)) ? LocalDateTime.from(t1) : LocalDate.from(t1).atStartOfDay();
        LocalDateTime ld2 = (t2.isSupported(ChronoUnit.NANOS)) ? LocalDateTime.from(t2) : LocalDate.from(t2).atStartOfDay();
        return ld1.compareTo(ld2);
    };
    
    /** Determines if Temporal is before t2
     * Works for LocalDate or LocalDateTime
     * 
     * @param t1 first Temporal
     * @param t2 second Temporal (to compare with t1)
     * @return true if t1 is before t2, false otherwise
     */
    public static boolean isBefore(Temporal t1, Temporal t2)
    {
        if (t1.getClass().equals(t2.getClass()))
        {
            LocalDateTime d1 = DateTimeUtilities.toLocalDateTime(t1);
            LocalDateTime d2 = DateTimeUtilities.toLocalDateTime(t2);
            return d1.isBefore(d2);
        } throw new DateTimeException("For comparision, Temporal classes must be equal (" + t1.getClass().getSimpleName() + ", " + t2.getClass().getSimpleName() + ")");
    }

    /** Determines if Temporal is after t2
     * Works for LocalDate or LocalDateTime
     * 
     * @param t1 first Temporal
     * @param t2 second Temporal (to compare with t1)
     * @return true if t1 is after t2, false otherwise
     */
    public static boolean isAfter(Temporal t1, Temporal t2)
    {
        if (t1.getClass().equals(t2.getClass()))
        {
            LocalDateTime d1 = DateTimeUtilities.toLocalDateTime(t1);
            LocalDateTime d2 = DateTimeUtilities.toLocalDateTime(t2);
            return d1.isAfter(d2);
        } throw new DateTimeException("For comparision, Temporal classes must be equal (" + t1.getClass().getSimpleName() + ", " + t2.getClass().getSimpleName() + ")");
    }
    
    /**
     * returns week of month.
     * For example, a LocalDate representing March 10, 2016 returns 2, for the 2nd Thursday.
     * 
     * @param dateBasedTemporal - date based Temporal, such as LocalDate
     * @return - ordinal week in month, such as 2nd (as in 2nd Wednesday in month)
     */
    public static int weekOrdinalInMonth(Temporal dateBasedTemporal)
    {
        Temporal start = dateBasedTemporal.with(TemporalAdjusters.firstDayOfMonth());
        int ordinalWeekNumber = 0;
        while (! DateTimeUtilities.isBefore(dateBasedTemporal, start))
        {
            ordinalWeekNumber++;
            start = start.plus(1, ChronoUnit.WEEKS);
        }
        return ordinalWeekNumber;
    }
    
    /**
     * Calculate TemporalAmount between two Temporals.
     * Both temporals must be the same type and representations of a DateTimeType.
     * 
     * @param startInclusive - the start temporal, not null
     * @param endExclusive - the end temporal, not null
     * @return - Period for DATE, Duration for all other DateTimeTypes
     */
    public static TemporalAmount durationBetween(Temporal startInclusive, Temporal endExclusive)
    {
        if (! startInclusive.getClass().equals(endExclusive.getClass()))
        {
            throw new DateTimeException("Temporal class of parameters startInclusive and endExclusive must be the same.");
        }
        final TemporalAmount duration;
        if (DateTimeType.of(startInclusive) == DateTimeType.DATE)
        {
            duration = Period.between(LocalDate.from(startInclusive), LocalDate.from(endExclusive));
        } else
        {
            duration = Duration.between(startInclusive, endExclusive);
        }
        return duration;
    }
    
    /**
     * Parse iCalendar date or date/time string into LocalDate, LocalDateTime or ZonedDateTime for following formats:
     * FORM #1: DATE WITH LOCAL TIME e.g. 19980118T230000 (LocalDateTime)
     * FORM #2: DATE WITH UTC TIME e.g. 19980119T070000Z (ZonedDateTime)
     * FORM #3: DATE WITH LOCAL TIME AND TIME ZONE REFERENCE e.g. TZID=America/New_York:19980119T020000 (ZonedDateTime)
     * FORM #4: DATE ONLY e.g. VALUE=DATE:19970304 (LocalDate)
     * 
     * 
     * @param temporalPropertyLine
     * @return
     */
    @Deprecated // use new 
    public static Temporal parse(String temporalPropertyLine)
    {
        Map<String, String> parameterMap = ICalendarUtilities.propertyLineToParameterMap(temporalPropertyLine);
        System.out.println("parameterMap:" + parameterMap);
        return Arrays.stream(DateTimeType.values())
                .map(d -> d.parse(parameterMap))
                .filter(d -> d != null)
                .findAny()
                .get();
    }
    
    public static Temporal parse(String temporalString, ZoneId zone)
    {
        return Arrays.stream(DateTimeType.values())
                .map(d -> d.parse(temporalString, zone))
                .filter(d -> d != null)
                .findAny()
                .get();        
    }
//    
//    /**
//     * Matches 
//     * 
//     * @param temporalString
//     * @return
//     */
//    private static Temporal parseTemporalString(String temporalString)
//    {
//        return Arrays.stream(DateTimeType.values())
//            .map(d -> d.parse(temporalString))
//            .filter(d -> d != null)
//            .findFirst()
//            .get();
//    }
    
//    /** Parse iCalendar date or date/time string into LocalDate, LocalDateTime or ZonedDateTime for following formats:
//     * FORM #1: DATE WITH LOCAL TIME e.g. 19980118T230000 (LocalDateTime)
//     * FORM #2: DATE WITH UTC TIME e.g. 19980119T070000Z (ZonedDateTime)
//     * FORM #3: DATE WITH LOCAL TIME AND TIME ZONE REFERENCE e.g. TZID=America/New_York:19980119T020000 (ZonedDateTime)
//     * FORM #4: DATE ONLY e.g. VALUE=DATE:19970304 (LocalDate)
//     * 
//     * Note: strings can contain optionally contain "VALUE" "=" ("DATE-TIME" / "DATE")) before the date-time portion of the string.
//     * e.g. VALUE=DATE:19960401         VALUE=DATE-TIME:19980101T050000Z
//     * 
//     * Based on ISO.8601.2004
//     */
//    @Deprecated // obsolete - use parameter map instead
    public static Temporal parseOld(String temporalString)
    {
        final String temporalStringAdjusted;
        if (temporalString.matches("^VALUE=DATE-TIME:.*"))
        {
            temporalStringAdjusted = temporalString.substring(temporalString.indexOf("VALUE=DATE-TIME:")+"VALUE=DATE-TIME:".length()).trim();
        } else
        {
            temporalStringAdjusted = temporalString;
        }
        System.out.println("temporalStringAdjusted:"  + temporalStringAdjusted + " " + Arrays.stream(DateTimeType.values())
                .filter(d -> temporalStringAdjusted.matches(d.getPattern()))
                .findFirst()
                .get());
//                .parse(temporalString);
        return null;
    }
    
    /**
     * produced ISO.8601 date and date-time string for given Temporal of type
     * LocalDate, LocalDateTime or ZonedDateTime
     * 
     * @param temporal
     * @return
     */
    public static String format(Temporal temporal)
    {
        return DateTimeType.of(temporal).formatDateTimeType(temporal);
    }
    
    /**
     * Produces property name and attribute, if necessary.
     * For example:
     * LocalDate : DTSTART;VALUE=DATE:
     * LocalDateTime : DTSTART:
     * ZonedDateTime (UTC) : DTSTART:
     * ZonedDateTime : DTEND;TZID=America/New_York:
     * 
     * @param propertyName
     * @param temporal - temporal of LocalDate, LocalDateTime or ZonedDateTime
     * @return
     */
//    @Deprecated
//    public static String dateTimePropertyTag(String propertyName, Temporal temporal)
//    {
//        return DateTimeType.of(temporal).propertyTag(propertyName, temporal);
//    }
    
    
    /**
     * Returns LocalDateTime from Temporal that is an instance of either LocalDate, LocalDateTime or ZonedDateTime
     * If the parameter is type LocalDate the returned LocalDateTime is atStartofDay.
     * If the parameter is type ZonedDateTime the zoneID is changed to ZoneId.systemDefault() before taking the
     * LocalDateTime.
     */
    public static LocalDateTime toLocalDateTime(Temporal temporal)
    {
        return (LocalDateTime) DateTimeType.DATE_WITH_LOCAL_TIME.from(temporal);
    }
    
    public enum DateTimeType
    {
//        DATE ("^(VALUE=DATE:)?[0-9]{8}")
        DATE ("^[0-9]{8}")
        {
            @Override
            public Temporal from(Temporal temporal, ZoneId zone)
            {
                return this.from(temporal);
            }
    
            @Override
            public Temporal from(Temporal temporal)
            {
                switch(DateTimeType.of(temporal))
                {
                case DATE:
                    return temporal; // do nothing
                case DATE_WITH_LOCAL_TIME:
                    return LocalDate.from(temporal);
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                case DATE_WITH_UTC_TIME:
                    return ZonedDateTime.from(temporal).withZoneSameInstant(DEFAULT_ZONE).toLocalDate();
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            }

            @Override
            String formatDateTimeType(Temporal temporal)
            {
                return LOCAL_DATE_FORMATTER.format(temporal);
            }

//            @Override
//            String propertyTag(String propertyName, Temporal temporal)
//            {
//                return propertyName + ";VALUE=DATE:";
//            }

//            @Override
//            Temporal parse(String temporalString)
//            {
//                if (temporalString.matches("^VALUE=DATE:.*"))
//                {
//                    temporalString = temporalString.substring(temporalString.indexOf("VALUE=DATE:")+"VALUE=DATE:".length()).trim();
//                }
//                return LocalDate.parse(temporalString, LOCAL_DATE_FORMATTER);
//            }

            @Override
            boolean is(Temporal temporal)
            {
                return temporal instanceof LocalDate;
            }

            @Deprecated
            @Override
            Temporal parse(Map<String, String> parameterMap)
            {
                String temporalString = parameterMap.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
                if (temporalString.matches(getPattern()))
                {
                    return LocalDate.parse(temporalString, LOCAL_DATE_FORMATTER);                    
                }
                return null;
            }

            @Override
            public Temporal parse(String temporalString, ZoneId zone)
            {
                if (temporalString.matches(getPattern()))
                {
                    return LocalDate.parse(temporalString, LOCAL_DATE_FORMATTER);                    
                }
                return null;
            }
        }
      , DATE_WITH_LOCAL_TIME ("^[0-9]{8}T([0-9]{6})")
        {
            @Override
            public Temporal from(Temporal temporal, ZoneId zone)
            {
                return this.from(temporal);
            }
    
            @Override
            public Temporal from(Temporal temporal)
            {
                switch(DateTimeType.of(temporal))
                {
                case DATE:
                    return LocalDate.from(temporal).atStartOfDay();
                case DATE_WITH_LOCAL_TIME:
                    return temporal;  // do nothing
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                case DATE_WITH_UTC_TIME:
                    return ZonedDateTime.from(temporal).withZoneSameInstant(DEFAULT_ZONE).toLocalDateTime();
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            }

            @Override
            String formatDateTimeType(Temporal temporal)
            {
                return LOCAL_DATE_TIME_FORMATTER.format(temporal);
            }

//            @Override
//            String propertyTag(String propertyName, Temporal temporal)
//            {
//                return propertyName + ":";
//            }

//            @Override
//            Temporal parse(String temporalString)
//            {
//                return LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
//            }

            @Override
            boolean is(Temporal temporal)
            {
                return temporal instanceof LocalDateTime;
            }

            @Deprecated
            @Override
            Temporal parse(Map<String, String> parameterMap)
            {
                String tzidParameter = parameterMap.get("TZID"); // time zone parameter
                String temporalString = parameterMap.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
                boolean isTzidEmpty = tzidParameter == null;
                boolean isPatternMatch = temporalString.matches(getPattern());
                if (isTzidEmpty && isPatternMatch)
                {
                    return LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
                }
                return null;
            }

            @Override
            public Temporal parse(String temporalString, ZoneId zone)
            {
                boolean isTzidEmpty = zone == null;
                boolean isPatternMatch = temporalString.matches(getPattern());
                if (isTzidEmpty && isPatternMatch)
                {
                    return LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
                }
                return null;
            }
        }
      , DATE_WITH_UTC_TIME ("^[0-9]{8}T([0-9]{6})Z")
        {
            @Override
            public Temporal from(Temporal temporal, ZoneId zone)
            {
                return this.from(temporal);
            }
    
            @Override
            public Temporal from(Temporal temporal)
            {
                switch(DateTimeType.of(temporal))
                {
                case DATE:
                    return LocalDate.from(temporal).atStartOfDay().atZone(ZoneId.of("Z"));
                case DATE_WITH_LOCAL_TIME:
                    return LocalDateTime.from(temporal).atZone(DEFAULT_ZONE).withZoneSameInstant(ZoneId.of("Z"));
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                    return ZonedDateTime.from(temporal).withZoneSameInstant(ZoneId.of("Z"));
                case DATE_WITH_UTC_TIME:
                    return temporal;  // do nothing
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            }

            @Override
            String formatDateTimeType(Temporal temporal)
            {
                return ZONED_DATE_TIME_UTC_FORMATTER.format(temporal);
            }

//            @Override
//            String propertyTag(String propertyName, Temporal temporal)
//            {
//                return propertyName + ":";
//            }

//            @Override
//            Temporal parse(String temporalString)
//            {
//                return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_UTC_FORMATTER);
//            }

            @Override
            boolean is(Temporal temporal)
            {
                if (temporal instanceof ZonedDateTime)
                {
                    ZoneId z = ((ZonedDateTime) temporal).getZone();
                    return z == ZoneId.of("Z");
                }
                return false;
            }

            @Deprecated
            @Override
            Temporal parse(Map<String, String> parameterMap)
            {
                String temporalString = parameterMap.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
                boolean isPatternMatch = temporalString.matches(getPattern());
                if (isPatternMatch)
                {
                    return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_UTC_FORMATTER);
                }
                return null;
            }

            @Override
            public Temporal parse(String temporalString, ZoneId zone)
            {
                boolean isPatternMatch = temporalString.matches(getPattern());
                if (isPatternMatch)
                {
                    return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_UTC_FORMATTER);
                }
                return null;
            }
        }
      , DATE_WITH_LOCAL_TIME_AND_TIME_ZONE ("^[0-9]{8}T([0-9]{6})Z?")
        {
            @Override
            public Temporal from(Temporal temporal, ZoneId zone)
            {
                switch(DateTimeType.of(temporal))
                {
                case DATE:
                    return LocalDate.from(temporal).atStartOfDay().atZone(zone);
                case DATE_WITH_LOCAL_TIME:
                    return LocalDateTime.from(temporal).atZone(zone);
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                case DATE_WITH_UTC_TIME:
                    return ZonedDateTime.from(temporal).withZoneSameInstant(zone);
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            }
    
            @Override
            public Temporal from(Temporal temporal)
            {
                throw new DateTimeException("Can't make DATE_WITH_LOCAL_TIME_AND_TIME_ZONE without time zone.  Use from(Temporal temporal, ZoneId zone) instead");
            }

            @Override
            String formatDateTimeType(Temporal temporal)
            {
                return LOCAL_DATE_TIME_FORMATTER.format(temporal); // don't use ZONED_DATE_TIME_FORMATTER because time zone is added to property tag
            }

//            @Override
//            String propertyTag(String propertyName, Temporal temporal)
//            {
//                String zone = ZONE_FORMATTER.format(temporal);
//                return propertyName + ";" + zone + ":";
//            }
//
//            @Override
//            Temporal parse(String temporalString)
//            {
//                return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_FORMATTER);
//            }

            @Override
            boolean is(Temporal temporal)
            {
                return temporal instanceof ZonedDateTime;
            }

            @Deprecated
            @Override
            Temporal parse(Map<String, String> parameterMap)
            {
              String tzidParameter = parameterMap.get("TZID"); // time zone parameter
              String temporalString = parameterMap.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
              boolean isTzidEmpty = tzidParameter == null;
              boolean isPatternMatch = temporalString.matches(getPattern());
              if (! isTzidEmpty && isPatternMatch)
              {
                  ZoneId zone = ZoneId.of(tzidParameter);
                  LocalDateTime localDateTime = LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
                  return localDateTime.atZone(zone);
              }
              return null;
            }

            @Override
            public Temporal parse(String temporalString, ZoneId zone)
            {
                boolean isTzidEmpty = zone == null;
                boolean isPatternMatch = temporalString.matches(getPattern());
                if (! isTzidEmpty && isPatternMatch)
                {
                    LocalDateTime localDateTime = LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
                    return localDateTime.atZone(zone);
                }
                return null;
            }
        };
        
        private String pattern;
        public String getPattern() { return pattern; }

        DateTimeType(String pattern)
        {
            this.pattern = pattern;
        }

        /** Find DateTimeType of Temporal parameter temporal */
        public static DateTimeType of(Temporal temporal)
        {
            return Arrays.stream(DateTimeType.values())
                    .filter(d -> d.is(temporal))
                    .findFirst()
                    .get();
        }
        
        /**
         * returns true if temporal is of the DateTimeType, false otherwise
         */
        abstract boolean is(Temporal temporal);
        
//        /** Parse iCalendar date or date/time string into LocalDate, LocalDateTime or ZonedDateTime for following formats:
//         * FORM #1: DATE WITH LOCAL TIME e.g. 19980118T230000 (LocalDateTime)
//         * FORM #2: DATE WITH UTC TIME e.g. 19980119T070000Z (ZonedDateTime)
//         * FORM #3: DATE WITH LOCAL TIME AND TIME ZONE REFERENCE e.g. TZID=America/New_York:19980119T020000 (ZonedDateTime)
//         * FORM #4: DATE ONLY e.g. VALUE=DATE:19970304 (LocalDate)
//         * 
//         * Note: strings can contain optionally contain "VALUE" "=" ("DATE-TIME" / "DATE")) before the date-time portion of the string.
//         * e.g. VALUE=DATE:19960401         VALUE=DATE-TIME:19980101T050000Z
//         * 
//         * Based on ISO.8601.2004
//         */
//        @Deprecated // use map version below
//        abstract Temporal parse(String temporalString);

        /**
         * Parses parameter map to Temporal.  Returns if no match.
         * 
         * @param parameterMap - map of parameters from propertyLineToParameterMap
         * @return - parsed Temporal, if matches, null otherwise
         */
        @Deprecated
        abstract Temporal parse(Map<String,String> parameterMap);
        
        public abstract Temporal parse(String value, ZoneId zone);

//        /**
//         * Produces property name and attribute, if necessary.
//         * For example:
//         * LocalDate : DTSTART;VALUE=DATE:
//         * LocalDateTime : DTSTART:
//         * ZonedDateTime (UTC) : DTSTART:
//         * ZonedDateTime : DTEND;TZID=America/New_York:
//         * 
//         * @param propertyName - e.g. DTSTART
//         * @param temporal - temporal of LocalDate, LocalDateTime or ZonedDateTime
//         * @return
//         */
//        @Deprecated
//        abstract String propertyTag(String propertyName, Temporal temporal);
    
        /** Format temporal to embedded DateTimeFormatter */
        abstract String formatDateTimeType(Temporal temporal);
        
        /** Convert temporal to new DateTimeType - for DATE_WITH_LOCAL_TIME_AND_TIME_ZONE */
        public abstract Temporal from(Temporal temporal, ZoneId zone);
        
        /** Convert temporal to new DateTimeType  - for all types, but DATE_WITH_LOCAL_TIME_AND_TIME_ZONE */
        public abstract Temporal from(Temporal temporal);
    }
}