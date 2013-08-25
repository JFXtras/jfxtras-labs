package jfxtras.labs.fxml;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Builder;
import jfxtras.labs.scene.control.CalendarTextField;

/**
 * @author Tom Eugelink
 *
 */
public class CalendarTextFieldBuilder implements BuilderService<CalendarTextField>
{
	/** DateFormat */
	public String getDateFormat() { return null; } // dummy, just to make it Java Bean compatible
	public void setDateFormat(String value) { iDateFormat = new SimpleDateFormat(value); }
	private SimpleDateFormat iDateFormat = null;

	/** Locale */
	public String getLocale() { return null; } // dummy, just to make it Java Bean compatible
	public void setLocale(String value) { iLocale = Locale.forLanguageTag(value); }
	private Locale iLocale = null;

	/** PromptText */
	public String getPromptText() { return null; } // dummy, just to make it Java Bean compatible
	public void setPromptText(String value) { iPromptText = value; }
	private String iPromptText = null;

	/** DateFormats */
	public String getDateFormats() { return null; } // dummy, just to make it Java Bean compatible
	public void setDateFormats(String value) 
	{  
		String[] lParts = value.split(",");
		iDateFormats = FXCollections.observableArrayList();
		for (String lPart : lParts) 
		{
			iDateFormats.add( new SimpleDateFormat(lPart.trim()) );
		}
	}
	private ObservableList<DateFormat> iDateFormats = null;

	/**
	 * Implementation of Builder interface
	 */
	@Override
	public CalendarTextField build()
	{
		CalendarTextField lCalendarTextField = new CalendarTextField();
		if (iDateFormat != null) lCalendarTextField.setDateFormat(iDateFormat);
		if (iLocale != null) lCalendarTextField.setLocale(iLocale);
		if (iPromptText != null) lCalendarTextField.setPromptText(iPromptText);
		if (iDateFormats != null) lCalendarTextField.setDateFormats(iDateFormats);
		return lCalendarTextField;
	}
	
	/**
	 * Implementation of BuilderService interface
	 */
	@Override
	public boolean isBuilderFor(Class<?> clazz)
	{
		return CalendarTextField.class.isAssignableFrom(clazz);
	}
}
