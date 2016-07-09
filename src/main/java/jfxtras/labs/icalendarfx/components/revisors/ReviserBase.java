package jfxtras.labs.icalendarfx.components.revisors;

import java.util.Collection;

public abstract class ReviserBase<U> implements Reviser
{
    @Override
    public abstract Collection<U> revise();
}
