package jfxtras.labs.icalendaragenda.trial;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.labs.icalendaragenda.AgendaTestAbstract;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVEventTabPane;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Demo of edit VEvent popup
 * 
 * @author David Bal
 *
 */
public class EditComponentPopupTrial extends Application
{
    
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage primaryStage)
	{
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.icalendaragenda.ICalendarAgenda", Locale.getDefault());
        Settings.setup(resources);

        VEvent vevent = ICalendarStaticComponents.getDaily1();
//        VJournal vevent = new VJournal()
//                .withDateTimeStart("20160518T110000")
//                .withSummary("test journal")
//                .withDateTimeStamp("20160518T232502Z")
//                .withUniqueIdentifier("20160518T232502-0@jfxtras.org");
//        VTodo vevent = new VTodo()
//                .withDateTimeStart("20160518T110000")
//                .withDuration(Duration.ofHours(1))
//                .withSummary("test todo")
//                .withDateTimeStamp("20160518T232502Z")
//                .withUniqueIdentifier("20160518T232502-0@jfxtras.org");
        
        ObservableList<VEvent> vEvents = FXCollections.observableArrayList(vevent);
//        ObservableList<VJournal> vEvents = FXCollections.observableArrayList(vevent);
//        ObservableList<VTodo> vEvents = FXCollections.observableArrayList(vevent);
//        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(AgendaTestAbstract.MAKE_APPOINTMENT_TEST_CALLBACK_VJOURNAL);
        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(AgendaTestAbstract.MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE);
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);

        EditVEventTabPane popup = new EditVEventTabPane();
//        EditVJournalTabPane popup = new EditVJournalTabPane();
//        EditVTodoTabPane popup = new EditVTodoTabPane();
        popup.setupData(
                appointment,
                vevent,
                vEvents,
                ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS
                );

        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        popup.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
        Scene scene = new Scene(popup);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ICalendar Edit Popup Demo");
        primaryStage.show();
        
        popup.isFinished().addListener((obs, oldValue, newValue) -> 
        {
            System.out.println("hide:");
            primaryStage.hide();
        });
        vEvents.addListener((InvalidationListener) (obs) ->
        {
            System.out.println("VEVENTS:" + vEvents.size());
            vEvents.stream().forEach(System.out::println);
        });
    }
}
