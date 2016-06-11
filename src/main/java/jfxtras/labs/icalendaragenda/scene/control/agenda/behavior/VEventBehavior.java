package jfxtras.labs.icalendaragenda.scene.control.agenda.behavior;

import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditComponentPopupStage;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVEventTabPane;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class VEventBehavior extends DisplayableBehavior<VEvent>
{
    public VEventBehavior(ICalendarAgenda agenda)
    {
        super(agenda);
    }

    @Override
    public EditComponentPopupStage<VEvent> editPopup(Appointment appointment)
    {
        EditVEventTabPane tabPane = new EditVEventTabPane();
        VEvent vComponent = (VEvent) agenda.appointmentVComponentMap.get(System.identityHashCode(appointment));
        if (vComponent == null)
        {
            // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
            // Throwing an exception will leave the mouse unresponsive.
            System.out.println("ERROR: no component found - popup can'b be displayed");
            return null;
        } else
        {
            tabPane.setupData(
                    vComponent,
                    agenda.getVCalendar().getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    agenda.getCategories());
            return new EditComponentPopupStage<VEvent>(tabPane);
        }
    }
}
