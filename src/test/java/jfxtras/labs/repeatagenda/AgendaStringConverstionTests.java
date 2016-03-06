package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.DateTimeUtilities;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.RepeatableController;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;

public class AgendaStringConverstionTests
{
    // Easy-to-read-summary tests for edit popup RRULE summary
    
    @Test
    public void canMakeRRuleSummaryString1()
    {
        VEventImpl e = ICalendarStaticVEvents.getDaily6();
//        String summaryString = e.getRRule().summary(e.getDateTimeStart());
        String summaryString = RepeatableController.makeSummary(e.getRRule(), e.getDateTimeStart());        
        String expectedString = "Every 2 days, until Dec 1, 2015";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString2()
    {
        VEventImpl e = ICalendarStaticVEvents.getYearly1();
        String summaryString = RepeatableController.makeSummary(e.getRRule(), e.getDateTimeStart());        
        String expectedString = "Yearly on November 9";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString3()
    {
        VEventImpl e = ICalendarStaticVEvents.getMonthly1();
        String summaryString = RepeatableController.makeSummary(e.getRRule(), e.getDateTimeStart());        
        String expectedString = "Monthly on day 9";
        assertEquals(expectedString, summaryString);
    }

    @Test
    public void canMakeRRuleSummaryString4()
    {
        VEventImpl e = ICalendarStaticVEvents.getMonthly7();
        String summaryString = RepeatableController.makeSummary(e.getRRule(), e.getDateTimeStart());        
        String expectedString = "Monthly on the third Monday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString5()
    {
        VEventImpl e = ICalendarStaticVEvents.getWeekly1();
        String summaryString = RepeatableController.makeSummary(e.getRRule(), e.getDateTimeStart());        
        String expectedString = "Weekly on Monday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString6()
    {
        VEventImpl e = ICalendarStaticVEvents.getWeekly2();
        String summaryString = RepeatableController.makeSummary(e.getRRule(), e.getDateTimeStart());        
        String expectedString = "Every 2 weeks on Monday, Wednesday, Friday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString7()
    {
        VEventImpl e = ICalendarStaticVEvents.getWeekly4();
        String summaryString = RepeatableController.makeSummary(e.getRRule(), e.getDateTimeStart());        
        String expectedString = "Every 2 weeks on Monday, Wednesday, Friday, 11 times";
        assertEquals(expectedString, summaryString);
    }
    
    /* date-time ranges for edit and delete dialogs */
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
