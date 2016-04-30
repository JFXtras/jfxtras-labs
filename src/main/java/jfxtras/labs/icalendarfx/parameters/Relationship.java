package jfxtras.labs.icalendarfx.parameters;

import jfxtras.labs.icalendarfx.parameters.Relationship.RelationshipType;

/**
 * RELTYPE
 * Relationship Type
 * RFC 5545, 3.2.15, page 25
 * 
 * To specify the type of hierarchical relationship associated
 *  with the calendar component specified by the property.
 * 
 * Example:
 * RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@
 *  example.com
 * 
 * @author David Bal
 *
 */
public class Relationship extends ParameterBase<Relationship, RelationshipType>
{
    private String unknownValue; // contains exact string for unknown value

    public Relationship()
    {
        super(RelationshipType.PARENT); // default value
    }
  
    public Relationship(RelationshipType value)
    {
        super(value);
    }

    public Relationship(String content)
    {
        super(RelationshipType.valueOfWithUnknown(content));
        if (getValue() == RelationshipType.UNKNOWN)
        {
            unknownValue = content;
        }
    }
    
    public Relationship(Relationship source)
    {
        super(source);
        unknownValue = source.unknownValue;
    }
    
    @Override
    public String toContent()
    {
        String value = (getValue() == RelationshipType.UNKNOWN) ? unknownValue : getValue().toString();
        String parameterName = myParameterEnum().toString();
        return ";" + parameterName + "=" + value;
    }
    
    public enum RelationshipType
    {
        PARENT,
        CHILD,
        SIBLING,
        UNKNOWN;
        
        /** get enum from name */
        public static RelationshipType valueOfWithUnknown(String propertyName)
        {
            RelationshipType match;
            try
            {
                match = valueOf(propertyName);
            } catch (Exception e)
            {
                match = UNKNOWN;
            }
            return match;
        }
    }
}
