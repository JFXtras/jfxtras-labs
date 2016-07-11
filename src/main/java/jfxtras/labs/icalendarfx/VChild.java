package jfxtras.labs.icalendarfx;

public interface VChild extends VElement
{
    /** Returns the {@link VParent} of this {@link VChild } */
    VParent getParent();
    /** This method is invoked internally by the API to set the parent.
     * It should not be used by the client */
    void setParent(VParent parent);
}
