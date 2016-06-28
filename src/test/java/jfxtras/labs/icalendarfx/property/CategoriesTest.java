package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;

public class CategoriesTest
{
    @Test
    public void canParseCategories()
    {
        Categories property = new Categories("group03","g,roup\\;p");
        String expectedContent = "CATEGORIES:group03,g\\,roup\\\\\\;p";
        assertEquals(expectedContent, property.toContent());
        Categories property2 = Categories.parse(expectedContent);
        assertEquals(property, property2);
    }
    
    @Test
    public void canParseMultipleCategories()
    {
        Categories property = Categories.parse("CATEGORIES:group03,group04,group05");
        String expectedSummary = "CATEGORIES:group03,group04,group05";
        assertEquals(expectedSummary, property.toContent());
        assertEquals(3, property.getValue().size());        
    }
    
    @Test
    public void canParseMultipleCategories2()
    {
        Categories property = Categories.parse("group03,group04,group05");
        String expectedSummary = "CATEGORIES:group03,group04,group05";
        assertEquals(expectedSummary, property.toContent());
        assertEquals(3, property.getValue().size());        
    }
    
    @Test
    public void canCopyCategories()
    {
        Categories property1 = Categories.parse("group03,group04,group05");
        Categories property2 = new Categories(property1);
        assertEquals(property1, property2);
        assertFalse(property1 == property2);
        assertFalse(property1.getValue() == property2.getValue());
    }
}
