package jfxtras.labs.scene.control.test;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.CalendarPicker;
import jfxtras.labs.scene.control.LocalDateTimePicker;
import jfxtras.labs.test.JFXtrasGuiTest;
import jfxtras.labs.test.TestUtil;
import jfxtras.labs.util.PlatformUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by tbee on 26-12-13.
 */
public class LocalDateTimePickerTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		VBox box = new VBox();

		localDateTimePicker = new LocalDateTimePicker();
		box.getChildren().add(localDateTimePicker);

		localDateTimePicker.setDisplayedLocalDateTime(LocalDateTime.of(2013, 1, 1, 12, 00, 00));
		return box;
	}
	private LocalDateTimePicker localDateTimePicker = null;

	/**
	 * 
	 */
	@Test
	public void defaultModeIsSingleWithNull()
	{
		// default value is null
		Assert.assertNull( localDateTimePicker.getLocalDateTime());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00]", localDateTimePicker.localDateTimes().toString());

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-02T00:00]", localDateTimePicker.localDateTimes().toString());

		// click the 2nd of January again
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertNull( localDateTimePicker.getLocalDateTime());
		Assert.assertEquals("[]", localDateTimePicker.localDateTimes().toString());
	}

	/**
	 * 
	 */
	@Test
	public void multipleModeWithNull()
	{
		// change localDateTimePicker's setting
		localDateTimePicker.setMode(CalendarPicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull( localDateTimePicker.getLocalDateTime());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00]", localDateTimePicker.localDateTimes().toString());

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of multiple mode, there are two in calendars
		Assert.assertEquals("2013-01-02T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00, 2013-01-02T00:00]", localDateTimePicker.localDateTimes().toString());

		// click the 4th of January
		click("#day5");

		// the selected value should be changed, and because of multiple mode, there are three in calendars
		Assert.assertEquals("2013-01-04T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00, 2013-01-02T00:00, 2013-01-04T00:00]", localDateTimePicker.localDateTimes().toString());

		// click the 2nd of January (unselecting it)
		click("#day3");

		// since the selected calendar was not unselected, it stays the same
		Assert.assertEquals("2013-01-04T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00, 2013-01-04T00:00]", localDateTimePicker.localDateTimes().toString());

		// click the 4th of January (unselecting it)
		click("#day5");

		// the first value in the list should be selected
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00]", localDateTimePicker.localDateTimes().toString());

		// click the 1st of January (unselecting it)
		click("#day2");

		// the first value in the list should be selected
		Assert.assertNull( localDateTimePicker.getLocalDateTime());
		Assert.assertEquals("[]", localDateTimePicker.localDateTimes().toString());
	}

	/**
	 * 
	 */
	@Test
	public void multipleModeWithNullSelectingRange()
	{
		// change localDateTimePicker's setting
		localDateTimePicker.setMode(CalendarPicker.Mode.MULTIPLE);

		// default value is null
		Assert.assertNull( localDateTimePicker.getLocalDateTime());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00]", localDateTimePicker.localDateTimes().toString());

		// shift click the 3rd of January
		click("#day4", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00, 2013-01-02T00:00, 2013-01-03T00:00]", localDateTimePicker.localDateTimes().toString());

		// click the 5th of January
		click("#day6");

		// the last selected value should be set
		Assert.assertEquals("2013-01-05T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00, 2013-01-02T00:00, 2013-01-03T00:00, 2013-01-05T00:00]", localDateTimePicker.localDateTimes().toString());

		// click the 2nd of January (unselecting it)
		click("#day3");

		// since the selected calendar was not unselected, it stays the samt
		Assert.assertEquals("2013-01-05T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00, 2013-01-03T00:00, 2013-01-05T00:00]", localDateTimePicker.localDateTimes().toString());
	}
	
	/**
	 * 
	 */
	@Test
	public void rangeModeWithNullSelectingSingles()
	{
		// default value is null
		Assert.assertNull( localDateTimePicker.getLocalDateTime());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00]", localDateTimePicker.localDateTimes().toString());

		// click the 2nd of January
		click("#day3");

		// the selected value should be changed, and because of single mode, it is also the only one in calendars
		Assert.assertEquals("2013-01-02T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-02T00:00]", localDateTimePicker.localDateTimes().toString());

		// click the 2nd of January again
		click("#day3");

		// the first value in the list should be selected
		Assert.assertNull( localDateTimePicker.getLocalDateTime());
		Assert.assertEquals("[]", localDateTimePicker.localDateTimes().toString());
	}

	/**
	 * 
	 */
	@Test
	public void rangeModeWithNullSelectingRange()
	{
		// change localDateTimePicker's setting
		localDateTimePicker.setMode(CalendarPicker.Mode.RANGE);

		// default value is null
		Assert.assertNull( localDateTimePicker.getLocalDateTime());

		// click the 1st of January
		click("#day2");

		// the last selected value should be set
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00]", localDateTimePicker.localDateTimes().toString());

		// shift click the 3rd of January
		click("#day4", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-03T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00, 2013-01-02T00:00, 2013-01-03T00:00]", localDateTimePicker.localDateTimes().toString());

		// shift click the 5th of January (extending the range)
		click("#day6", KeyCode.SHIFT);

		// the last selected value should be set
		Assert.assertEquals("2013-01-05T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-01T00:00, 2013-01-02T00:00, 2013-01-03T00:00, 2013-01-04T00:00, 2013-01-05T00:00]", localDateTimePicker.localDateTimes().toString());
		
		// click the 10th of January (switching to a single date)
		click("#day11");

		// the last selected value should be set
		Assert.assertEquals("2013-01-10T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-10T00:00]", localDateTimePicker.localDateTimes().toString());

		// shift click the 5th of January (selecting a range downwards)
		click("#day6", KeyCode.SHIFT);

		// the last selected value should be set 
		Assert.assertEquals("2013-01-05T00:00", localDateTimePicker.getLocalDateTime().toString());
		Assert.assertEquals("[2013-01-10T00:00, 2013-01-09T00:00, 2013-01-08T00:00, 2013-01-07T00:00, 2013-01-06T00:00, 2013-01-05T00:00]", localDateTimePicker.localDateTimes().toString());
	}

	/**
	 * 
	 */
	@Test
	public void singleModeWithTime()
	{
		PlatformUtil.runAndWait( () -> {
			// show time
			localDateTimePicker.setShowTime(true);
		});
		
		// click the 1st of January
		click("#day2");

		// first of January at midnight
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());		
	}

	/**
	 * 
	 */
	@Test
	public void singleModeWithTimeSlide()
	{
		PlatformUtil.runAndWait( () -> {
			// show time
			localDateTimePicker.setShowTime(true);
		});
		TestUtil.waitForPaintPulse(); // required for JavaFX to load the the skin of the TimePicker, otherwise it is null
		
		// default value is null
		Assert.assertNull( localDateTimePicker.getLocalDateTime());

		// set a value
		final Calendar lCalendar = new GregorianCalendar(2013, 00, 01, 12, 34, 56);
		PlatformUtil.runAndWait( () -> {
			localDateTimePicker.setCalendar( (Calendar)lCalendar.clone() );
		});
		Assert.assertEquals("2013-01-01T12:34:56", localDateTimePicker.getLocalDateTime().toString());

		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-01T16:34:56", localDateTimePicker.getLocalDateTime().toString());
		
		// move the minute slider
		move("#minuteSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-150,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("2013-01-01T16:18:56", localDateTimePicker.getLocalDateTime().toString());
	}
	
	
	/**
	 * 
	 */
	@Test
	public void notNullWhileNull()
	{
		// default value is null
		Assert.assertNull(localDateTimePicker.getLocalDateTime());

		PlatformUtil.runAndWait( () -> {
			// show time
			localDateTimePicker.setAllowNull(false);
		});
		
		// not null caused a value to be set, which defaults to now 
		// These asserts will be green about 99.9999% of the time, because we only check on the date, not the time. Only when a date transition occurs exactly between the setAllowNull and the assert will they fail.
		Assert.assertEquals( TestUtil.quickFormatCalendarAsDate(Calendar.getInstance()), localDateTimePicker.getLocalDateTime().toLocalDate().toString());
		Assert.assertEquals(1, localDateTimePicker.localDateTimes().size());
		Assert.assertEquals(TestUtil.quickFormatCalendarAsDate(Calendar.getInstance()), localDateTimePicker.localDateTimes().get(0).toLocalDate().toString());
	}

	
	/**
	 * 
	 */
	@Test
	public void notNullWhileSet()
	{
		// default value is null
		Assert.assertNull(localDateTimePicker.getLocalDateTime());

		// click the 1st of January
		click("#day2");

		// first of January
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());		

		PlatformUtil.runAndWait( () -> {
			// show time
			localDateTimePicker.setAllowNull(false);
		});
		
		// click the 1st of January again (which would unselect in allow null mode)
		click("#day2");

		// first of January
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getLocalDateTime().toString());		
	}


	/**
	 * 
	 */
	@Test
	public void navigateYear()
	{
		// Jan 2013 is shown
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());
		
		// click next in year
		click("#yearListSpinner .right-arrow");

		// Jan 2014 is shown
		Assert.assertEquals("2014-01-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());
		
		// click next in month
		click("#monthListSpinner .right-arrow");

		// Feb 2014 is shown
		Assert.assertEquals("2014-02-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());
		
		// click 2x prev in month
		click("#monthListSpinner .left-arrow");
		click("#monthListSpinner .left-arrow");

		// Dec 2013 is shown
		Assert.assertEquals("2013-12-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());

		// click prev in year
		click("#yearListSpinner .left-arrow");

		// Dec 2012 is shown
		Assert.assertEquals("2012-12-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());
		
		// click next in year
		click("#monthListSpinner .right-arrow");

		// Jan 2013 is shown
		Assert.assertEquals("2013-01-01T00:00", localDateTimePicker.getDisplayedLocalDateTime().toString());
	}
}
