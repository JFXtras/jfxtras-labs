package jfxtras.labs.icalendaragenda.trial;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.layout.HBox;

/**
 * Simple Demo of {@link ICalendarAgenda} with one event
 * 
 * @author David Bal
 *
 */
public class ICalendarAgendaSimpleTrial2 extends Application
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
                .withOrganizer("mailto:david@balsoftware.net")
                .withUniqueIdentifier("exampleuid000jfxtras.org");
        vCalendar.addVComponent(vEvent);

        // setup control
        BorderPane root = new BorderPane();
        ICalendarAgenda agenda = new ICalendarAgenda(vCalendar);        
        root.setCenter(agenda);
        
        // weekly increase/decrease buttons
        Button increaseWeek = new Button(">");
        Button decreaseWeek = new Button("<");
        HBox buttonHBox = new HBox(decreaseWeek, increaseWeek);
        root.setTop(buttonHBox);
        
        // weekly increase/decrease functionality
        increaseWeek.setOnAction((e) ->
        {
            LocalDateTime newDisplayedLocalDateTime = agenda.getDisplayedLocalDateTime().plus(Period.ofWeeks(1));
            agenda.setDisplayedLocalDateTime(newDisplayedLocalDateTime);
        });        
        decreaseWeek.setOnAction((e) ->
        {
            LocalDateTime newDisplayedLocalDateTime = agenda.getDisplayedLocalDateTime().minus(Period.ofWeeks(1));
            agenda.setDisplayedLocalDateTime(newDisplayedLocalDateTime);
        });
        
        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ICalendar Agenda Simple Demo");
        primaryStage.show();
    }
}
