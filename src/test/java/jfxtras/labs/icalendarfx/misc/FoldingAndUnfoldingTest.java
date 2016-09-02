package jfxtras.labs.icalendarfx.misc;

import org.junit.Test;

public class FoldingAndUnfoldingTest
{
    @Test
    public void canUnfoldLine()
    {
        String line = "COMMENT:Ek and Lorentzon said they would consider halting investment at th,eir headquarters in Stockholm. The pioneering music streaming company employs about 850 people in the city, and more than 1,000 in nearly 30 other offices around the world.";
//        VEvent builtComponent = new VEvent()
//                .withComments(line);
//        String componentName = builtComponent.name();
        String foldedContent = "BEGIN:X-TEST" + System.lineSeparator() +
                                 "COMMENT:Ek and Lorentzon said they would consider halting investment at th" + System.lineSeparator() +
                                 " \\,eir headquarters in Stockholm. The pioneering music streaming company em" + System.lineSeparator() +
                                 " ploys about 850 people in the city\\, and more than 1\\,000 in nearly 30 oth" + System.lineSeparator() +
                                 " er offices around the world." + System.lineSeparator() +
                                 "END:X-TEST";
//        Arrays.stream(foldedContent.split(System.lineSeparator()))
    }
}
