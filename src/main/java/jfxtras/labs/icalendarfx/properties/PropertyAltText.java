package jfxtras.labs.icalendarfx.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.parameters.AlternateText;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.relationship.Contact;

/**
 * Property with language, alternate text display, and a text-based value
 *  
 * @param <T>
 * 
 * @see PropertyBaseAltText
 * 
 * concrete subclasses
 * @see Comment
 * @see Contact
 * @see Description
 * @see Location
 * @see Resources
 * @see Summary
 */
public interface PropertyAltText<T> extends PropertyLanguage<T>
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
    AlternateText getAlternateText();
    ObjectProperty<AlternateText> alternateTextProperty();
    void setAlternateText(AlternateText alternateText);
}
