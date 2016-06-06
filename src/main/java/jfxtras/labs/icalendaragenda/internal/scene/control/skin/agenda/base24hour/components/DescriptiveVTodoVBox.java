package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;

public class DescriptiveVTodoVBox extends DescriptiveLocatableVBox<VTodo>
{
    public DescriptiveVTodoVBox()
    {
        super();
        endLabel.setText( getResources().getString("due.time") );
    }
    
    @Override
    public void setupData(
//            Appointment appointment,
            VTodo vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {        
        // Convert duration to date/time end - this controller can't handle VEvents with duration
        if (vComponent.getDuration() != null)
        {
            Temporal end = vComponent.getDateTimeStart().getValue().plus(vComponent.getDuration().getValue());
            vComponent.setDuration((DurationProp) null);
            vComponent.setDateTimeDue(end);
        }
        
        super.setupData(vComponent, startRecurrence, endRecurrence, categories);
    }
}
