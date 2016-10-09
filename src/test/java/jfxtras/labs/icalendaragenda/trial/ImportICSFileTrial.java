package jfxtras.labs.icalendaragenda.trial;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.scene.layout.HBox;

/**
 * Demo that allows importing an ics file
 * 
 * @author David Bal
 *
 */
public class ImportICSFileTrial extends Application
{        
    public static void main(String[] args) {
        launch(args);       
    }

    @Override
    public void start(Stage primaryStage) {
        VCalendar mainVCalendar = new VCalendar();

        // setup control
        BorderPane root = new BorderPane();
        ICalendarAgenda agenda = new ICalendarAgenda(mainVCalendar);        
        root.setCenter(agenda);
        
        // Buttons
        Button increaseWeek = new Button(">");
        Button decreaseWeek = new Button("<");
        Button openButton = new Button("Import an ics file");
        HBox space = new HBox();
        space.setPadding(new Insets(0,10,0,10));
        HBox buttonHBox = new HBox(decreaseWeek, increaseWeek, space, openButton);
        root.setTop(buttonHBox);
        
        // weekly increase/decrease functionality
        increaseWeek.setOnAction(e ->
        {
            LocalDateTime newDisplayedLocalDateTime = agenda.getDisplayedLocalDateTime().plus(Period.ofWeeks(1));
            agenda.setDisplayedLocalDateTime(newDisplayedLocalDateTime);
        });        
        decreaseWeek.setOnAction(e ->
        {
            LocalDateTime newDisplayedLocalDateTime = agenda.getDisplayedLocalDateTime().minus(Period.ofWeeks(1));
            agenda.setDisplayedLocalDateTime(newDisplayedLocalDateTime);
        });
        
        // file chooser functionality
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("ics files", "*.ics"));
        openButton.setOnAction(e ->
        {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null && file.toString().lastIndexOf("ics") > 0)
            {
                try
                {
                    // process iTIP and log exceptions
                    final List<String> log = new ArrayList<>();
                    VCalendar iTIPMessage = VCalendar.parseICalendarFile(file.toPath());
                    Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> log.add(exception.getMessage()));
                    List<String> messageLog = mainVCalendar.processITIPMessage(iTIPMessage);
                    log.addAll(messageLog);
                    log.forEach(System.out::println);
                } catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            } else
            {
                throw new IllegalArgumentException("Invalid file:" + file + ". Select a valid ics file.");
            }
        });
        
        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ICalendar Agenda Simple Demo");
        primaryStage.show();
    }
}