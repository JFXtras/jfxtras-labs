package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import jfxtras.labs.icalendarfx.components.VTodo;

public class EditVTodoTabPane extends EditDisplayableTabPane<VTodo>
{
    public EditVTodoTabPane( )
    {
        super();
        setDescriptiveVBox(new DescriptiveVTodoVBox());
        getDescriptiveAnchorPane().getChildren().add(getDescriptiveVBox());
        isFinished.bind(getDescriptiveVBox().isFinished);
    }
}