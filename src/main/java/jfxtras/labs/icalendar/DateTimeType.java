package jfxtras.labs.icalendar;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.Settings;


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
public enum DateTimeType
{
    DATE
    {
        @Override
        public String formatStart(Temporal temporal) { return Settings.DATE_FORMAT.format(temporal); }

        @Override
        public String formatEndSameDay(Temporal temporal) { throw new DateTimeException("Invalid DateTimeType format selection"); }

        @Override
        public String formatEndDifferentDay(Temporal temporal) { return Settings.DATE_FORMAT.format(temporal); }

        @Override
        public Temporal from(Temporal temporal, ZoneId zone)
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
    }
  , DATE_WITH_LOCAL_TIME
    {
        @Override
        public String formatStart(Temporal temporal) { return Settings.DATE_TIME_FORMAT.format(temporal); }

        @Override
        public String formatEndSameDay(Temporal temporal) { return Settings.TIME_FORMAT_END.format(temporal); }

        @Override
        public String formatEndDifferentDay(Temporal temporal) { return Settings.DATE_TIME_FORMAT.format(temporal); }

        @Override
        public Temporal from(Temporal temporal, ZoneId zone)
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
    }
  , DATE_WITH_UTC_TIME
    {
        @Override
        public String formatStart(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatStart(temporal); }
    
        @Override
        public String formatEndSameDay(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatEndSameDay(temporal); }
    
        @Override
        public String formatEndDifferentDay(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatEndDifferentDay(temporal); }

        @Override
        public Temporal from(Temporal temporal, ZoneId zone)
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
    }
  , DATE_WITH_LOCAL_TIME_AND_TIME_ZONE
    {
        @Override
        public String formatStart(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatStart(temporal); }
    
        @Override
        public String formatEndSameDay(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatEndSameDay(temporal); }
    
        @Override
        public String formatEndDifferentDay(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatEndDifferentDay(temporal); }
    
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
    };

    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault(); // default ZoneId to convert to
    /** 
     * Default DateTimeType to use when none is specified.  For example, when a date-only component is converted to
     * a date-time one.
     */
    public static final DateTimeType DEFAULT_DATE_TIME_TYPE = DateTimeType.DATE_WITH_UTC_TIME;
    
    /** Find DateTimeType of Temporal parameter t */
    public static DateTimeType of(Temporal temporal)
    {
        if (temporal instanceof LocalDate)
        {
            return DATE;
        } else if (temporal instanceof LocalDateTime)
        {
            return DATE_WITH_LOCAL_TIME;
        } else if (temporal instanceof ZonedDateTime)
        {
            ZoneId z = ((ZonedDateTime) temporal).getZone();
            if (z == ZoneId.of("Z"))
            {
                return DATE_WITH_UTC_TIME;
            } else
            {
                return DATE_WITH_LOCAL_TIME_AND_TIME_ZONE;                    
            }
        } else
        {
            throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
        }
    }
    
