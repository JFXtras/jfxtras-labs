package jfxtras.labs.scene.control.test;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import jfxtras.labs.internal.scene.control.skin.ListSpinnerCaspianSkin;
import jfxtras.labs.scene.control.CalendarPicker;
import jfxtras.labs.scene.control.ListSpinner;
import jfxtras.labs.util.PlatformUtil;

import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.util.GregorianCalendar;

/**
 * Created by tbee on 26-12-13.
 */
public class CalendarPickerTest extends GuiTest {

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
		Assert.assertEquals("2013-01-01", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("1x [2013-01-01]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("1x [2013-01-02]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January again
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("0x []", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));
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
		Assert.assertEquals("2013-01-01", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("1x [2013-01-01]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of multiple mode, there are two in calendars
		Assert.assertEquals("2013-01-02", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("2x [2013-01-01,2013-01-02]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 4th of January
		click("#day5");

		// the selected value should be changed, and because of multiple mode, there are three in calendars
		Assert.assertEquals("2013-01-04", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("3x [2013-01-01,2013-01-02,2013-01-04]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January (unselecting it)
		click("#day3");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-01", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("2x [2013-01-01,2013-01-04]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 1st of January (unselecting it)
		click("#day2");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-04", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("1x [2013-01-04]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 4th of January (unselecting it)
		click("#day5");

		// the first value in the list should be selected
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("0x []", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));
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
		Assert.assertEquals("2013-01-01", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("1x [2013-01-01]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// shift click the 3rd of January
		press(KeyCode.SHIFT).click("#day4").release(KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("3x [2013-01-01,2013-01-02,2013-01-03]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 5th of January
		click("#day6");

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("4x [2013-01-01,2013-01-02,2013-01-03,2013-01-05]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January (unselecting it)
		click("#day3");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-01", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("3x [2013-01-01,2013-01-03,2013-01-05]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));
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
		Assert.assertEquals("2013-01-01", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("1x [2013-01-01]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("1x [2013-01-02]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January again
		click("#day3");

		// the first value in the list should be selected
		Assert.assertNull(calendarPicker.getCalendar());
		Assert.assertEquals("0x []", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));
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
		Assert.assertEquals("2013-01-01", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("1x [2013-01-01]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// shift click the 3rd of January
		press(KeyCode.SHIFT).click("#day4").release(KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("3x [2013-01-01,2013-01-02,2013-01-03]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// shift click the 5th of January (extending the range)
		press(KeyCode.SHIFT).click("#day6").release(KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-05", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("5x [2013-01-01,2013-01-02,2013-01-03,2013-01-04,2013-01-05]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));
		
		// click the 10th of January (switching to a single date)
		click("#day11");

		// the last selected value should be set
		Assert.assertEquals("2013-01-10", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("1x [2013-01-10]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// shift click the 5th of January (extending the range)
		press(KeyCode.SHIFT).click("#day6").release(KeyCode.SHIFT);

		// the last selected value should be set 
		// TODO: 2013-01-05 & sorted down calendars?
		Assert.assertEquals("2013-01-10", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals("6x [2013-01-05,2013-01-06,2013-01-07,2013-01-08,2013-01-09,2013-01-10]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));
	}
}
