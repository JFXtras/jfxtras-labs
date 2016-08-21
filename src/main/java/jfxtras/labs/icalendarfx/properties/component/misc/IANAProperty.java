package jfxtras.labs.icalendarfx.properties.component.misc;

import java.util.Arrays;
import java.util.List;

/**
   <h2>3.8.8.1.  IANA Properties</h2>
   
   <p>Property Name:  An IANA-registered property name</p>

   <p>Value Type:  The default value type is TEXT.  The value type can be
      set to any value type.</p>

   <p>Property Parameters:  Any parameter can be specified on this
      property.</p>

   <p>Description:  This specification allows other properties registered
      with IANA to be specified in any calendar components.  Compliant
      applications are expected to be able to parse these other IANA-
      registered properties but can ignore them.</p>

   <p>Format Definition:  This property is defined by the following
      notation:</p>
  <ul>
  <li>iana-prop
    <ul>
    <li>iana-token *(";" icalparameter) ":" value CRLF
    </ul>
  </ul>
  </p>
  
  <p>Example:  The following are examples of properties that might be
      registered to IANA:
  <ul>
  <li>DRESSCODE:CASUAL
  <li>NON-SMOKING;VALUE=BOOLEAN:TRUE
  </ul>
  </p>
  <h2>RFC 5545                       iCalendar                  September 2009</h2>
 * 
 * @author David Bal
 */
public class IANAProperty extends UnknownProperty<Object, IANAProperty>
{
    public static final List<String> REGISTERED_IANA_PROPERTY_NAMES = 
            Arrays.asList("TESTPROP1", "TESTPROP2");
    
    /** Returns true if name is a registered IANA property, false otherwise */
    public static boolean isIANAProperty(String name)
    {
        return REGISTERED_IANA_PROPERTY_NAMES.contains(name);
    }
      
    public IANAProperty(Object value)
    {
        super(value);
    }
    
    public IANAProperty(IANAProperty source)
    {
        super(source);
    }
    
    public IANAProperty()
    {
        super();
    }
    
    @Override
    public boolean isValid()
    {
        return REGISTERED_IANA_PROPERTY_NAMES.contains(getPropertyName()) && super.isValid();
    }
    
    public static IANAProperty parse(String value)
    {
        IANAProperty property = new IANAProperty();
        property.parseContent(value);
        return property;
    }
}
