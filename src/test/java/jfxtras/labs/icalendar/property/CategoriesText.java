package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.descriptive.Categories;

public class CategoriesText
{
    @Test
    public void canParseCategories()
    {
        Categories madeSummary = new Categories("group03");
        String expectedSummary = "CATEGORIES:group03";
        assertEquals(expectedSummary, madeSummary.toContentLine());
    }
}
