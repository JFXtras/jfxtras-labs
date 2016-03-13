package jfxtras.labs.icalendar.properties.relationship;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.components.VComponentUtilities.VComponentProperty;
import jfxtras.labs.icalendar.properties.LanguageAndAltRepBaseBase;

public class Contact extends LanguageAndAltRepBaseBase<Contact>
{
    public Contact(String contentLine)
    {
        super(contentLine);
    }
    /**
     * CONTACT: RFC 5545 iCalendar 3.8.4.2. page 109
     *  This property is used to represent contact information or
     * alternately a reference to contact information associated with the
     * calendar component.
     * Example:
     * CONTACT:Jim Dolittle\, ABC Industries\, +1-919-555-1234
     */
    @Override public StringProperty textProperty() { return textProperty; }
    final private StringProperty textProperty = new SimpleStringProperty(this, VComponentProperty.CATEGORIES.toString());
    @Override public String getText() { return textProperty.get(); }
    @Override public void setText(String value) { textProperty.set(value); }
    public Contact withText(String s) { setText(s); return this; }
    
//    /**
//     * Obtains an instance of Contact by parsing contentLine
//     * 
//     * @param contentLine
//     * @return
//     */
//    public static Contact parseContentLine(String contentLine)
//    {
//        return new Contact(contentLine);
//    }
}
