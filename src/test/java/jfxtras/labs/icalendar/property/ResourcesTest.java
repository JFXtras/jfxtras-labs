package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.descriptive.Resources;

public class ResourcesTest
{
    @Test
    public void canParseResources()
    {
        String content = "RESOURCES:EASEL,PROJECTOR,VCR";
        Resources madeProperty = new Resources(content);
        assertEquals(content, madeProperty.toContentLine());
        Resources expectedProperty = new Resources("RESOURCES:EASEL,PROJECTOR,VCR");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(3, madeProperty.getValue().size());
    }
    
    @Test
    public void canParseResources2()
    {
        String content = "RESOURCES;ALTREP=\"http://xyzcorp.com/conf-rooms/f123.vcf\";LANGUAGE=fr:Nettoyeur haute pression";
        Resources madeProperty = new Resources(content);
        assertEquals(content, madeProperty.toContentLine());
        Resources expectedProperty = new Resources("Nettoyeur haute pression")
                .withAlternateText("http://xyzcorp.com/conf-rooms/f123.vcf")
                .withLanguage("fr");
        assertEquals(expectedProperty, madeProperty);
    }
}