    /*
     * Change a Temporal type to match new DateTimeType outputType
     * 
     * When changing a ZonedDateTime it is first adjusted to the DEFAULT_ZONE to ensure
     * proper local time.
     */
    @Deprecated // use from method instead
    public static Temporal changeTemporal(Temporal temporal, DateTimeType outputType)
    {
        DateTimeType initialType = of(temporal);
        if (initialType == outputType)
        {
            return temporal; // nothing to do;
        } else
        {
            switch (initialType)
            {
            case DATE:
                switch(outputType)
                {
                case DATE:
                    return temporal; // do nothing
                case DATE_WITH_LOCAL_TIME:
                    return LocalDate.from(temporal).atStartOfDay();
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                    return LocalDate.from(temporal).atStartOfDay().atZone(DEFAULT_ZONE);
                case DATE_WITH_UTC_TIME:
                    return LocalDate.from(temporal).atStartOfDay().atZone(DEFAULT_ZONE).withZoneSameInstant(ZoneId.of("Z"));
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            case DATE_WITH_LOCAL_TIME:
                switch(outputType)
                {
                case DATE:
                    return LocalDate.from(temporal);
                case DATE_WITH_LOCAL_TIME:
                    return temporal; // do nothing
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                    return LocalDateTime.from(temporal).atZone(DEFAULT_ZONE);
                case DATE_WITH_UTC_TIME:
                    return LocalDateTime.from(temporal).atZone(DEFAULT_ZONE).withZoneSameInstant(ZoneId.of("Z"));
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
            {
                ZonedDateTime myZonedDateTime;
                switch(outputType)
                {
                case DATE:
                    myZonedDateTime = ZonedDateTime.from(temporal).withZoneSameInstant(DEFAULT_ZONE);
                    return LocalDate.from(myZonedDateTime);
                case DATE_WITH_LOCAL_TIME:
                    myZonedDateTime = ZonedDateTime.from(temporal).withZoneSameInstant(DEFAULT_ZONE);
                    return LocalDateTime.from(myZonedDateTime);
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                    return temporal; // do nothing
                case DATE_WITH_UTC_TIME:
                    return ZonedDateTime.from(temporal).withZoneSameInstant(ZoneId.of("Z"));
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            }
            case DATE_WITH_UTC_TIME:
            {
                ZonedDateTime myZonedDateTime = ZonedDateTime.from(temporal).withZoneSameInstant(DEFAULT_ZONE);
                switch(outputType)
                {
                case DATE:
                    return LocalDate.from(myZonedDateTime);
                case DATE_WITH_LOCAL_TIME:
                    return LocalDateTime.from(myZonedDateTime);
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                    return myZonedDateTime.withZoneSameInstant(DEFAULT_ZONE);
                case DATE_WITH_UTC_TIME:
                    return temporal; // do nothing
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
                }
            }
            default:
                throw new DateTimeException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
            }
        }
    }
        
    /**
     * Returns LocalDateTime from Temporal that is an instance of either LocalDate or LocalDateTime
     * If the parameter is type LocalDate the returned LocalDateTime is atStartofDay.
     * If the parameter is type ZonedDateTime the zoneID is changed to ZoneId.systemDefault() before taking the
     * LocalDateTime.
     * 
     * @param temporal - either LocalDate or LocalDateTime type
     * @return LocalDateTime
     */
    public static LocalDateTime localDateTimeFromTemporal(Temporal temporal)
    {
        if (temporal == null) return null;
        if (temporal.isSupported(ChronoUnit.NANOS))
        {
            if (temporal instanceof ZonedDateTime)
            {
                ZonedDateTime z = ((ZonedDateTime) temporal).withZoneSameInstant(ZoneId.systemDefault());
                return LocalDateTime.from(z);                
            } else if (temporal instanceof LocalDateTime)
            {
                return LocalDateTime.from(temporal);                
            } else throw new DateTimeException("Invalid temporal type:" + temporal.getClass().getSimpleName());
        } else
        {
            return LocalDate.from(temporal).atStartOfDay();
        }
    }

    public static String formatRange(Temporal start, Temporal end)
    {
        Period days = Period.between(LocalDate.from(start), LocalDate.from(end));
        final String startString = DateTimeType.of(start).formatStart(start);
        final String endString;
        if (days.isZero()) // same day
        {
            endString = DateTimeType.of(end).formatEndSameDay(end);
        } else
        {
            endString = DateTimeType.of(end).formatEndDifferentDay(end);            
        }
        return startString + " - " + endString;
    }

    /** Convert temporal to new DateTimeType */
    public abstract Temporal from(Temporal temporal, ZoneId zone);
    /** Formats temporal according to its DateTimeType */
    public abstract String formatStart(Temporal temporal);
    /** Formats temporal according to its DateTimeType */
    public abstract String formatEndSameDay(Temporal temporal);
    /** Formats temporal according to its DateTimeType */
    public abstract String formatEndDifferentDay(Temporal temporal);

}