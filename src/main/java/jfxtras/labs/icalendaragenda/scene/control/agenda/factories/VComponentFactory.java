package jfxtras.labs.icalendaragenda.scene.control.agenda.factories;

import jfxtras.labs.icalendarfx.components.VComponentDisplayableBase;

public abstract class VComponentFactory<R>
{
    /** Create VComponent from a recurrence.  The recurrence is tested to determine which type of VComponent should
     * be created, such as VEVENT or VTODO
     * 
     * @param recurrence - recurrence as basis for VComponent
     * @return - new VComponent
     */
    abstract public VComponentDisplayableBase<?> createVComponent(R recurrence);
}
