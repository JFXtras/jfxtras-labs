package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import javafx.scene.Scene;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.scene.control.agenda.Agenda;

public class EditComponentPopupScene extends Scene
{
    public EditComponentPopupScene(EditDisplayableTabPane<?,?> parent)
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
