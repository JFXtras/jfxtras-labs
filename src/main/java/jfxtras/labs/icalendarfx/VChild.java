package jfxtras.labs.icalendarfx;

public interface VChild extends VElement
{
    /** Gets the {@link VParent} of this {@link VChild} */
    VParent getParent();
    /** Set the {@link VParent}  of this {@link VChild}.  This method is invoked internally by the API.
     * It should not be used externally */
    void setParent(VParent parent);
}
