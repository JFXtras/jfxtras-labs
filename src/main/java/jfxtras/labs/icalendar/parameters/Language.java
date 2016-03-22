package jfxtras.labs.icalendar.parameters;

/**
 * LANGUAGE
 * To specify the language for text values in a property or property parameter.
 * 
 * Examples:
 * SUMMARY;LANGUAGE=en-US:Company Holiday Party
 * LOCATION;LANGUAGE=no:Tyskland
 */
public class Language extends ParameterTextBase
{
    private final static String NAME = ParameterEnum.LANGUAGE.toString();
    
//    // SHOULD LOCALE BE USED?
//    public String getLanguage() { return language.get(); }
//    public StringProperty languageProperty() { return language; }
//    private StringProperty language = new SimpleStringProperty(this, ICalendarParameter.LANGUAGE.toString());
//    public void setLanguage(String language) { this.language.set(language); }
    
    /*
     * CONSTRUCTOR
     */
    public Language()
    {
        super(NAME);
    }
  
    public Language(String content)
    {
        super(NAME, content);
    }

    // copy constructor
    public Language(Language source)
    {
        super(NAME, source);
    }
   

    
}
