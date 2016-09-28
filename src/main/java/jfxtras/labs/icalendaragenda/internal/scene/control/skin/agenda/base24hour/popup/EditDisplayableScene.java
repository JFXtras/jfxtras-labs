package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.popup;

import javafx.scene.Scene;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VDisplayable;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.scene.control.agenda.Agenda;

/** 
 * Base Scene for editing descriptive properties and a {@link RecurrenceRule} in a {@link VDisplayable}.
 * A {@link EditDisplayableTabPane} passed to the constructor is the root node of the scene graph<br>
 * 
 * @author David Bal
 */
public abstract class EditDisplayableScene extends Scene
{
    public EditDisplayableScene(EditDisplayableTabPane<?,?> parent)
    {
        super(parent);
        ICalendarAgenda.class.getResource(ICalendarAgenda.class.getSimpleName() + ".css").toExternalForm();
        getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET);
        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
    }

    public EditDisplayableTabPane<?,?> getEditDisplayableTabPane()
    {
        return (EditDisplayableTabPane<?, ?>) getRoot();
    }
}
