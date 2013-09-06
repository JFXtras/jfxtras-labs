package jfxtras.labs.fxml;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Builder;
import jfxtras.fxml.BuilderService;
import jfxtras.labs.scene.control.CalendarTextField;

/**
 * @author Tom Eugelink
 *
 */
public class CalendarTextFieldBuilder implements BuilderService<CalendarTextField>
{
	/** Locale */
	public String getLocale() { return null; } // dummy, just to make it Java Bean compatible
	public void setLocale(String value) { iLocale = Locale.forLanguageTag(value); }
	private Locale iLocale = null;

	/** PromptText */
	public String getPromptText() { return null; } // dummy, just to make it Java Bean compatible
	public void setPromptText(String value) { iPromptText = value; }
	private String iPromptText = null;

	/** DateFormat */
	public String getDateFormat() { return null; } // dummy, just to make it Java Bean compatible
	public void setDateFormat(String value) { iDateFormat = value; }
	private String iDateFormat = null;

	/** DateFormats */
	public String getDateFormats() { return null; } // dummy, just to make it Java Bean compatible
	public void setDateFormats(String value) 
	{  
		String[] lParts = value.split(",");
		iDateFormats = new ArrayList<>();
		for (String lPart : lParts) 
		{
			iDateFormats.add( lPart.trim() );
		}
	}
	private List<String> iDateFormats = null;

	/**
	 * Implementation of Builder interface
	 */
	@Override
	public CalendarTextField build()
	{
		Locale lLocale = (iLocale == null ? Locale.getDefault() : iLocale);
		CalendarTextField lCalendarTextField = new CalendarTextField();
		if (iDateFormat != null) lCalendarTextField.setDateFormat(new SimpleDateFormat(iDateFormat, lLocale));
		if (iLocale != null) lCalendarTextField.setLocale(iLocale);
		if (iPromptText != null) lCalendarTextField.setPromptText(iPromptText);
		if (iDateFormats != null) 
		{
			ObservableList<DateFormat> lDateFormats = FXCollections.observableArrayList();
			for (String lPart : iDateFormats) 
			{
				lDateFormats.add( new SimpleDateFormat(lPart.trim(), lLocale) );
			}
			lCalendarTextField.setDateFormats(lDateFormats);
		}
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
