package jfxtras.labs.icalendar;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import org.junit.Test;

import jfxtras.labs.icalendar.mocks.VEventMock;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.DateTimeUtilities;

public class ICalendarToStringTest extends ICalendarTestAbstract
{

    /* Tests both multi-line EXDATE and comma separated EXDATE */
    @Test
    public void canMakeZonedExDateToString()
    {
        VEventMock v = getGoogleWithExDates();

        String madeString = VComponentProperty.EXCEPTIONS.makeContentLine(v);
        String expectedString = "EXDATE;TZID=America/Los_Angeles:20160212T123000" + System.lineSeparator()
                                + "EXDATE;TZID=America/Los_Angeles:20160210T123000" + System.lineSeparator()
                                + "EXDATE;TZID=America/Los_Angeles:20160209T123000";
        assertEquals(expectedString, madeString);
        
        v.setExDatesOnOneLine(true);
        String madeString2 = VComponentProperty.EXCEPTIONS.makeContentLine(v);
        String expectedString2 = "EXDATE;TZID=America/Los_Angeles:20160209T123000,20160210T123000,20160212T123000";
        assertEquals(expectedString2, madeString2);
        
        
    }
    
    /** Tests FREQ=YEARLY */
    @Test
    public void yearly1ToString()
    {
        VEventMock e = getYearly1();
        String madeString = e.toComponentText();
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "CATEGORIES:group13" + System.lineSeparator()
                              + "CREATED:20151109T082900Z" + System.lineSeparator()
                              + "DESCRIPTION:Yearly1 Description" + System.lineSeparator()
                              + "DTSTAMP:20151109T083000Z" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "DURATION:PT1H" + System.lineSeparator()
                              + "LAST-MODIFIED:20151110T183000Z" + System.lineSeparator()
                              + "RRULE:FREQ=YEARLY" + System.lineSeparator()
                              + "SUMMARY:Yearly1 Summary" + System.lineSeparator()
                              + "UID:20151109T082900-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        assertEquals(expectedString, madeString);
    }

    /** Tests FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 */
    @Test
    public void monthly5ToString()
    {

    VEventMock e = getMonthly5();
    String madeString = e.toComponentText();
    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                          + "DTSTAMP:19970901T083000Z" + System.lineSeparator()
                          + "DTSTART:19970613T100000" + System.lineSeparator()
                          + "DURATION:PT1H" + System.lineSeparator()
                          + "RRULE:FREQ=MONTHLY;BYMONTHDAY=13;BYDAY=FR" + System.lineSeparator()
                          + "UID:19970901T083000-0@jfxtras.org" + System.lineSeparator()
                          + "END:VEVENT";
    assertEquals(expectedString, madeString);
    }
    
    /** Tests FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 */
    @Test
    public void monthly6ToString()
    {

    VEventMock e = getMonthly6();
    String madeString = e.toComponentText();
    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                          + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                          + "DTSTART:20151103T100000" + System.lineSeparator()
                          + "DURATION:PT1H30M" + System.lineSeparator()
                          + "RRULE:FREQ=MONTHLY;BYMONTH=11,12;BYDAY=TU,WE,FR" + System.lineSeparator()
                          + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                          + "END:VEVENT";
    assertEquals(expectedString, madeString);
    }
    
