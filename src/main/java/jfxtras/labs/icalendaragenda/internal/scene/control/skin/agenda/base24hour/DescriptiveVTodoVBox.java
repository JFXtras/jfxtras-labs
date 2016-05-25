package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import jfxtras.labs.icalendarfx.components.VTodo;

public class DescriptiveVTodoVBox extends DescriptiveVBox<VTodo>
{
    public DescriptiveVTodoVBox()
    {
        super();
        getEndLabel().setText( getResources().getString("due.time") );
    }
}
