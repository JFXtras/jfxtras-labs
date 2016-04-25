package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VJournal;

public class VJournalTest
{
    @Test
    public void canBuildVJournal()
    {
        VJournal builtComponent = new VJournal()
                .withDescriptions("DESCRIPTION:description 1")
                .withDescriptions("description 2", "DESCRIPTION:description 3");
        
        String componentName = builtComponent.componentType().toString();
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "DESCRIPTION:description 1" + System.lineSeparator() +
                "DESCRIPTION:description 2" + System.lineSeparator() +
                "DESCRIPTION:description 3" + System.lineSeparator() +
                "END:" + componentName;
                
        VJournal madeComponent = new VJournal(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContentLines());
    }
}
