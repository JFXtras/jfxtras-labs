package jfxtras.labs.icalendarfx.components.revisors;

import java.util.List;

public abstract class ReviserBase<U> implements Reviser
{
    @Override
    public abstract List<U> revise();
}
