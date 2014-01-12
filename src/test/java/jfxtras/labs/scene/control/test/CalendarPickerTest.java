package jfxtras.labs.scene.control.test;

import javafx.scene.Parent;
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

	public Parent getRootNode()
	{
		VBox box = new VBox();

		calendarPicker = new CalendarPicker();
		box.getChildren().add(calendarPicker);

		calendarPicker.setDisplayedCalendar(new GregorianCalendar(2013, 0, 1));
		return box;
	}
	private CalendarPicker calendarPicker = null;

	@Test
	public void defaultModeIsSingleSelectWithNulls()
	{
		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the selected value should be set
		Assert.assertEquals("2013-01-01", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals(1, calendarPicker.calendars().size());
		Assert.assertEquals("2013-01-01", CalendarPicker.quickFormatCalendar(calendarPicker.calendars().get(0)));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals(1, calendarPicker.calendars().size());
		Assert.assertEquals("2013-01-02", CalendarPicker.quickFormatCalendar(calendarPicker.calendars().get(0)));
	}

	@Test
	public void multipleModeWithNull()
	{
		// change calendarPicker's setting
		calendarPicker.setMode(CalendarPicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull(calendarPicker.getCalendar());

		// click the 1st of January
		click("#day2");

		// the selected value should be set
		Assert.assertEquals("2013-01-01", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals(1, calendarPicker.calendars().size());
		Assert.assertEquals("1x [2013-01-01]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02", CalendarPicker.quickFormatCalendar(calendarPicker.getCalendar()));
		Assert.assertEquals(2, calendarPicker.calendars().size());
		Assert.assertEquals("2x [2013-01-01,2013-01-02]", CalendarPicker.quickFormatCalendar(calendarPicker.calendars()));
	}
}
