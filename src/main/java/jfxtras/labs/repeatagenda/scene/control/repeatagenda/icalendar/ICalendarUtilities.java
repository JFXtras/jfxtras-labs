package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.Pair;

public final class ICalendarUtilities
{
    private ICalendarUtilities() { };
    
    /*
     * Returns list of property name and property value list wrapped in a Pair.  DTSTART
     * property is put on top if present.
     */
    public static List<Pair<String,String>> ComponentStringToPropertyNameAndValueList(String string)
    {
        Comparator<? super Pair<String, String>> comparator = (p1, p2) ->
        {
            String propertyName1 = p1.getKey();
            return (propertyName1.equals("DTSTART")) ? -1 : 1;
        };
        return Arrays
            .stream(string.split(System.lineSeparator()))
            .map(line -> {
                int propertyValueSeparatorIndex = 0;
                for (int i=0; i<line.length(); i++)
                {
                    if ((line.charAt(i) == ';') || (line.charAt(i) == ':'))
                    {
                        propertyValueSeparatorIndex = i;
                        break;
                    }
                }
                if (propertyValueSeparatorIndex == 0)
                {
                    return null; // line doesn't contain a property, skip
                }
                String propertyName = line.substring(0, propertyValueSeparatorIndex);
                String value = line.substring(propertyValueSeparatorIndex + 1).trim();
                if (value.isEmpty())
                { // skip empty properties
                    return null;
                }
                return new Pair<String,String>(propertyName, value);
            })
            .filter(p -> p != null)
            .sorted(comparator)
            .collect(Collectors.toList());
    }
}
