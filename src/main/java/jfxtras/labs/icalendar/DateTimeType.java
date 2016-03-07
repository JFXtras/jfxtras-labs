package jfxtras.labs.icalendar;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;


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
    }
  , DATE_WITH_LOCAL_TIME
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
    }
  , DATE_WITH_UTC_TIME
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
    }
  , DATE_WITH_LOCAL_TIME_AND_TIME_ZONE
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
    };

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
    final static DateTimeFormatter ZONED_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .optionalStart()
            .parseCaseInsensitive()
            .appendLiteral("TZID=")
            .appendZoneRegionId()
            .appendLiteral(':')
            .optionalEnd()
            .append(LOCAL_DATE_TIME_FORMATTER)
            .toFormatter();
    final static DateTimeFormatter ZONE_FORMATTER = new DateTimeFormatterBuilder()
            .optionalStart()
            .parseCaseInsensitive()
            .appendLiteral("TZID=")
            .appendZoneRegionId()
            .optionalEnd()
            .toFormatter();
    
    /** Parse iCalendar date or date/time string into LocalDate, LocalDateTime or ZonedDateTime for following formats:
     * FORM #1: DATE WITH LOCAL TIME e.g. 19980118T230000 (LocalDateTime)
     * FORM #2: DATE WITH UTC TIME e.g. 19980119T070000Z (ZonedDateTime)
     * FORM #3: DATE WITH LOCAL TIME AND TIME ZONE REFERENCE e.g. TZID=America/New_York:19980119T020000 (ZonedDateTime)
     * FORM #4: DATE ONLY e.g. VALUE=DATE:19970304 (LocalDate)
     * 
     * Note: strings can contain optionally contain "VALUE" "=" ("DATE-TIME" / "DATE")) before the date-time portion of the string.
     * e.g. VALUE=DATE:19960401         VALUE=DATE-TIME:19980101T050000Z
     * 
     * Based on ISO.8601.2004
     */
    @Deprecated // put regex into abstract methods and loop through all enums for match
    public
    static Temporal parseTemporal(String temporalString)
    {
        final String form1 = "^[0-9]{8}T([0-9]{6})";
        final String form2 = "^[0-9]{8}T([0-9]{6})Z";
        final String form3 = "^(TZID=.*:)[0-9]{8}T([0-9]{6})";
        final String form4 = "^(VALUE=DATE:)?[0-9]{8}";
        if (temporalString.matches("^VALUE=DATE-TIME:.*")) // remove optional VALUE=DATE-TIME
        {
            temporalString = temporalString.substring(temporalString.indexOf("VALUE=DATE-TIME:")+"VALUE=DATE-TIME:".length()).trim();
        }
        if (temporalString.matches(form1))
        {
            return LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
        } else if (temporalString.matches(form2))
        {
            return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_UTC_FORMATTER);
        } else if (temporalString.matches(form3))
        {
            return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_FORMATTER);            
        } else if (temporalString.matches(form4))
        {
            if (temporalString.matches("^VALUE=DATE:.*"))
            {
                temporalString = temporalString.substring(temporalString.indexOf("VALUE=DATE:")+"VALUE=DATE:".length()).trim();
            }
            return LocalDate.parse(temporalString, LOCAL_DATE_FORMATTER);
        } else
        {
            throw new IllegalArgumentException("String does not match any DATE or DATE-TIME patterns: " + temporalString);
        }
    }
    
    /**
     * Convert temporal, either LocalDate or LocalDateTime to appropriate iCalendar string
     * Examples:
     * 19980119T020000
     * 19980119
     * 
     * @param temporal LocalDate or LocalDateTime
     * @return iCalendar date or date/time string based on ISO.8601.2004
     */
    @Deprecated // put inside abstract methods, loop through to find match
    public
    static String temporalToString(Temporal temporal)
    {
        if (temporal == null) return null;
        if (temporal instanceof ZonedDateTime)
        {
            ZoneOffset offset = ((ZonedDateTime) temporal).getOffset();
            if (offset == ZoneOffset.UTC)
            {
                return ZONED_DATE_TIME_UTC_FORMATTER.format(temporal);
            } else
            {
                return LOCAL_DATE_TIME_FORMATTER.format(temporal); // don't use ZONED_DATE_TIME_FORMATTER because time zone is added to property tag
            }
        } else if (temporal instanceof LocalDateTime)
        {
            return LOCAL_DATE_TIME_FORMATTER.format(temporal);
        } else if (temporal instanceof LocalDate)
        {
            return LOCAL_DATE_FORMATTER.format(temporal);
        } else
        {
            throw new DateTimeException("Invalid temporal type:" + temporal.getClass().getSimpleName());
        }
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
     * @param t - temporal of LocalDate, LocalDateTime or ZonedDateTime
     * @return
     */
    static String makeDateTimePropertyTag(String propertyName, Temporal t)
    {
        if (t instanceof ZonedDateTime)
        {
            String zone = ZONE_FORMATTER.format(t);
            if (zone.isEmpty())
            {
                return propertyName + ":";                
            } else
            {
                return propertyName + ";" + zone + ":";                
            }
        } else
        {
            String prefex = (t instanceof LocalDate) ? ";VALUE=DATE:" : ":";
            return propertyName + prefex;
        }
    }
        
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
        
    /**
     * Returns LocalDateTime from Temporal that is an instance of either LocalDate or LocalDateTime
     * If the parameter is type LocalDate the returned LocalDateTime is atStartofDay.
     * If the parameter is type ZonedDateTime the zoneID is changed to ZoneId.systemDefault() before taking the
     * LocalDateTime.
     * 
     * @param temporal - either LocalDate or LocalDateTime type
     * @return LocalDateTime
     */
    @Deprecated // use TemporalType.toLocalDateTime instead
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

    /** Convert temporal to new DateTimeType  - for DATE_WITH_LOCAL_TIME_AND_TIME_ZONE */
    public abstract Temporal from(Temporal temporal, ZoneId zone);
    /** Convert temporal to new DateTimeType  - for all types, but DATE_WITH_LOCAL_TIME_AND_TIME_ZONE */
    public abstract Temporal from(Temporal temporal);
}