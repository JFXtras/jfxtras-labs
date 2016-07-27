package jfxtras.labs.icalendaragenda.trial;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;

/**
 * Simple Demo of {@link ICalendarAgenda}
 * 
 * @author David Bal
 *
 */
public class ICalendarAgendaSimpleTrial extends Application
{        
    public static void main(String[] args) {
        launch(args);       
    }

    @Override
    public void start(Stage primaryStage) {
        VCalendar vCalendar = new VCalendar();
        VEvent vEvent = new VEvent()
                .withDateTimeStart(LocalDateTime.now().minusMonths(1))
                .withDateTimeEnd(LocalDateTime.now().minusMonths(1).plusHours(1))
                .withDateTimeStamp(LocalDateTime.now().atZone(ZoneId.of("Z")))
                .withSummary("Example Daily Event")
                .withRecurrenceRule("RRULE:FREQ=DAILY")
                .withUniqueIdentifier("exampleuid000jfxtras.org");
        vCalendar.addVComponent(vEvent);
        ICalendarAgenda agenda = new ICalendarAgenda(vCalendar);
        
        BorderPane root = new BorderPane();
        root.setCenter(agenda);
        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ICalendar Agenda Simple Demo");
        primaryStage.show();
    }
}
