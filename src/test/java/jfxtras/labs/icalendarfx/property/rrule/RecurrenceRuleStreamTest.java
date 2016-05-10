package jfxtras.labs.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleNew;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule3;

public class RecurrenceRuleStreamTest
{
    /*
    DTSTART;VALUE=DATE:20160611
    RRULE:FREQ=MONTHLY;BYMONTHDAY=7,8,9,10,11,12,13;BYDAY=SA
     */
    @Test
    public void canStreamRRule1()
    {
        String s = "FREQ=MONTHLY;BYMONTHDAY=7,8,9,10,11,12,13;BYDAY=SA";
        RecurrenceRule3 rRule = RecurrenceRule3.parse(s);
        Temporal dateTimeStart = LocalDate.of(2016, 6, 11);
        List<LocalDate> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDate.of(2016, 6, 11)
              , LocalDate.of(2016, 7, 9)
              , LocalDate.of(2016, 8, 13)
              , LocalDate.of(2016, 9, 10)
              , LocalDate.of(2016, 10, 8)
                ));
        List<Temporal> madeRecurrences = rRule.streamRecurrences(dateTimeStart).limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);

    }
    
    /*
    DTSTART;VALUE=DATE:20160630
    RRULE:FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-1
    
    Last workday of the month
     */
    @Test
    public void canStreamRRule2()
    {
        String s = "FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-1";
        RecurrenceRule3 rRule = RecurrenceRule3.parse(s);
        Temporal dateTimeStart = LocalDate.of(2016, 6, 30);
        List<LocalDate> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDate.of(2016, 6, 30)
              , LocalDate.of(2016, 7, 29)
              , LocalDate.of(2016, 8, 31)
              , LocalDate.of(2016, 9, 30)
              , LocalDate.of(2016, 10, 31)
                ));
        List<Temporal> madeRecurrences = rRule.streamRecurrences(dateTimeStart).limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
     * Examples from RFC 5545, page 123-132
     */
    
    /*
     DTSTART;TZID=America/New_York:19970902T090000
     RRULE:FREQ=DAILY;COUNT=10
     */
    @Test
    public void canStreamRRule3()
    {
        String s = "FREQ=DAILY;COUNT=10";
        RecurrenceRule3 rRule = RecurrenceRule3.parse(s);
        assertEquals(rRule.toContent(), s);
        Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
        List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 3, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 4, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 5, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 6, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 7, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 8, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 9, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 10, 9, 0), ZoneId.of("America/New_York"))
              , ZonedDateTime.of(LocalDateTime.of(1997, 9, 11, 9, 0), ZoneId.of("America/New_York"))
              ));
        List<Temporal> madeRecurrences = rRule.streamRecurrences(dateTimeStart).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART;TZID=America/New_York:19970902T090000
    RRULE:FREQ=DAILY;UNTIL=19971224T000000Z
    */
   @Test
   public void canStreamRRule4()
   {
       String s = "FREQ=DAILY;UNTIL=19971224T000000Z";
       RecurrenceRule3 rRule = RecurrenceRule3.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(1, ChronoUnit.DAYS))
               .limit(113)
               .collect(Collectors.toList());
       List<Temporal> madeRecurrences = rRule.streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=DAILY;INTERVAL=2
   */
  @Test
  public void canStreamRRule5()
  {
      String s = "RRULE:FREQ=DAILY;INTERVAL=2";
      RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
      assertEquals(rRule.toContent(), s);
      Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
      List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
              ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
            , ZonedDateTime.of(LocalDateTime.of(1997, 9, 4, 9, 0), ZoneId.of("America/New_York"))
            , ZonedDateTime.of(LocalDateTime.of(1997, 9, 6, 9, 0), ZoneId.of("America/New_York"))
            , ZonedDateTime.of(LocalDateTime.of(1997, 9, 8, 9, 0), ZoneId.of("America/New_York"))
            , ZonedDateTime.of(LocalDateTime.of(1997, 9, 10, 9, 0), ZoneId.of("America/New_York"))
            ));
      List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(5).collect(Collectors.toList());
      assertEquals(expectedRecurrences, madeRecurrences);
  }
  
    /*
    DTSTART;TZID=America/New_York:19970902T090000
    RRULE:FREQ=DAILY;INTERVAL=10;COUNT=5
    */
   @Test
   public void canStreamRRule6()
   {
       String s = "RRULE:FREQ=DAILY;INTERVAL=10;COUNT=5";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 12, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 22, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 12, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(5).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19980101T090000
   RRULE:FREQ=YEARLY;UNTIL=20000131T140000Z;BYMONTH=1;BYDAY=SU,MO,TU,WE,TH,FR,SA
   or
   RRULE:FREQ=DAILY;UNTIL=20000131T140000Z;BYMONTH=1
   */
   @Test
   public void canStreamRRule7()
   {
       String s = "RRULE:FREQ=YEARLY;UNTIL=20000131T140000Z;BYMONTH=1;BYDAY=SU,MO,TU,WE,TH,FR,SA";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1998, 1, 1, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(1, ChronoUnit.DAYS))
               .limit(31)
               .collect(Collectors.toList());
       expectedRecurrences.addAll(Stream
               .iterate(dateTimeStart.plus(1, ChronoUnit.YEARS), a -> a.plus(1, ChronoUnit.DAYS))
               .limit(31)
               .collect(Collectors.toList()));
       expectedRecurrences.addAll(Stream
               .iterate(dateTimeStart.plus(2, ChronoUnit.YEARS), a -> a.plus(1, ChronoUnit.DAYS))
               .limit(31)
               .collect(Collectors.toList()));
       
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
       
       String s2 = "RRULE:FREQ=DAILY;UNTIL=20000131T140000Z;BYMONTH=1";
       RecurrenceRuleNew rRule2 = RecurrenceRuleNew.parse(s2);
       List<Temporal> madeRecurrences2 = rRule2.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences2);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=WEEKLY;COUNT=10
    */

   @Test
   public void canStreamRRule8()
   {
       String s = "RRULE:FREQ=WEEKLY;COUNT=10";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(1, ChronoUnit.WEEKS))
               .limit(10)
               .collect(Collectors.toList());
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
  
   /*
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=WEEKLY;UNTIL=19971224T000000Z
    */
   @Test
   public void canStreamRRule9()
   {
       String s = "RRULE:FREQ=WEEKLY;UNTIL=19971224T000000Z";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(1, ChronoUnit.WEEKS))
               .limit(17)
               .collect(Collectors.toList());
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=WEEKLY;INTERVAL=2;WKST=SU
    */
   @Test
   public void canStreamRRule10()
   {
       String s = "RRULE:FREQ=WEEKLY;INTERVAL=2;WKST=SU";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(2, ChronoUnit.WEEKS))
               .limit(100)
               .collect(Collectors.toList());
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(100).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH
    */
   @Test
   public void canStreamRRule11()
   {
       String s = "RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 4, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 9, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 16, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 18, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 23, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 25, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 2, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
       
       String s2 = "RRULE:FREQ=WEEKLY;COUNT=10;WKST=SU;BYDAY=TU,TH";
       RecurrenceRuleNew rRule2 = RecurrenceRuleNew.parse(s2);
       List<Temporal> madeRecurrences2 = rRule2.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences2);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970901T090000
   RRULE:FREQ=WEEKLY;INTERVAL=2;UNTIL=19971224T000000Z;WKST=SU;BYDAY=MO,WE,FR
    */
   @Test
   public void canStreamRRule12()
   {
       String s = "RRULE:FREQ=WEEKLY;INTERVAL=2;UNTIL=19971224T000000Z;WKST=SU;BYDAY=MO,WE,FR";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 1, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = Stream
               .iterate(dateTimeStart, a -> a.plus(2, ChronoUnit.WEEKS))
               .flatMap(v -> 
               {
                   List<Temporal> t = new ArrayList<>();
                   t.add(v); // monday
                   t.add(v.plus(2, ChronoUnit.DAYS)); // wednesday
                   t.add(v.plus(4, ChronoUnit.DAYS)); // friday
                   return t.stream();
               })
               .limit(25)
               .collect(Collectors.toList());
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=8;WKST=SU;BYDAY=TU,TH
    */
   @Test
   public void canStreamRRule13()
   {
       String s = "RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=8;WKST=SU;BYDAY=TU,TH";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 4, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 16, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 18, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 14, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 16, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
     DTSTART;TZID=America/New_York:19970905T090000
     RRULE:FREQ=MONTHLY;COUNT=10;BYDAY=1FR
    */
   @Test
   public void canStreamRRule14()
   {
       String s = "RRULE:FREQ=MONTHLY;COUNT=10;BYDAY=1FR";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 5, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 5, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 3, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 7, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 5, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 2, 6, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 6, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 4, 3, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 5, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 6, 5, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970905T090000
   RRULE:FREQ=MONTHLY;UNTIL=19971224T000000Z;BYDAY=1FR
    */
   @Test
   public void canStreamRRule15()
   {
       String s = "RRULE:FREQ=MONTHLY;UNTIL=19971224T000000Z;BYDAY=1FR";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 5, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 5, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 3, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 7, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 5, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970907T090000
   RRULE:FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU
    */
   @Test
   public void canStreamRRule16()
   {
       String s = "RRULE:FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 7, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 7, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 28, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 4, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 25, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 29, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 5, 3, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 5, 31, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970922T090000
   RRULE:FREQ=MONTHLY;COUNT=6;BYDAY=-2MO
    */
   @Test
   public void canStreamRRule17()
   {
       String s = "RRULE:FREQ=MONTHLY;COUNT=6;BYDAY=-2MO";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 22, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 22, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 20, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 17, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 22, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 19, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 2, 16, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970928T090000
   RRULE:FREQ=MONTHLY;BYMONTHDAY=-3
    */
   @Test
   public void canStreamRRule18()
   {
       String s = "RRULE:FREQ=MONTHLY;BYMONTHDAY=-3";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 28, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 28, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 29, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 28, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 29, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 29, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 2, 26, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(6).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=MONTHLY;COUNT=10;BYMONTHDAY=2,15
    */
   @Test
   public void canStreamRRule19()
   {
       String s = "RRULE:FREQ=MONTHLY;BYMONTHDAY=2,15";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 15, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970930T090000
   RRULE:FREQ=MONTHLY;COUNT=10;BYMONTHDAY=1,-1
    */
   @Test
   public void canStreamRRule20()
   {
       String s = "RRULE:FREQ=MONTHLY;COUNT=10;BYMONTHDAY=1,-1";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 10, 31, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 12, 31, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 31, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 2, 1, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970910T090000
   RRULE:FREQ=MONTHLY;INTERVAL=18;COUNT=10;BYMONTHDAY=10,11,12,13,14,15
    */
   @Test
   public void canStreamRRule21()
   {
       String s = "RRULE:FREQ=MONTHLY;INTERVAL=18;COUNT=10;BYMONTHDAY=10,11,12,13,14,15";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 10, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 12, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 13, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 14, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 15, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 12, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 13, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
   DTSTART;TZID=America/New_York:19970902T090000
   RRULE:FREQ=MONTHLY;INTERVAL=2;BYDAY=TU
    */
   @Test
   public void canStreamRRule22()
   {
       String s = "RRULE:FREQ=MONTHLY;INTERVAL=2;BYDAY=TU";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 9, 2, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 9, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 16, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 23, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 9, 30, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 4, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 18, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 11, 25, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 6, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 13, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 20, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 1, 27, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 3, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 17, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 24, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 3, 31, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(18).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
      
   /*
   DTSTART;TZID=America/New_York:19970610T090000
   RRULE:FREQ=YEARLY;COUNT=10;BYMONTH=6,7
    */
   @Test
   public void canStreamRRule23()
   {
       String s = "RRULE:FREQ=YEARLY;COUNT=10;BYMONTH=6,7";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 6, 10, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 6, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 7, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 6, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 7, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 6, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 7, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 6, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 7, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2001, 6, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2001, 7, 10, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every other year on January, February, and March for 10 occurrences:
    * 
   DTSTART;TZID=America/New_York:19970310T090000
   RRULE:FREQ=YEARLY;INTERVAL=2;COUNT=10;BYMONTH=1,2,3
    */
   @Test
   public void canStreamRRule24()
   {
       String s = "RRULE:FREQ=YEARLY;INTERVAL=2;COUNT=10;BYMONTH=1,2,3";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 3, 10, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 1, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 2, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2001, 1, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2001, 2, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2001, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 1, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 2, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 3, 10, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
      
   /*
    * Every third year on the 1st, 100th, and 200th day for 10 occurrences:
    *       
   DTSTART;TZID=America/New_York:19970101T090000
   RRULE:FREQ=YEARLY;INTERVAL=3;COUNT=10;BYYEARDAY=1,100,200
    */
   @Test
   public void canStreamRRule25()
   {
       String s = "RRULE:FREQ=YEARLY;INTERVAL=3;COUNT=10;BYYEARDAY=1,100,200";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 1, 1, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 1, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 4, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1997, 7, 19, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 4, 9, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2000, 7, 18, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 1, 1, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 4, 10, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2003, 7, 19, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(2006, 1, 1, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(10).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Every 20th Monday of the year, forever:
    * 
   DTSTART;TZID=America/New_York:19970519T090000
   RRULE:FREQ=YEARLY;BYDAY=20MO
    */
   @Test
   public void canStreamRRule26()
   {
       String s = "RRULE:FREQ=YEARLY;BYDAY=20MO";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 5, 19, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 5, 19, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 5, 18, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 5, 17, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(3).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
   
   /*
    * Monday of week number 20 (where the default start of the week is Monday), forever:
    * 
   DTSTART;TZID=America/New_York:19970512T090000
   RRULE:FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO
    */
   @Test
   public void canStreamRRule27()
   {
       String s = "RRULE:FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO";
       RecurrenceRuleNew rRule = RecurrenceRuleNew.parse(s);
       assertEquals(rRule.toContent(), s);
       Temporal dateTimeStart = ZonedDateTime.of(LocalDateTime.of(1997, 5, 12, 9, 0), ZoneId.of("America/New_York"));
       List<Temporal> expectedRecurrences = new ArrayList<>(Arrays.asList(
               ZonedDateTime.of(LocalDateTime.of(1997, 5, 12, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1998, 5, 11, 9, 0), ZoneId.of("America/New_York"))
             , ZonedDateTime.of(LocalDateTime.of(1999, 5, 17, 9, 0), ZoneId.of("America/New_York"))
             ));
       List<Temporal> madeRecurrences = rRule.getValue().streamRecurrences(dateTimeStart).limit(3).collect(Collectors.toList());
       assertEquals(expectedRecurrences, madeRecurrences);
   }
}
