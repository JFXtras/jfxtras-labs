package jfxtras.labs.icalendaragenda.popup;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.editors.EditVEventTabPane;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.test.JFXtrasGuiTest;

/**
 * Tests the edit popup without an Agenda instance.
 * 
 * @author David Bal
 *
 */
public class VEventPopupTestBase extends JFXtrasGuiTest
{
    public EditVEventTabPane getEditComponentPopup()
    {
        return editComponentPopup;
    }
    public void setEditComponentPopup(EditVEventTabPane editComponentPopup)
    {
        this.editComponentPopup = editComponentPopup;
    }
    private EditVEventTabPane editComponentPopup;
    
    @Override
    public Parent getRootNode()
    {
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.icalendaragenda.ICalendarAgenda", Locale.getDefault());
        Settings.setup(resources);
        setEditComponentPopup(new EditVEventTabPane());
        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        getEditComponentPopup().getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
        return getEditComponentPopup();
    }
}
