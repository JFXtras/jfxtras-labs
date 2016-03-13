package jfxtras.labs.icalendar.properties.descriptive;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.components.VComponentUtilities.VComponentProperty;
import jfxtras.labs.icalendar.properties.LanguageAndAltRepBaseBase;

public class Comment extends LanguageAndAltRepBaseBase<Comment>
{
    /**
     * COMMENT: RFC 5545 iCalendar 3.8.1.4. page 83
     * This property specifies non-processing information intended
     * to provide a comment to the calendar user
     * Example:
     * COMMENT:The meeting really needs to include both ourselves
         and the customer. We can't hold this meeting without them.
         As a matter of fact\, the venue for the meeting ought to be at
         their site. - - John
     */
    @Override public StringProperty textProperty() { return textProperty; }
    final private StringProperty textProperty = new SimpleStringProperty(this, VComponentProperty.CATEGORIES.toString());
    @Override public String getText() { return textProperty.get(); }
    @Override public void setText(String value) { textProperty.set(value); }
    public Comment withText(String s) { setText(s); return this; }
}
