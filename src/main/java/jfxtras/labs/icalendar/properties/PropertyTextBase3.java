package jfxtras.labs.icalendar.properties;

import java.net.URI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
 * @see Comment
 * @see Contact
 * @see Description
 * @see Location
 * @see Resources
 * @see Summary
 */
public abstract class PropertyTextBase3<T> extends PropertyTextBase2<T>
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
    public URI getAlternateTextRepresentation() { return (alternateTextRepresentation == null) ? _alternateTextRepresentation : alternateTextRepresentation.get(); }
    public ObjectProperty<URI> alternateTextRepresentationProperty()
    {
        if (alternateTextRepresentation == null)
        {
            alternateTextRepresentation = new SimpleObjectProperty<>(this, ParameterEnum.ALTERNATE_TEXT_REPRESENTATION.toString(), _alternateTextRepresentation);
//            alternateTextRepresentation.addListener((observable, oldValue, newValue) ->
//            {
//                if (newValue == null)
//                {
//                    parameters().remove(oldValue);
//                } else
//                {
//                    parameters().add(newValue);
//                }
//            });
        }
        return alternateTextRepresentation;
    }
    private URI _alternateTextRepresentation;
    private ObjectProperty<URI> alternateTextRepresentation;
    public void setAlternateTextRepresentation(URI alternateTextRepresentation)
    {
        if (alternateTextRepresentation != null)
        {
            parameters().put(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, alternateTextRepresentation);
        } else
        {
            parameters().remove(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION);            
        }
        if (this.alternateTextRepresentation == null)
        {
            _alternateTextRepresentation = alternateTextRepresentation;
        } else
        {
            this.alternateTextRepresentation.set(alternateTextRepresentation);
        }
    }
    public T withAlternateTextRepresentation(URI uri) { setAlternateTextRepresentation(uri); return (T) this; }
    
    /*
     * CONSTRUCTORS
     */    
    protected PropertyTextBase3(String propertyString)
    {
        super(propertyString);
    }

    // copy constructor
    public PropertyTextBase3(PropertyTextBase3<T> property)
    {
        super(property);
        if (getAlternateTextRepresentation() != null)
        {
            setAlternateTextRepresentation(property.getAlternateTextRepresentation());
        }
    }

    public PropertyTextBase3() { super(); }
}
