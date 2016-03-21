package jfxtras.labs.icalendar.properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.properties.component.descriptive.Comment;
import jfxtras.labs.icalendar.properties.component.descriptive.Description;
import jfxtras.labs.icalendar.properties.component.descriptive.Location;
import jfxtras.labs.icalendar.properties.component.descriptive.Resources;
import jfxtras.labs.icalendar.properties.component.descriptive.Summary;
import jfxtras.labs.icalendar.properties.component.relationship.Contact;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

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
public abstract class TextPropertyAbstract<T> implements VComponentProperty
{   
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
        if (language == null) language = new SimpleStringProperty(this, TextPropertyParameter.LANGUAGE.toString(), _language);
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
    public T withLanguage(String language) { setLanguage(language); return (T) this; }
    
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
        if (alternateTextRepresentation == null) alternateTextRepresentation = new SimpleStringProperty(this, TextPropertyParameter.ALTERNATE_TEXT_REPRESENTATION.toString(), _alternateTextRepresentation);
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
    public T withAlternateTextRepresentation(String comment) { setAlternateTextRepresentation(comment); return (T) this; }
    
    /**
     * Required String part of the property.  This is the part that appears after any
     * property parameters and the colon symbol
     * For example
     * For property SUMMARY:Eat at Joe's
     * The text is "Eat at Joe's"
     */
    public StringProperty textProperty() { return textProperty; }
    private String textPropertyName;
    final private StringProperty textProperty = new SimpleStringProperty(this, textPropertyName);
    public String getText() { return textProperty.get(); }
    public void setText(String value) { textProperty.set(value); }
    public T withText(String s) { setText(s); return (T) this; }

    /*
     * CONSTRUCTORS
     */
    
    // construct new object by parsing property line
    protected TextPropertyAbstract(String textPropertyName, String propertyString)
    {
        this(textPropertyName);
        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyString);
                map.entrySet()
                .stream()
                .forEach(e ->
                {
                    TextPropertyParameter.propertyFromName(e.getKey())
                            .setValue(this, e.getValue());
                });
        setText(map.get(ICalendarUtilities.PROPERTY_VALUE_KEY));
    }

    public TextPropertyAbstract(String textPropertyName) { this.textPropertyName = textPropertyName; }
    
    // Copy constructor
    protected TextPropertyAbstract(TextPropertyAbstract<?> source)
    {
        this.textPropertyName = source.textPropertyName;
        Arrays.stream(TextPropertyParameter.values())
                .forEach(p -> p.copyProperty(source, this));
    }
    
    @Override
    public String toString()
    {
        return super.toString() + ", " + toContentLine();
    }

    /**
     * Return property content line for iCalendar output files.  See RFC 5545 3.5
     * Contains component property with its value and any populated parameters.
     * 
     * For example: SUMMARY;LANGUAGE=en-US:Company Holiday Party
     * 
     * @return - the content line
     */
    public String toContentLine()
    {
        return textPropertyName + Arrays.stream(TextPropertyParameter.values())
                .map(p -> p.toParameterString(this))
                .filter(s -> ! (s == null))
                .collect(Collectors.joining());        
    }
//    public void parse(String propertyString)
//    {
//        Map<String, String> p = ICalendarUtilities.PropertyLineToParameterMap(propertyString);
//        p.entrySet().stream().forEach(e ->
//        {
//            TextPropertyParameter.propertyFromName(e.getKey()).setValue(this, e.getValue());
//        });
//    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        TextPropertyAbstract<?> testObj = (TextPropertyAbstract<?>) obj;
        boolean languageEquals = (getLanguage() == null) ? (testObj.getLanguage() == null) : getLanguage().equals(testObj.getLanguage());
        boolean alternateTextRepresentationEquals = (getAlternateTextRepresentation() == null) ? (testObj.getAlternateTextRepresentation() == null) : getAlternateTextRepresentation().equals(testObj.getAlternateTextRepresentation());
        boolean textEquals = getText().equals(testObj.getText());
//        System.out.println("text property equals:" + languageEquals + " " + alternateTextRepresentationEquals + " " + textEquals);
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
    
    private enum TextPropertyParameter
    {
        ALTERNATE_TEXT_REPRESENTATION ("ALTREP") {
            @Override
            public void setValue(TextPropertyAbstract<?> textProperty, String alternateTextRepresentation)
            {
                textProperty.setAlternateTextRepresentation(alternateTextRepresentation);
            }

            @Override
            public String toParameterString(TextPropertyAbstract<?> textProperty)
            {
                if (textProperty.getAlternateTextRepresentation() != null)
                {
                    return ";" + toString() + "=" + ICalendarUtilities.addDQuotesIfNecessary(textProperty.getAlternateTextRepresentation());
                }
                return null;
            }

            @Override
            public void copyProperty(TextPropertyAbstract<?> source, TextPropertyAbstract<?> destination)
            {
                destination.setAlternateTextRepresentation(source.getAlternateTextRepresentation());
            }
        },
        LANGUAGE ("LANGUAGE") {
            @Override
            public void setValue(TextPropertyAbstract<?> textProperty, String language)
            {
                textProperty.setLanguage(language);
            }

            @Override
            public String toParameterString(TextPropertyAbstract<?> textProperty)
            {
                if (textProperty.getLanguage() != null)
                {
                    return ";" + toString() + "=" + textProperty.getLanguage();
                }
                return null;
            }

            @Override
            public void copyProperty(TextPropertyAbstract<?> source, TextPropertyAbstract<?> destination)
            {
                destination.setLanguage(source.getLanguage());
            }
        },
        VALUE (ICalendarUtilities.PROPERTY_VALUE_KEY) { // Not a parameter, but the properties value
            @Override
            public void setValue(TextPropertyAbstract<?> textProperty, String value)
            {
                textProperty.setText(value);
            }

            @Override
            public String toParameterString(TextPropertyAbstract<?> textProperty)
            {
                if (textProperty.getText() != null)
                {
                    return ":" + textProperty.getText();
                }
                return null;
            }

            @Override
            public void copyProperty(TextPropertyAbstract<?> source, TextPropertyAbstract<?> destination)
            {
                destination.setText(source.getText());
            }
        };
        
        // Map to match up name to enum
        private static Map<String, TextPropertyParameter> propertyFromNameMap = makePropertiesFromNameMap();
        private static Map<String, TextPropertyParameter> makePropertiesFromNameMap()
        {
            Map<String, TextPropertyParameter> map = new HashMap<>();
            TextPropertyParameter[] values = TextPropertyParameter.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static TextPropertyParameter propertyFromName(String propertyName)
        {
            return propertyFromNameMap.get(propertyName.toUpperCase());
        }
        
        private String name;
        
        TextPropertyParameter(String name)
        {
            this.name = name;
        }
        
        /** Returns the iCalendar property name (e.g. LANGUAGE) */
        @Override public String toString() { return name; }
        
        /** sets parameter value */
        public abstract void setValue(TextPropertyAbstract<?> textProperty, String value);
        
        /** makes content line (RFC 5545 3.1) from a vComponent property  */
        public abstract String toParameterString(TextPropertyAbstract<?> textProperty);
        
        /** Copies property value from source to destination */
        public abstract void copyProperty(TextPropertyAbstract<?> source, TextPropertyAbstract<?> destination);  
    }
}
