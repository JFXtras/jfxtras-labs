package jfxtras.labs.icalendarfx.properties.component.descriptive;

import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.parameters.AlternateText;
import jfxtras.labs.icalendarfx.parameters.Encoding;
import jfxtras.labs.icalendarfx.parameters.Encoding.EncodingType;
import jfxtras.labs.icalendarfx.parameters.Language;
import jfxtras.labs.icalendarfx.parameters.OtherParameter;
import jfxtras.labs.icalendarfx.parameters.ValueParameter;
import jfxtras.labs.icalendarfx.properties.PropertyBaseAltText;
import jfxtras.labs.icalendarfx.properties.ValueType;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;

/**
<h2>3.8.1.12.  Summary</h2>

   <p>Property Name:  SUMMARY</p>

   <p>Purpose:  This property defines a short summary or subject for the
      calendar component.</p>

   <p>Value Type:  TEXT</p>

   <p>Property Parameters:  {@link IANAProperty IANA}, {@link NonStandardProperty non-standard},
      {@link AlternateText alternate text representation}, and {@link Language language property}
      parameters can be specified on this property.</p>

   <p>Conformance:  The property can be specified in {@link VEvent VEVENT}, {@link VTodo VTODO},
    {@link VJournal VJOURNAL}, or {@link VAlarm VALARM} calendar components.</p>

   <p>Description:  This property is used in the {@link VEvent VEVENT}, {@link VTodo VTODO} AND
    {@link VJournal VJOURNAL} calendar components to capture a short, one-line
      summary about the activity or journal entry.</p>

      <p>This property is used in the {@link VAlarm VALARM} calendar component to
      capture the subject of an EMAIL category of alarm.</p>

  <p>Format Definition:  This property is defined by the following notation:
  <ul>
  <li>summary
    <ul>
    <li>"{@link Summary SUMMARY} summparam ":" text CRLF
      <ul>
      <li>";" {@link Encoding ENCODING} "=" {@link EncodingType#BASE64 BASE64}
      <li>";" {@link ValueParameter VALUE} "=" {@link ValueType#BINARY BINARY}
      <li>":" binary
      <li>CRLF
      </ul>
    </ul>
  <li>summparam
    <ul>
    <li>The following are OPTIONAL, but MUST NOT occur more than once.
      <ul>
      <li>";" {@link AlternateText altrepparam}
      <li>";" {@link Language languageparam}
      </ul>
    <li>The following are OPTIONAL, and MAY occur more than once.
      <ul>
      <li>";" {@link OtherParameter}
      </ul>
    </ul>
  </ul>
  
  <p>Example:  The following is an example of this property:
  <ul>
  <li>SUMMARY:Department Party
  </ul>
  </p>
  <h2>RFC 5545                       iCalendar                  September 2009</h2>
 */
public class Summary extends PropertyBaseAltText<String, Summary>
{
    public Summary(Summary source)
    {
        super(source);
    }
    
    public Summary()
    {
        super();
    }

    public static Summary parse(String propertyContent)
    {
        Summary property = new Summary();
        property.parseContent(propertyContent);
        return property;
    }
}
