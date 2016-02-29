package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

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
    }
  , DATE_WITH_LOCAL_TIME
    {
      @Override
      public String formatStart(Temporal temporal) { return Settings.DATE_TIME_FORMAT.format(temporal); }

      @Override
      public String formatEndSameDay(Temporal temporal) { return Settings.TIME_FORMAT_END.format(temporal); }

      @Override
      public String formatEndDifferentDay(Temporal temporal) { return Settings.DATE_TIME_FORMAT.format(temporal); }
    }
  , DATE_WITH_UTC_TIME
    {
    @Override
    public String formatStart(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatStart(temporal); }

    @Override
    public String formatEndSameDay(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatEndSameDay(temporal); }

    @Override
    public String formatEndDifferentDay(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatEndDifferentDay(temporal); }
    }
  , DATE_WITH_LOCAL_TIME_AND_TIME_ZONE
    {
    @Override
    public String formatStart(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatStart(temporal); }

    @Override
    public String formatEndSameDay(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatEndSameDay(temporal); }

    @Override
    public String formatEndDifferentDay(Temporal temporal) { return DATE_WITH_LOCAL_TIME.formatEndDifferentDay(temporal); }
    };

    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault(); // default ZoneId to convert to
    
    /** Find DateTimeType of Temporal parameter t */
    public static DateTimeType from(Temporal t)
    {
        if (t instanceof LocalDate)
        {
            return DATE;
        } else if (t instanceof LocalDateTime)
        {
            return DATE_WITH_LOCAL_TIME;
        } else if (t instanceof ZonedDateTime)
        {
            ZoneId z = ((ZonedDateTime) t).getZone();
            if (z == ZoneId.of("Z"))
            {
                return DATE_WITH_UTC_TIME;
            } else
            {
                return DATE_WITH_LOCAL_TIME_AND_TIME_ZONE;                    
            }
        } else
        {
            throw new DateTimeException("Unsupported Temporal class:" + t.getClass().getSimpleName());
        }
    }
    
    /*
     * Change a Temporal type to match new DateTimeType outputType
     * 
     * When changing a ZonedDateTime it is first adjusted to the DEFAULT_ZONE to ensure
     * proper local time.
     */
    public static Temporal changeTemporal(Temporal t, DateTimeType outputType)
    {
        DateTimeType initialType = from(t);
        if (initialType == outputType)
        {
            return t; // nothing to do;
        } else
        {
            switch (initialType)
            {
            case DATE:
                switch(outputType)
                {
                case DATE:
                    return t; // do nothing
                case DATE_WITH_LOCAL_TIME:
                    return LocalDate.from(t).atStartOfDay();
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                    return LocalDate.from(t).atStartOfDay().atZone(DEFAULT_ZONE);
                case DATE_WITH_UTC_TIME:
                    return LocalDate.from(t).atStartOfDay().atZone(DEFAULT_ZONE).withZoneSameInstant(ZoneId.of("Z"));
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + t.getClass().getSimpleName());
                }
            case DATE_WITH_LOCAL_TIME:
                switch(outputType)
                {
                case DATE:
                    return LocalDate.from(t);
                case DATE_WITH_LOCAL_TIME:
                    return t; // do nothing
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                    return LocalDateTime.from(t).atZone(DEFAULT_ZONE);
                case DATE_WITH_UTC_TIME:
                    return LocalDateTime.from(t).atZone(DEFAULT_ZONE).withZoneSameInstant(ZoneId.of("Z"));
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + t.getClass().getSimpleName());
                }
            case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
            {
                ZonedDateTime myZonedDateTime;
                switch(outputType)
                {
                case DATE:
                    myZonedDateTime = ZonedDateTime.from(t).withZoneSameInstant(DEFAULT_ZONE);
                    return LocalDate.from(myZonedDateTime);
                case DATE_WITH_LOCAL_TIME:
                    myZonedDateTime = ZonedDateTime.from(t).withZoneSameInstant(DEFAULT_ZONE);
                    return LocalDateTime.from(myZonedDateTime);
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                    return t; // do nothing
                case DATE_WITH_UTC_TIME:
                    return ZonedDateTime.from(t).withZoneSameInstant(ZoneId.of("Z"));
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + t.getClass().getSimpleName());
                }
            }
            case DATE_WITH_UTC_TIME:
            {
                ZonedDateTime myZonedDateTime = ZonedDateTime.from(t).withZoneSameInstant(DEFAULT_ZONE);
                switch(outputType)
                {
                case DATE:
                    return LocalDate.from(myZonedDateTime);
                case DATE_WITH_LOCAL_TIME:
                    return LocalDateTime.from(myZonedDateTime);
                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
                    return myZonedDateTime.withZoneSameInstant(DEFAULT_ZONE);
                case DATE_WITH_UTC_TIME:
                    return t; // do nothing
                default:
                    throw new DateTimeException("Unsupported Temporal class:" + t.getClass().getSimpleName());
                }
            }
            default:
                throw new DateTimeException("Unsupported Temporal class:" + t.getClass().getSimpleName());
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
        final String startString = DateTimeType.from(start).formatStart(start);
        final String endString;
        if (days.isZero()) // same day
        {
            endString = DateTimeType.from(end).formatEndSameDay(end);
        } else
        {
            endString = DateTimeType.from(end).formatEndDifferentDay(end);            
        }
        return startString + " - " + endString;
    }
    
    /** Formats temporal according to its DateTimeType */
    public abstract String formatStart(Temporal temporal);
    /** Formats temporal according to its DateTimeType */
    public abstract String formatEndSameDay(Temporal temporal);
    /** Formats temporal according to its DateTimeType */
    public abstract String formatEndDifferentDay(Temporal temporal);

}