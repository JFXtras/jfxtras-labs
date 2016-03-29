package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.Relationship.RelationshipType;

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
    public Relationship()
    {
        super();
    }
  
    public Relationship(String content)
    {
        super(content);
    }

    public Relationship(Relationship source)
    {
        super(source);
    }
    
    public enum RelationshipType
    {
        PARENT,
        CHILD,
        SIBLING;

    }
}
