package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.descriptive.Categories;

public class CategoriesTest
{
    @Test
    public void canParseCategories()
    {
        Categories property = new Categories("group03");
        String expectedSummary = "CATEGORIES:group03";
        assertEquals(expectedSummary, property.toContentLine());
    }
    
    @Test
    public void canParseMultipleCategories()
    {
        Categories property = new Categories("CATEGORIES:group03,group04,group05");
        String expectedSummary = "CATEGORIES:group03,group04,group05";
        assertEquals(expectedSummary, property.toContentLine());
        assertEquals(3, property.getValue().size());
        
    }
}
