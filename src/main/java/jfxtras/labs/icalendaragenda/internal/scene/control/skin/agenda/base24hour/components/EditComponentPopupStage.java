package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.scene.control.agenda.Agenda;

public class EditComponentPopupStage<T extends VComponentDisplayable<?>> extends Stage
{
    private EditDisplayableTabPane<T,?> tabPane;
    public EditDisplayableTabPane<T,?> getEditDisplayableTabPane() { return tabPane; }   
    
    public EditComponentPopupStage(EditDisplayableTabPane<T,?> tabPane)
    {
        super();
        this.tabPane = tabPane;
        initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(getEditDisplayableTabPane());
        ICalendarAgenda.class.getResource(ICalendarAgenda.class.getSimpleName() + ".css").toExternalForm();
        scene.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET);
        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        scene.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
        setResizable(false);
        setScene(scene);
    }

//    public static <U extends VComponentDisplayable<?>> EditComponentPopupStage<U> editComponentPopupStageFactory(Class<U> clazz)
//    {
//        if (clazz.equals(VEvent.class))
//        {
//            return (EditComponentPopupStage<U>) new EditVEventPopupStage();
//        } else if (clazz.equals(VTodo.class))
//        {
//            return (EditComponentPopupStage<U>) new EditVTodoPopupStage();
//        } else if (clazz.equals(VJournal.class))
//        {
//            return (EditComponentPopupStage<U>) new EditVJournalPopupStage();
//        } else
//        {
//            throw new RuntimeException("Unsupported VComponent class:" + clazz);
//        }
//    }
    
//    public EditComponentPopupStage(
//            T vComponent,
//            List<T> vComponents,
//            Temporal startTemporal,
//            Temporal endTemporal,
//            List<String> categories)
//    {
//        // TODO Auto-generated constructor stub
//    }

    @Deprecated
    public static <U extends VComponentDisplayable<?>> EditComponentPopupStage<U> editComponentPopupStageFactory(
            U vComponent,
            VCalendar vCalendar,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
//            U vComponent, VCalendar vCalendar)
    {
        if (vComponent instanceof VEvent)
        {
            return (EditComponentPopupStage<U>) new EditVEventPopupStage(
                    (VEvent) vComponent,
                    vCalendar.getVEvents(),
                    startRecurrence,
                    endRecurrence,
                    categories);
        } else if (vComponent instanceof VTodo)
        {
            return (EditComponentPopupStage<U>) new EditVTodoPopupStage(
                    (VTodo) vComponent,
                    vCalendar.getVTodos(),
                    startRecurrence,
                    endRecurrence,
                    categories);
        } else if (vComponent instanceof VJournal)
        {
            return (EditComponentPopupStage<U>) new EditVJournalPopupStage(
                    (VJournal) vComponent,
                    vCalendar.getVJournals(),
                    startRecurrence,
                    endRecurrence,
                    categories);
        } else
        {
            throw new RuntimeException("Unsupported VComponent class:" + vComponent.getClass());
        }
    }
}
