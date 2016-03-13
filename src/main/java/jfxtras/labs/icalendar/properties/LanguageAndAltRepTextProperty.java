package jfxtras.labs.icalendar.properties;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.components.VComponentUtilities.VComponentProperty;
import jfxtras.labs.icalendar.properties.descriptive.Comment;
import jfxtras.labs.icalendar.properties.descriptive.Description;
import jfxtras.labs.icalendar.properties.descriptive.Location;
import jfxtras.labs.icalendar.properties.descriptive.Resources;
import jfxtras.labs.icalendar.properties.descriptive.Summary;
import jfxtras.labs.icalendar.properties.relationship.Contact;

/**
 * Implemented methods for following properties:
 * @see Comment
 * @see Contact
 * @see Description
 * @see Location
 * @see Resources
 * @see Summary
 * 
 * @author David Bal
 * @param <T> - concrete extended class
 */
public abstract class LanguageAndAltRepTextProperty<T>
{
    private final static String LANGUAGE_NAME = "LANGUAGE";
    private final static String ALTERNATE_TEXT_REPRESENTATION_NAME = "ALTREP";
    
    protected LanguageAndAltRepTextProperty(String contentLine)
    {
        // TODO add Language and alternateTextRepresentation
        setText(contentLine);
    }

    // Copy constructor
    protected LanguageAndAltRepTextProperty(LanguageAndAltRepTextProperty originalObject)
    {
        setAlternateTextRepresentation(originalObject.getAlternateTextRepresentation());
        setLanguage(originalObject.getLanguage());
        setText(originalObject.getText());
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        LanguageAndAltRepTextProperty testObj = (LanguageAndAltRepTextProperty) obj;
        boolean languageEquals = (getLanguage() == null) ? (testObj.getLanguage() == null) : getLanguage().equals(testObj.getLanguage());
        boolean alternateTextRepresentationEquals = (getAlternateTextRepresentation() == null) ? (testObj.getAlternateTextRepresentation() == null) : getAlternateTextRepresentation().equals(testObj.getAlternateTextRepresentation());
        boolean textEquals = getText().equals(testObj.getText());
        System.out.println("LanguageAndAltRepTextProperty equals:" + languageEquals + " " + alternateTextRepresentationEquals + " " + textEquals);
        return languageEquals && alternateTextRepresentationEquals && textEquals;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = (31 * hash) + getText().hashCode();
        hash = (31 * hash) + ((getLanguage() == null) ? 0 : getLanguage().hashCode());
        hash = (31 * hash) + ((getAlternateTextRepresentation() == null) ? 0 : getAlternateTextRepresentation().hashCode());
        return hash;
    }
    
    /**
     * LANGUAGE: RFC 5545 iCalendar 3.2.10. page 21
     * Optional
     * To specify the language for text values in a property or property parameter.
     * Example:
     *  SUMMARY;LANGUAGE=en-US:Company Holiday Party
     *  LOCATION;LANGUAGE=no:Tyskland
     * */
    public StringProperty languageProperty()
    {
        if (language == null) language = new SimpleStringProperty(this, LANGUAGE_NAME, _language);
        return language;
    }
    private StringProperty language;
    private String _language;
    public String getLanguage() { return (language == null) ? _language : language.get(); }
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
     * Optional
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
    public StringProperty alternateTextRepresentationProperty()
    {
        if (alternateTextRepresentation == null) alternateTextRepresentation = new SimpleStringProperty(this, ALTERNATE_TEXT_REPRESENTATION_NAME, _alternateTextRepresentation);
        return alternateTextRepresentation;
    }
    private StringProperty alternateTextRepresentation;
    private String _alternateTextRepresentation;
    public String getAlternateTextRepresentation() { return (alternateTextRepresentation == null) ? _alternateTextRepresentation : alternateTextRepresentation.get(); }
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
    
    /**
     * Required String part of the property.  This is the part that appears after the colon symbol
     * For example
     * For property SUMMARY:Eat at Joe's
     * The text is "Eat at Joe's"
     */
    public StringProperty textProperty() { return textProperty; }
    final private StringProperty textProperty = new SimpleStringProperty(this, VComponentProperty.CATEGORIES.toString());
    public String getText() { return textProperty.get(); }
    public void setText(String value) { textProperty.set(value); }
    public T withText(String s) { setText(s); return (T) this; }
}
