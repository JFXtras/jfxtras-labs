package jfxtras.labs.scene.control.test;

import java.util.GregorianCalendar;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.CalendarPicker;
import jfxtras.labs.test.JFXtrasGuiTest;
import jfxtras.labs.test.TestUtil;

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

		calendarPicker.setDisplayedCalendar(new GregorianCalendar(2013, 0, 1));
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
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January again
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("[]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));
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
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of multiple mode, there are two in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01,2013-01-02]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// click the 4th of January
		click("#day5");

		// the selected value should be changed, and because of multiple mode, there are three in calendars
		Assert.assertEquals("2013-01-04", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01,2013-01-02,2013-01-04]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January (unselecting it)
		click("#day3");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01,2013-01-04]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// click the 1st of January (unselecting it)
		click("#day2");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-04", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-04]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// click the 4th of January (unselecting it)
		click("#day5");

		// the first value in the list should be selected
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("[]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));
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
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// shift click the 3rd of January
		click("#day4", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01,2013-01-02,2013-01-03]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// click the 5th of January
		click("#day6");

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01,2013-01-02,2013-01-03,2013-01-05]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January (unselecting it)
		click("#day3");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01,2013-01-03,2013-01-05]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));
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
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-02]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January again
		click("#day3");

		// the first value in the list should be selected
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("[]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));
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
		Assert.assertEquals("2013-01-01", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// shift click the 3rd of January
		click("#day4", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01,2013-01-02,2013-01-03]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// shift click the 5th of January (extending the range)
		click("#day6", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-01,2013-01-02,2013-01-03,2013-01-04,2013-01-05]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));
		
		// click the 10th of January (switching to a single date)
		click("#day11");

		// the last selected value should be set
		Assert.assertEquals("2013-01-10", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-10]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));

		// shift click the 5th of January (selecting a range downwards)
		click("#day6", KeyCode.SHIFT);

		// the last selected value should be set 
		Assert.assertEquals("2013-01-05", TestUtil.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("[2013-01-10,2013-01-09,2013-01-08,2013-01-07,2013-01-06,2013-01-05]", TestUtil.quickFormatCalendar(calendarPicker.calendars()));
	}
}
