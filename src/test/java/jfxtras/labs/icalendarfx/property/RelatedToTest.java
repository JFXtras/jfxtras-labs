package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.parameters.Relationship.RelationshipType;
import jfxtras.labs.icalendarfx.properties.component.relationship.RelatedTo;

public class RelatedToTest
{
    @Test
    public void canParseRelatedTo()
    {
        String expectedContent = "RELATED-TO:jsmith.part7.19960817T083000.xyzMail@example.com";
        RelatedTo madeProperty = new RelatedTo(expectedContent);
        assertEquals(expectedContent, madeProperty.toContentLine());
        RelatedTo expectedProperty = new RelatedTo("jsmith.part7.19960817T083000.xyzMail@example.com");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseRelatedTo2()
    {
        String expectedContent = "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com";
        RelatedTo madeProperty = new RelatedTo(expectedContent);
        assertEquals(expectedContent, madeProperty.toContentLine());
        RelatedTo expectedProperty = new RelatedTo("19960401-080045-4000F192713@example.com")
                .withRelationship(RelationshipType.SIBLING);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(RelationshipType.SIBLING, madeProperty.getRelationship().getValue());
    }
    
    @Test
    public void canParseRelatedTo3()
    {
        String expectedContent = "RELATED-TO;RELTYPE=CUSTOM RELATIONSHIP:fc3577e0-8155-4fa2-a085-a15bdc50a5b4";
        RelatedTo madeProperty = new RelatedTo(expectedContent);
        assertEquals(expectedContent, madeProperty.toContentLine());
        RelatedTo expectedProperty = new RelatedTo("fc3577e0-8155-4fa2-a085-a15bdc50a5b4")
                .withRelationship("CUSTOM RELATIONSHIP");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(RelationshipType.UNKNOWN, madeProperty.getRelationship().getValue());
    }
}
