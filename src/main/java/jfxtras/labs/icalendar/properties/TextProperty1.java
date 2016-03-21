//package jfxtras.labs.icalendar.properties;
//
//import java.util.Arrays;
//import java.util.Map;
//
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
//import jfxtras.labs.icalendar.parameters.ICalendarParameter;
//import jfxtras.labs.icalendar.utilities.ICalendarUtilities;
//
///**
// * ICalendar text-based property with language and text
// * 
// * @author David Bal
// *
// * @param <T>
// */
//public class TextProperty1<T> implements VComponentProperty
//{
//    /**
//     * LANGUAGE: RFC 5545 iCalendar 3.2.10. page 21
//     * Optional
//     * To specify the language for text values in a property or property parameter.
//     * Example:
//     *  SUMMARY;LANGUAGE=en-US:Company Holiday Party
//     *  LOCATION;LANGUAGE=no:Tyskland
//     * */
//    public StringProperty languageProperty()
//    {
//        if (language == null) language = new SimpleStringProperty(this, ICalendarParameter.LANGUAGE.toString(), _language);
//        return language;
//    }
//    private StringProperty language;
//    private String _language;
//    public String getLanguage() { return (language == null) ? _language : language.get(); }
//    public void setLanguage(String language)
//    {
//        if (this.language == null)
//        {
//            _language = language;
//        } else
//        {
//            this.language.set(language);            
//        }
//    }
//    public T withLanguage(String language) { setLanguage(language); return (T) this; }
//    
//    /**
//     * Required String part of the property.  This is the part that appears after any
//     * property parameters and the colon symbol
//     * For example
//     * For property SUMMARY:Eat at Joe's
//     * The text is "Eat at Joe's"
//     */
//    public StringProperty valueProperty() { return textProperty; }
//    private String valuePropertyName;
//    final private StringProperty textProperty = new SimpleStringProperty(this, valuePropertyName);
//    @Override
//    public String getValue() { return textProperty.get(); }
//    @Override
//    public void setValue(String value) { textProperty.set(value); }
//    public T withValue(String s) { setValue(s); return (T) this; }
//    
//    /*
//     * CONSTRUCTORS
//     */
//    
//    // construct new object by parsing property line
//    protected TextProperty1(String textPropertyName, String propertyString)
//    {
//        this(textPropertyName);
//        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyString);
//                map.entrySet()
//                .stream()
//                .forEach(e ->
//                {
//                    ICalendarParameter.propertyFromName(e.getKey())
//                            .setValue(this, e.getValue());
//                });
//        setValue(map.get(ICalendarUtilities.PROPERTY_VALUE_KEY));
//    }
//
//    public TextProperty1(String textPropertyName) { this.valuePropertyName = textPropertyName; }
//    
//    // Copy constructor
//    protected TextProperty1(TextProperty1<?> source)
//    {
//        this.valuePropertyName = source.valuePropertyName;
//        Arrays.stream(ICalendarParameter.textProperty1ParameterValues())
//                .forEach(p -> p.copyProperty(source, this));
//    }
//}
