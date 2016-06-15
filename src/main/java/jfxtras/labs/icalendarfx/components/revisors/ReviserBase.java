package jfxtras.labs.icalendarfx.components.revisors;

import java.util.Collection;

public abstract class ReviserBase<T, U> implements Revisable
{
    @Override
    public abstract Collection<U> revise();
}
