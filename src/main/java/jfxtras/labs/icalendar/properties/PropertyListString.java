package jfxtras.labs.icalendar.properties;

import java.util.List;
import java.util.stream.Collectors;

public interface PropertyListString extends Property<List<String>>
{
    @Override
    default String getValueForContentLine()
    {
        return getValue().stream().collect(Collectors.joining(","));
    }
}
