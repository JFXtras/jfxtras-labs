package jfxtras.labs.icalendar.properties;

import java.net.URI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.AlternateText;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.properties.component.descriptive.Comment;
import jfxtras.labs.icalendar.properties.component.descriptive.Description;
import jfxtras.labs.icalendar.properties.component.descriptive.Location;
import jfxtras.labs.icalendar.properties.component.descriptive.Resources;
import jfxtras.labs.icalendar.properties.component.descriptive.Summary;
import jfxtras.labs.icalendar.properties.component.relationship.Contact;

/**
 * Property with language, alternate text display, and a text-based value
 *  
 * @param <T>
 * 
 * concrete subclasses
 * @see Comment
 * @see Contact
 * @see Description
 * @see Location
 * @see Resources
 * @see Summary
 */
public abstract class PropertyBaseAltText<T,U> extends PropertyBaseLanguage<T,U> implements PropertyAltText<U>
{
    /**
     * ALTREP : Alternate Text Representation
     * To specify an alternate text representation for the property value.
     * 
     * Example:
     * DESCRIPTION;ALTREP="CID:part3.msg.970415T083000@example.com":
     *  Project XYZ Review Meeting will include the following agenda
     *   items: (a) Market Overview\, (b) Finances\, (c) Project Man
     *  agement
     *
     *The "ALTREP" property parameter value might point to a "text/html"
     *content portion.
     *
     * Content-Type:text/html
     * Content-Id:<part3.msg.970415T083000@example.com>
     *
     * <html>
     *   <head>
     *    <title></title>
     *   </head>
     *   <body>
     *     <p>
     *       <b>Project XYZ Review Meeting</b> will include
     *       the following agenda items:
     *       <ol>
     *         <li>Market Overview</li>
     *         <li>Finances</li>
     *         <li>Project Management</li>
     *       </ol>
     *     </p>
     *   </body>
     * </html>
     */
    @Override
    public AlternateText getAlternateText() { return (alternateText == null) ? null : alternateText.get(); }
    @Override
    public ObjectProperty<AlternateText> alternateTextProperty()
    {
        if (alternateText == null)
        {
            alternateText = new SimpleObjectProperty<>(this, ParameterEnum.ALTERNATE_TEXT_REPRESENTATION.toString());
        }
        return alternateText;
    }
    private ObjectProperty<AlternateText> alternateText;
    @Override
    public void setAlternateText(AlternateText alternateText)
    {
        if (alternateText != null)
        {
            alternateTextProperty().set(alternateText);
        }
    }
    public void setAlternateText(String value) { setAlternateText(new AlternateText(value)); }
    public T withAlternateText(AlternateText altrep) { setAlternateText(altrep); return (T) this; }
    public T withAlternateText(URI value) { setAlternateText(new AlternateText(value)); return (T) this; }
    public T withAlternateText(String content) { setAlternateText(content); return (T) this; }
    
    /*
     * CONSTRUCTORS
     */    
    protected PropertyBaseAltText(CharSequence contentLine)
    {
        super(contentLine);
    }

    // copy constructor
    public PropertyBaseAltText(PropertyBaseAltText<T,U> property)
    {
        super(property);
        if (getAlternateText() != null)
        {
            setAlternateText(property.getAlternateText());
        }
    }

    public PropertyBaseAltText(U value)
    {
        super(value);
    }
    
//    public PropertyAlternateTextRepresentation() { super(); }
}
