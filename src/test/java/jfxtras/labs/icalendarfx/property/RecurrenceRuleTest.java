package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleNew;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Count;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Frequency2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Interval;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule3;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Until;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.WeekStart;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByYearDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Yearly;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

public class RecurrenceRuleTest
{
    @Test
    public void canParseRecurrenceRule()
    {
        String content = "RRULE:FREQ=YEARLY;BYMONTH=1,2";
        RecurrenceRule madeProperty = RecurrenceRule.parse(content);
        assertEquals(content, madeProperty.toContent());
        RecurrenceRule expectedProperty = new RecurrenceRule(
                new RecurrenceRule2()
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
    
    @Test
    public void canParseRecurrenceRule2()
    {
        String content = "RRULE:FREQ=YEARLY;UNTIL=19730429T070000Z;BYMONTH=4;BYDAY=-1SU";
        RecurrenceRule madeProperty = RecurrenceRule.parse(content);
        assertEquals(content, madeProperty.toContent());
        RecurrenceRule expectedProperty = new RecurrenceRule(
                new RecurrenceRule2()
                    .withUntil("19730429T070000Z")
                    .withFrequency(new Yearly()
                            .withByRules(new ByMonth(Month.APRIL),
                                    new ByDay(new ByDay.ByDayPair(DayOfWeek.SUNDAY, -1)))));
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseRecurrenceRule2New()
    {
        String content = "RRULE:FREQ=YEARLY;UNTIL=19730429T070000Z;BYMONTH=4;BYDAY=-1SU";
        RecurrenceRuleNew madeProperty = RecurrenceRuleNew.parse(content);
        System.out.println(madeProperty.toContent() + " " + madeProperty.getValue().getFrequency());
        assertEquals(content, madeProperty.toContent());
        RecurrenceRuleNew expectedProperty = new RecurrenceRuleNew(
                new RecurrenceRule3()
                    .withUntil("19730429T070000Z")
                    .withFrequency(new Frequency2(FrequencyType.YEARLY))
                    .withByRules(new ByMonth(Month.APRIL),
                                 new ByDay(new ByDay.ByDayPair(DayOfWeek.SUNDAY, -1))));
//        System.out.println(expectedProperty.getValue().elements());

        System.out.println(expectedProperty.toContent());
        assertEquals(expectedProperty, madeProperty);
    }
    
    /*
     * TEST RECURRENCE RULE ELEMENTS
     */
    
    @Test
    public void canParseFrequency()
    {
        String content = "DAILY";
        Frequency2 element = Frequency2.parse(content);
        assertEquals(FrequencyType.DAILY, element.getValue());
        assertEquals("FREQ=DAILY", element.toContent());
    }
    
    @Test
    public void canParseUntil()
    {
        String content = "19730429T070000Z";
        Until element = Until.parse(content);
        ZonedDateTime t = ZonedDateTime.of(LocalDateTime.of(1973, 4, 29, 7, 0), ZoneId.of("Z"));
        assertEquals(t, element.getValue());
        assertEquals("UNTIL=19730429T070000Z", element.toContent());
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongUntil()
    {
        String content = "19730429T070000";
        Until.parse(content);
    }
    
    @Test
    public void canParseCount()
    {
        Count element = Count.parse("2");
        assertEquals((Integer) 2, element.getValue());
        assertEquals("COUNT=2", element.toContent());
    }
    
    @Test (expected = IllegalArgumentException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchNegativeCount()
    {
        Count element = new Count(5);
        assertEquals((Integer) 5, element.getValue());
        element.setValue(0);
        assertEquals("COUNT=5", element.toContent());
    }
    
    @Test
    public void canParseInterval()
    {
        Interval element = Interval.parse("2");
        assertEquals((Integer) 2, element.getValue());
        assertEquals("INTERVAL=2", element.toContent());
    }

    @Test
    public void canParseWeekStart()
    {
        WeekStart element = new WeekStart().withValue("SU");
        assertEquals(DayOfWeek.SUNDAY, element.getValue());
        assertEquals("WKST=SU", element.toContent());
    }

    @Test
    public void canParseByMonth()
    {
        ByMonth element = new ByMonth(4);
        assertEquals(Month.APRIL, element.getValue().get(0));
        assertEquals("BYMONTH=4", element.toContent());
    }
    
    @Test
    public void canStreamByMonth()
    {
        ByMonth element = new ByMonth(4);
        LocalDateTime startDateTime = LocalDateTime.of(2016, 5, 5, 10, 0);
        ChronoUnit chronoUnit = ChronoUnit.DAYS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, chronoUnit);
        Stream<Temporal> inStream = Stream.iterate(startDateTime, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, chronoUnit, startDateTime);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2017, 4, 1, 10, 0)
              , LocalDateTime.of(2017, 4, 2, 10, 0)
              , LocalDateTime.of(2017, 4, 3, 10, 0)
              , LocalDateTime.of(2017, 4, 4, 10, 0)
              , LocalDateTime.of(2017, 4, 5, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test
    public void canStreamByMonth2()
    {
        ByMonth element = new ByMonth(4,5);
        LocalDateTime startDateTime = LocalDateTime.of(2016, 5, 5, 10, 0);
        ChronoUnit chronoUnit = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, chronoUnit);
        Stream<Temporal> inStream = Stream.iterate(startDateTime, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, chronoUnit, startDateTime);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 5, 10, 0)
              , LocalDateTime.of(2017, 4, 5, 10, 0)
              , LocalDateTime.of(2017, 5, 5, 10, 0)
              , LocalDateTime.of(2018, 4, 5, 10, 0)
              , LocalDateTime.of(2018, 5, 5, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }

    @Test
    public void canParseByWeekNumber()
    {
        ByWeekNumber element = new ByWeekNumber(4,5);
        assertEquals(Arrays.asList(4,5), element.getValue());
        assertEquals("BYWEEKNO=4,5", element.toContent());
    }
    
    @Test
    public void canStreamByWeekNumber()
    {
        ByWeekNumber element = new ByWeekNumber(20);
        LocalDateTime startDateTime = LocalDateTime.of(1997, 5, 12, 10, 0);
        ChronoUnit chronoUnit = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, chronoUnit);
        Stream<Temporal> inStream = Stream.iterate(startDateTime, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, chronoUnit, startDateTime);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(1997, 5, 12, 10, 0)
              , LocalDateTime.of(1998, 5, 11, 10, 0)
              , LocalDateTime.of(1999, 6, 17, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(3).peek(System.out::println).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }

//    @Test
//    public void canCountWeekNumbers()
//    {
//        Locale myLocale = Locale.FRANCE;
//        System.out.println("size:" + myLocale.getExtensionKeys().size());
//        myLocale.getExtensionKeys().stream().forEach(System.out::println);
//        
////        Locale.setDefault(Locale.FRANCE);
//        LocalDate firstDay = LocalDate.of(2016, 1, 1);
//        WeekFields weekFields = WeekFields.of(Locale.FRANCE);
//        System.out.println("min week days:" + weekFields.getMinimalDaysInFirstWeek());
//        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, ChronoUnit.DAYS);
//        boolean allMatch = Stream.iterate(firstDay, a -> a.with(adjuster))
//                .limit(1000)
//                .allMatch(d ->
//                {
//                    int weekFieldsWeekNumber = d.get(weekFields.weekOfWeekBasedYear());
//                    int myWeekNumber = ByWeekNumber.calcWeekNumber(d, DayOfWeek.MONDAY);
//                    System.out.println(myWeekNumber + " " + weekFieldsWeekNumber + " " + d);
//                    return myWeekNumber == weekFieldsWeekNumber;
//                });
//        assertTrue(allMatch);
//    }

    @Test
    public void canParseByYearDay()
    {
        ByYearDay element = new ByYearDay(100,200,300);
        assertEquals(Arrays.asList(100,200,300), element.getValue());
        assertEquals("BYYEARDAY=100,200,300", element.toContent());
    }
    
    @Test
    public void canParseByDay()
    {
        ByDay element = ByDay.parse("-1SU");
        ByDayPair byDayPair = new ByDayPair()
                .withDayOfWeek(DayOfWeek.SUNDAY)
                .withOrdinal(-1);
        assertEquals(byDayPair, element.getValue().get(0));
        assertEquals("BYDAY=-1SU", element.toContent());
    }
}