    /** Tests FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 */
    @Test
    public void daily6ToString()
    {
    VEventMock e = getDaily6();
    String madeString = e.toComponentText();
    ZonedDateTime until = ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 9, 59, 59), ZoneOffset.systemDefault())
        .withZoneSameInstant(ZoneId.of("Z")); // depends on time zone, so can't be hard coded
    String untilString = VComponent.ZONED_DATE_TIME_UTC_FORMATTER.format(until);
    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                        + "CATEGORIES:group03" + System.lineSeparator()
                        + "DESCRIPTION:Daily6 Description" + System.lineSeparator()
                        + "DTEND:20151109T110000" + System.lineSeparator()
                        + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                        + "DTSTART:20151109T100000" + System.lineSeparator()
                        + "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=" + untilString + System.lineSeparator()
                        + "SUMMARY:Daily6 Summary" + System.lineSeparator()
                        + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                        + "END:VEVENT";
    assertEquals(expectedString, madeString);
    }

    @Test
    public void dailyUTCToString()
    {
    VEventMock e = getDailyUTC();
    String madeString = e.toComponentText();
    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                        + "CATEGORIES:group03" + System.lineSeparator()
                        + "DESCRIPTION:Daily6 Description" + System.lineSeparator()
                        + "DTEND:20151109T110000Z" + System.lineSeparator()
                        + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                        + "DTSTART:20151109T100000Z" + System.lineSeparator()
                        + "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=20151201T095959Z" + System.lineSeparator()
                        + "SUMMARY:Daily6 Summary" + System.lineSeparator()
                        + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                        + "END:VEVENT";
    assertEquals(expectedString, madeString);
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=6
     *  EXDATE=20151112T100000,20151115T100000 */
    @Test
    public void dailyWithException1ToString()
    {

    VEventMock e = getDailyWithException1();
    e.setExDatesOnOneLine(true);
    String madeString = e.toComponentText();
    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                          + "CATEGORIES:group03" + System.lineSeparator()
                          + "DESCRIPTION:Daily2 Description" + System.lineSeparator()
                          + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                          + "DTSTART:20151109T100000" + System.lineSeparator()
                          + "DURATION:PT1H30M" + System.lineSeparator()
                          + "EXDATE:20151112T100000,20151115T100000" + System.lineSeparator()
                          + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6" + System.lineSeparator()
                          + "SUMMARY:Daily2 Summary" + System.lineSeparator()
                          + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                          + "END:VEVENT";
    assertEquals(expectedString, madeString);
    }

    @Test
    public void canConvertWholeDay1()
    {
        Temporal date = LocalDate.of(2015, 11, 12);
        String expectedString = "20151112";
        String madeString = VComponent.temporalToString(date);
        assertEquals(expectedString, madeString);
    }

    @Test
    public void canConvertWholeDay2()
    {
    VEventMock vEvent = getWholeDayDaily1();
    String madeString = vEvent.toComponentText();
    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                          + "DTEND;VALUE=DATE:20151112" + System.lineSeparator()
                          + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                          + "DTSTART;VALUE=DATE:20151109" + System.lineSeparator()
                          + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                          + "END:VEVENT";
    assertEquals(expectedString, madeString);
    }
    
    @Test
    public void canConvertWholeDay3()
    {
    VEventMock vEvent = getWholeDayDaily3();
    String madeString = vEvent.toComponentText();
    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                          + "CATEGORIES:group06" + System.lineSeparator()
                          + "DTEND;VALUE=DATE:20151111" + System.lineSeparator()
                          + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                          + "DTSTART;VALUE=DATE:20151109" + System.lineSeparator()
                          + "RRULE:FREQ=DAILY;INTERVAL=3;UNTIL=20151124" + System.lineSeparator()
                          + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                          + "END:VEVENT";
    assertEquals(expectedString, madeString);
    }
    
    // multi-part recurrence sets
    
//    @Test
//    public void canConvertBranch1ToString()
//    {
//    VEventMock vEvent = getBranch1();
//    String madeString = vEvent.toComponentText();
//    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
//                        + "CATEGORIES:group03" + System.lineSeparator()
//                        + "DESCRIPTION:Daily6 Description" + System.lineSeparator()
//                        + "DTEND:20151201T130000" + System.lineSeparator()
//                        + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
//                        + "DTSTART:20151201T120000" + System.lineSeparator()
//                        + "RELATED-TO:20150110T080000-0@jfxtras.org" + System.lineSeparator()
//                        + "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=20151213T115959" + System.lineSeparator()
//                        + "SUMMARY:Daily6 Summary" + System.lineSeparator()
//                        + "UID:20151201T080000-0@jfxtras.org" + System.lineSeparator()
//                        + "END:VEVENT";
//    assertEquals(expectedString, madeString);
//    }
//    
//    @Test
//    public void canConvertBranch2ToString()
//    {
//    VEventMock vEvent = getBranch2();
//    String madeString = vEvent.toComponentText();
//    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
//                        + "CATEGORIES:group03" + System.lineSeparator()
//                        + "DESCRIPTION:Daily6 Description" + System.lineSeparator()
//                        + "DTEND:20151214T080000" + System.lineSeparator()
//                        + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
//                        + "DTSTART:20151214T060000" + System.lineSeparator()
//                        + "RELATED-TO:20150110T080000-0@jfxtras.org" + System.lineSeparator()
//                        + "RRULE:FREQ=DAILY;INTERVAL=2" + System.lineSeparator()
//                        + "SUMMARY:Daily6 Summary" + System.lineSeparator()
//                        + "UID:20151214T080000-0@jfxtras.org" + System.lineSeparator()
//                        + "END:VEVENT";
//    assertEquals(expectedString, madeString);
//    }
    
    @Test
    public void canConvertChild1ToString()
    {
    VEventMock vEvent = getChild1();
    String madeString = vEvent.toComponentText();
    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                        + "CATEGORIES:group03" + System.lineSeparator()
                        + "DESCRIPTION:Daily6 Description" + System.lineSeparator()
                        + "DTEND:20151122T180000" + System.lineSeparator()
                        + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                        + "DTSTART:20151122T160000" + System.lineSeparator()
                        + "RECURRENCE-ID:20151121T100000" + System.lineSeparator()
                        + "SUMMARY:Daily6 Summary" + System.lineSeparator()
                        + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                        + "END:VEVENT";
    assertEquals(expectedString, madeString);
    }
    
    // Easy-to-read-summary tests
    
    @Test
    public void canMakeRRuleSummaryString1()
    {
        VEventMock e = getDaily6();
        String summaryString = e.getRRule().summary(e.getDateTimeStart());
        String expectedString = "Every 2 days, until Dec 1, 2015";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString2()
    {
        VEventMock e = getYearly1();
        String summaryString = e.getRRule().summary(e.getDateTimeStart());
        String expectedString = "Yearly on November 9";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString3()
    {
        VEventMock e = getMonthly1();
        String summaryString = e.getRRule().summary(e.getDateTimeStart());
        String expectedString = "Monthly on day 9";
        assertEquals(expectedString, summaryString);
    }

    @Test
    public void canMakeRRuleSummaryString4()
    {
        VEventMock e = getMonthly7();
        String summaryString = e.getRRule().summary(e.getDateTimeStart());
        String expectedString = "Monthly on the third Monday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString5()
    {
        VEventMock e = getWeekly1();
        String summaryString = e.getRRule().summary(e.getDateTimeStart());
        String expectedString = "Weekly on Monday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString6()
    {
        VEventMock e = getWeekly2();
        String summaryString = e.getRRule().summary(e.getDateTimeStart());
        String expectedString = "Every 2 weeks on Monday, Wednesday, Friday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString7()
    {
        VEventMock e = getWeekly4();
        String summaryString = e.getRRule().summary(e.getDateTimeStart());
        String expectedString = "Every 2 weeks on Monday, Wednesday, Friday, 11 times";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRangeToString1()
    {
        String dateTimeString = DateTimeUtilities.formatRange(LocalDateTime.of(2015, 11, 11, 10, 0), LocalDateTime.of(2015, 12, 25, 12, 0));
        assertEquals("Wed, November 11, 2015 10:00 AM - Fri, December 25, 2015 12:00 PM", dateTimeString);
        String dateString = DateTimeUtilities.formatRange(LocalDate.of(2015, 11, 9), LocalDate.of(2015, 11, 24));
        assertEquals("Mon, November 9, 2015 - Tue, November 24, 2015", dateString);
        String dateForeverString = DateTimeUtilities.formatRange(LocalDateTime.of(2015, 11, 9, 10, 0), null);
        assertEquals("Mon, November 9, 2015 10:00 AM - forever", dateForeverString);
    }
    

}
