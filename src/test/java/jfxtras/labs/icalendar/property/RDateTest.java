package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.icalendar.ICalendarUtilities;
import jfxtras.labs.icalendar.properties.recurrence.RDate;

public class RDateTest
{
    @Test
    public void canParseRDate()
    {
//        String s = "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904";
//        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap2(s);
        String s2 = "ORGANIZER;CN=David Bal;SENT-BY=\"mailto:ddbal1@yahoo.com\":mailto:ddbal1@yahoo.com";
        Map<String, String> map2 = ICalendarUtilities.propertyLineToParameterMap2(s2);
//        String s3 = "STATUS:CONFIRMED";
//        Map<String, String> map3 = ICalendarUtilities.propertyLineToParameterMap2(s3);
//        String s4 = "RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1";
//        Map<String, String> map4 = ICalendarUtilities.propertyLineToParameterMap2(s4);

//        map.entrySet().stream().forEach(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
        map2.entrySet().stream().forEach(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
//        map3.entrySet().stream().forEach(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
//        map4.entrySet().stream().forEach(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
    }
    
    @Test
    public void canConvertRDateToString()
    {
        RDate rDate = new RDate()
                .withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0)
                     , LocalDateTime.of(2015, 11, 14, 12, 0));
        assertEquals("20151112T100000,20151114T120000", rDate.toString()); // property name is added in enum
    }

    @Test
    public void canChageTemporalClassAfterClear()
    {
        RDate rDate = new RDate()
                .withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0)
                     , LocalDateTime.of(2015, 11, 14, 12, 0));
        rDate.getTemporals().clear();
        rDate.getTemporals().add(LocalDate.of(2015, 11, 14));
        rDate.getTemporals().add(LocalDate.of(2015, 11, 17));
        assertEquals(2, rDate.getTemporals().size());
    }
    
    /*
     * Error checking
     */
    @Test(expected=DateTimeException.class)
    @Ignore // Doesn't catch exception - wrong thread?
    public void canDetectTemporalClassChange()
    {
        RDate rDate = new RDate().withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0)
                     , LocalDate.of(2015, 11, 14));
        assertEquals(1, rDate.getTemporals().size());
    }
    
//  @Rule
//  public final ExpectedException exception = ExpectedException.none();
//  
//  @Test // (expected=IllegalArgumentException.class)
//  public void canStopTwoSameByRules()
//  {
//      try {
//          Yearly y = new Yearly();
//          y.byRules().add(new ByMonth(Month.JANUARY));
//          y.byRules().add(new ByDay(DayOfWeek.SUNDAY));
//          y.byRules().add(new ByMonth(Month.MARCH));
//      } catch (Exception ex) {
//          System.out.println("ok");
////          assertEquals("Can't add ByMonth (BYMONTH) more than once.", ex.getMessage());
//      }
////      fail("expected IllegalArgumentException Can't add ByMonth (BYMONTH) more than once.");
////      exception.expect(RuntimeException.class);
////      exception.expectMessage("Can't add ByMonth (BYMONTH) more than once.");
//  }
//  
//  @Test(expected=IndexOutOfBoundsException.class)
//  public void testIndexOutOfBoundsException() {
//      ArrayList emptyList = new ArrayList();
//      Object o = emptyList.get(0);
//  }
//  
////  @Rule
////  public final ExpectedException exception = ExpectedException.none();
//  
//  @Test
//  public void doStuffThrowsIndexOutOfBoundsException() {
//      ArrayList emptyList = new ArrayList();
//
//    exception.expect(IndexOutOfBoundsException.class);
//    Object o = emptyList.get(0);
//  }
    
}
