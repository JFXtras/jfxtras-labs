package jfxtras.labs.icalendarfx.method;

import jfxtras.labs.icalendarfx.properties.calendar.Method.MethodType;

public abstract class AbstractMethodProcessFactory
{
    public abstract Processable getMethodProcess(MethodType methodType);
}
