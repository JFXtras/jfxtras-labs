package jfxtras.labs.icalendar.properties;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class LanguageAndAltRepBaseBase<T> implements LanguageAndAltRep
{
    private final static String LANGUAGE_NAME = "LANGUAGE";
    private final static String ALTERNATE_TEXT_REPRESENTATION_NAME = "ALTREP";
    
    /**
     *  LANGUAGE: RFC 5545 iCalendar 3.2.10. page 21
     * To specify the language for text values in a property or property parameter.
     * Example:
     *  SUMMARY;LANGUAGE=en-US:Company Holiday Party
     *  LOCATION;LANGUAGE=no:Tyskland
     * */
    @Override
    public StringProperty languageProperty()
    {
        if (language == null) language = new SimpleStringProperty(this, LANGUAGE_NAME, _language);
        return language;
    }
    private StringProperty language;
    private String _language;
    @Override
    public String getLanguage() { return (language == null) ? _language : language.get(); }
    @Override
    public void setLanguage(String language)
    {
        if (this.language == null)
        {
            _language = language;
        } else
        {
            this.language.set(language);            
        }
    }
    public T withLanguage(String comment) { setLanguage(comment); return (T) this; }
    
    /**
     * Alternate Text Representation
     * ALTREP: RFC 5545 iCalendar 3.2.1. page 14
     * To specify an alternate text representation for the property value.
     * Example:
       DESCRIPTION;ALTREP="CID:part3.msg.970415T083000@example.com":
        Project XYZ Review Meeting will include the following agenda
         items: (a) Market Overview\, (b) Finances\, (c) Project Man
        agement

      The "ALTREP" property parameter value might point to a "text/html"
      content portion.

       Content-Type:text/html
       Content-Id:<part3.msg.970415T083000@example.com>

       <html>
         <head>
          <title></title>
         </head>
         <body>
           <p>
             <b>Project XYZ Review Meeting</b> will include
             the following agenda items:
             <ol>
               <li>Market Overview</li>
               <li>Finances</li>
               <li>Project Management</li>
             </ol>
           </p>
         </body>
       </html>
     * */
    @Override
    public StringProperty alternateTextRepresentationProperty()
    {
        if (alternateTextRepresentation == null) alternateTextRepresentation = new SimpleStringProperty(this, ALTERNATE_TEXT_REPRESENTATION_NAME, _alternateTextRepresentation);
        return alternateTextRepresentation;
    }
    private StringProperty alternateTextRepresentation;
    private String _alternateTextRepresentation;
    @Override
    public String getAlternateTextRepresentation() { return (alternateTextRepresentation == null) ? _alternateTextRepresentation : alternateTextRepresentation.get(); }
    @Override
    public void setAlternateTextRepresentation(String alternateTextRepresentation)
    {
        if (this.alternateTextRepresentation == null)
        {
            _alternateTextRepresentation = alternateTextRepresentation;
        } else
        {
            this.alternateTextRepresentation.set(alternateTextRepresentation);            
        }
    }
    public T withAlternateTextRepresentation(String comment) { setLanguage(comment); return (T) this; }
}
