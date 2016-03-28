package jfxtras.labs.icalendar.parameters;

import java.util.List;

public class Delegatees extends ParameterBase<Delegatees, List<String>>
{
    public Delegatees(String content)
    {
        super();
        List<String> value = myParameterEnum().parse(content);
        setValue(value);
    }

}
