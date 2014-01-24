package jfxtras.labs.scene.control.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.CalendarPicker;
import jfxtras.labs.test.JFXtrasGuiTest;
import jfxtras.labs.test.TestUtil;
import jfxtras.labs.util.PlatformUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by tbee on 26-12-13.
 */
public class CalendarPickerTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		VBox box = new VBox();

		calendarPicker = new CalendarPicker();
		box.getChildren().add(calendarPicker);

		calendarPicker.setDisplayedCalendar(new GregorianCalendar(2013, 0, 1, 12, 00, 00));
		return box;
	}
	private CalendarPicker calendarPicker = null;

	/**
	 * 
	 */
	@Test
	public void defaultModeIsSingleWithNull()
	{
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January again
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("[]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}

	/**
	 * 
	 */
	@Test
	public void multipleModeWithNull()
	{
		// change calendarPicker's setting
		calendarPicker.setMode(CalendarPicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of multiple mode, there are two in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 4th of January
		click("#day5");

		// the selected value should be changed, and because of multiple mode, there are three in calendars
		Assert.assertEquals("2013-01-04", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January (unselecting it)
		click("#day3");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 1st of January (unselecting it)
		click("#day2");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-04", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-04]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 4th of January (unselecting it)
		click("#day5");

		// the first value in the list should be selected
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("[]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}

	/**
	 * 
	 */
	@Test
	public void multipleModeWithNullSelectingRange()
	{
		// change calendarPicker's setting
		calendarPicker.setMode(CalendarPicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// shift click the 3rd of January
		click("#day4", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 5th of January
		click("#day6");

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03, 2013-01-05]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January (unselecting it)
		click("#day3");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-03, 2013-01-05]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}
	
	/**
	 * 
	 */
	@Test
	public void rangeModeWithNullSelectingSingles()
	{
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// click the 2nd of January again
		click("#day3");

		// the first value in the list should be selected
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("[]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}

	/**
	 * 
	 */
	@Test
	public void rangeModeWithNullSelectingRange()
	{
		// change calendarPicker's setting
		calendarPicker.setMode(CalendarPicker.Mode.RANGE);

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// shift click the 3rd of January
		click("#day4", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// shift click the 5th of January (extending the range)
		click("#day6", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01, 2013-01-02, 2013-01-03, 2013-01-04, 2013-01-05]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
		
		// click the 10th of January (switching to a single date)
		click("#day11");

		// the last selected value should be set
		Assert.assertEquals("2013-01-10", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-10]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));

		// shift click the 5th of January (selecting a range downwards)
		click("#day6", KeyCode.SHIFT);

		// the last selected value should be set 
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendarAsDate(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-10, 2013-01-09, 2013-01-08, 2013-01-07, 2013-01-06, 2013-01-05]", TestUtil.quickFormatCalendarsAsDate(calendarPicker.calendars()));
	}

	/**
	 * 
	 */
	@Test
	public void singleModeWithTime()
	{
		PlatformUtil.runAndWait( () -> {
			// show time
			calendarPicker.setShowTime(true);
		});
		
		// click the 1st of January
		click("#day2");

		// first of January at midnight
		Assert.assertEquals("2013-01-01T00:00:00.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));		
	}

	/**
	 * 
	 */
	@Test
	public void singleModeWithTimeSlide()
	{
		PlatformUtil.runAndWait( () -> {
			// show time
			calendarPicker.setShowTime(true);
		});
		TestUtil.waitForPaintPulse(); // required for JavaFX to load the the skin of the TimePicker, otherwise it is null
		
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// set a value
		final Calendar lCalendar = new GregorianCalendar(2013, 00, 01, 12, 34, 56);
		PlatformUtil.runAndWait( () -> {
			calendarPicker.setCalendar( (Calendar)lCalendar.clone() );
		});
		Assert.assertEquals("2013-01-01T12:34:56.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));

		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-01T16:34:56.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));
		
		// move the minute slider
		move("#minuteSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-150,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-01T16:18:56.000", TestUtil.quickFormatCalendarAsDateTime(calendarPicker.getCalendar()));
	}
}
