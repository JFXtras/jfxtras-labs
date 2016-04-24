package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleParameter;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Yearly;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

public class RecurrenceRuleTest
{
    @Test
    public void canParseRecurrenceRule()
    {
        String content = "RRULE:FREQ=YEARLY;BYMONTH=1,2";
        RecurrenceRule madeProperty = new RecurrenceRule(content);
        assertEquals(content, madeProperty.toContentLine());
        RecurrenceRule expectedProperty = new RecurrenceRule(
                new RecurrenceRuleParameter()
                .withFrequency(new Yearly()
                        .withByRules(new ByMonth(Month.JANUARY, Month.FEBRUARY))));
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseRRuleProperty()
    {
        String s = "RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=2";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=2");
        assertEquals(expectedMap, valueMap);

        String s2 = "FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=2";
        SortedMap<String, String> valueMap2 = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s2));
        SortedMap<String, String> expectedMap2 = new TreeMap<>();
        expectedMap2.put("FREQ", "DAILY");
        expectedMap2.put("UNTIL", "20160417T235959Z");
        expectedMap2.put("INTERVAL", "2");
        assertEquals(expectedMap2, valueMap2);
    }
    
    /*
     * STREAM RECURRENCES TESTS
     */
    
    /** tests converting ISO.8601.2004 date-time string to LocalDateTime */
    /** Tests daily stream with FREQ=YEARLY */
    @Test
    public void yearlyStreamTest1()
    {
        VEventNew e = new VEventNew()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRuleParameter()
                        .withFrequency(new Yearly()));
        List<Temporal> madeDates = e
                .streamRecurrences(e.getDateTimeStart().getValue())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2016, 11, 9, 10, 0)
              , LocalDateTime.of(2017, 11, 9, 10, 0)
              , LocalDateTime.of(2018, 11, 9, 10, 0)
              , LocalDateTime.of(2019, 11, 9, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY";
        assertEquals(expectedContent, e.getRecurrenceRule().toContentLine());
    }
    
    /** Tests daily stream with FREQ=YEARLY;BYDAY=FR */
    @Test
    public void yearlyStreamTest2()
    {
        VEventNew e = new VEventNew()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 6, 10, 0))
                .withRecurrenceRule(new RecurrenceRuleParameter()
                        .withFrequency(new Yearly()
                                .withByRules(new ByDay(DayOfWeek.FRIDAY))));
        List<Temporal> madeDates = e
                .streamRecurrences(e.getDateTimeStart().getValue())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 6, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 4, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYDAY=FR";
        assertEquals(expectedContent, e.getRecurrenceRule().toContentLine());
    }
    
    /** FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8 */
    @Test
    public void yearlyStreamTest3()
    {
        VEventNew e = new VEventNew()
                .withDateTimeStart(LocalDateTime.of(1997, 6, 5, 9, 0))
                .withRecurrenceRule(new RecurrenceRuleParameter()
                        .withFrequency(new Yearly()
                                .withByRules(new ByDay(DayOfWeek.THURSDAY)
                                           , new ByMonth(Month.JUNE, Month.JULY, Month.AUGUST))));
        List<Temporal> madeDates = e
                .streamRecurrences(e.getDateTimeStart().getValue())
                .limit(20)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 6, 5, 9, 0)
              , LocalDateTime.of(1997, 6, 12, 9, 0)
              , LocalDateTime.of(1997, 6, 19, 9, 0)
              , LocalDateTime.of(1997, 6, 26, 9, 0)
              , LocalDateTime.of(1997, 7, 3, 9, 0)
              , LocalDateTime.of(1997, 7, 10, 9, 0)
              , LocalDateTime.of(1997, 7, 17, 9, 0)
              , LocalDateTime.of(1997, 7, 24, 9, 0)
              , LocalDateTime.of(1997, 7, 31, 9, 0)
              , LocalDateTime.of(1997, 8, 7, 9, 0)
              , LocalDateTime.of(1997, 8, 14, 9, 0)
              , LocalDateTime.of(1997, 8, 21, 9, 0)
              , LocalDateTime.of(1997, 8, 28, 9, 0)
              , LocalDateTime.of(1998, 6, 4, 9, 0)
              , LocalDateTime.of(1998, 6, 11, 9, 0)
              , LocalDateTime.of(1998, 6, 18, 9, 0)
              , LocalDateTime.of(1998, 6, 25, 9, 0)
              , LocalDateTime.of(1998, 7, 2, 9, 0)
              , LocalDateTime.of(1998, 7, 9, 9, 0)
              , LocalDateTime.of(1998, 7, 16, 9, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYMONTH=6,7,8;BYDAY=TH";
        assertEquals(expectedContent, e.getRecurrenceRule().toContentLine());
    }
    
    /** FREQ=YEARLY;BYMONTH=1,2 */
    @Test
    public void yearlyStreamTest4()
    {
        VEventNew e = new VEventNew()
                .withDateTimeStart(LocalDateTime.of(2015, 1, 6, 10, 0))
                .withRecurrenceRule(new RecurrenceRuleParameter()
                        .withFrequency(new Yearly()
                                .withByRules(new ByMonth(Month.JANUARY, Month.FEBRUARY))));
        List<Temporal> madeDates = e
                .streamRecurrences(e.getDateTimeStart().getValue())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 1, 6, 10, 0)
              , LocalDateTime.of(2015, 2, 6, 10, 0)
              , LocalDateTime.of(2016, 1, 6, 10, 0)
              , LocalDateTime.of(2016, 2, 6, 10, 0)
              , LocalDateTime.of(2017, 1, 6, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYMONTH=1,2";
        assertEquals(expectedContent, e.getRecurrenceRule().toContentLine());
    }
    
    /** FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10 */
    @Test
    public void yearlyStreamTest5()
    {
        VEventNew e = new VEventNew()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 0, 0))
                .withRecurrenceRule(new RecurrenceRuleParameter()
                        .withFrequency(new Yearly()
                                .withByRules(new ByMonth(Month.NOVEMBER)
                                           , new ByMonthDay(10))));
        List<Temporal> madeDates = e
                .streamRecurrences(e.getDateTimeStart().getValue())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 10, 0, 0)
              , LocalDateTime.of(2016, 11, 10, 0, 0)
              , LocalDateTime.of(2017, 11, 10, 0, 0)
              , LocalDateTime.of(2018, 11, 10, 0, 0)
              , LocalDateTime.of(2019, 11, 10, 0, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10";
        assertEquals(expectedContent, e.getRecurrenceRule().toContentLine());

    }
    
    /** FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYMONTHDAY=2,3,4,5,6,7,8;BYDAY=TU
     * (U.S. Presidential Election day) */
    @Test
    public void yearlyStreamTest6()
    {
        VEventNew e = new VEventNew()
                .withDateTimeStart(LocalDateTime.of(1996, 11, 5, 0, 0))
                .withRecurrenceRule(new RecurrenceRuleParameter()
                        .withFrequency(new Yearly()
                                .withInterval(4)
                                .withByRules(new ByMonth(Month.NOVEMBER)
                                           , new ByDay(DayOfWeek.TUESDAY)
                                           , new ByMonthDay(2,3,4,5,6,7,8))));
        List<Temporal> madeDates = e
                .streamRecurrences(e.getDateTimeStart().getValue())
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1996, 11, 5, 0, 0)
              , LocalDateTime.of(2000, 11, 7, 0, 0)
              , LocalDateTime.of(2004, 11, 2, 0, 0)
              , LocalDateTime.of(2008, 11, 4, 0, 0)
              , LocalDateTime.of(2012, 11, 6, 0, 0)
              , LocalDateTime.of(2016, 11, 8, 0, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYMONTHDAY=2,3,4,5,6,7,8;BYDAY=TU";
        assertEquals(expectedContent, e.getRecurrenceRule().toContentLine());
    }
}
