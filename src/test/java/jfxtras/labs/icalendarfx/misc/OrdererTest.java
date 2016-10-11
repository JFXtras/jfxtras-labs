package jfxtras.labs.icalendarfx.misc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import javafx.collections.FXCollections;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;

public class OrdererTest
{
    @Test
    public void canReplaceListElement()
    {
        VEvent builtComponent = new VEvent()
                .withCategories("category1")
                .withSummary("test");
        builtComponent.withCategories(FXCollections.observableArrayList(new Categories("category3"), new Categories("category4")));
        assertEquals(2, builtComponent.getCategories().size());
        String expectedContent = "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:category3" + System.lineSeparator() +
                "SUMMARY:test" + System.lineSeparator() +
                "CATEGORIES:category4" + System.lineSeparator() +
                "END:VEVENT";
        VEvent expectedVEvent = VEvent.parse(expectedContent);
        assertEquals(expectedVEvent, builtComponent);
    }

    @Test
    public void canReplaceListElement2()
    {
        VEvent v = new VEvent()
                .withCategories("d")
                .withCategories("new group name");
        System.out.println(v);
        System.out.println(v.getCategories());
throw new RuntimeException("do a test with attachments - make sure works for all list-based properties");
    }

}
