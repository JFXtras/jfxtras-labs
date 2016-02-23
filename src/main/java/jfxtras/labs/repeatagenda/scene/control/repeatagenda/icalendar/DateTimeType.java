package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;


/**
 * Temporal date and date-time types supported by iCalendar.
 *  DATE
 *  DATE_WITH_LOCAL_TIME
 *  DATE_WITH_LOCAL_TIME_AND_TIME_ZONE
 *  DATE_WITH_UTC_TIME:
 * see iCalendar RFC 5545, page 32-33
 * 
 * @author David Bal
 *
 */
public enum DateTimeType
{
    DATE (LocalDate.class, null)
  , DATE_WITH_LOCAL_TIME (LocalDate.class, null)
  , DATE_WITH_UTC_TIME (ZonedDateTime.class, ZoneId.of("Z"))
  , DATE_WITH_LOCAL_TIME_AND_TIME_ZONE (ZonedDateTime.class, ZoneId.systemDefault());

    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault(); // default ZoneId to convert to
    private Class<? extends Temporal> clazz;
    private ZoneId zoneId;
    
    DateTimeType(Class<? extends Temporal> clazz, ZoneId zoneId)
    {
        this.clazz = clazz;
        this.zoneId = zoneId;
    }
    
    public Class<? extends Temporal> getTemporalClass() { return clazz; }
    public ZoneId getZoneId() { return zoneId; }
    
    
    public static DateTimeType dateTimeTypeFromTemporal(Temporal t)
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
        DateTimeType initialType = dateTimeTypeFromTemporal(t);
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
}