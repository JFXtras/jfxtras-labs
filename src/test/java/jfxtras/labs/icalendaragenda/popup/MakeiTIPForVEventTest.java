package jfxtras.labs.icalendaragenda.popup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jfxtras.labs.icalendaragenda.AgendaTestAbstract;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.CategorySelectionGridPane;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.editors.ChangeDialogOption;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.DefaultRecurrenceFactory;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.RecurrenceFactory;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.calendar.Version;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.TestUtil;

public class MakeiTIPForVEventTest extends VEventPopupTestBase
{
    @Test // simple press save
    public void canSaveWithNoEdit()
    {
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));   
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
        });
        
        // click save button (no changes so no dialog)
        click("#saveComponentButton");
        
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals("", iTIPMessage);
    }
    
    @Test
    public void canCancelEdit()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(2);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
        });

        // edit properties
        TextField summaryTextField = find("#summaryTextField");
        summaryTextField.setText("new summary");
        LocalDateTimeTextField startDateTimeTextField = find("#startDateTimeTextField");
        startDateTimeTextField.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 10, 30));

        // cancel changes
        click("#saveComponentButton");
        click("#changeDialogCancelButton");
        click("#cancelComponentButton");
        
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals("", iTIPMessage);
    }
    
    @Test
    public void canEditSimpleVEvent()
    {
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));   
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
        });

        // Make changes
        TextField summaryTextField = find("#summaryTextField");
        summaryTextField.setText("new summary");
        
        // Save changes
        click("#saveComponentButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        click("#changeDialogOkButton");
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T100000" + System.lineSeparator() +
                "DTEND:20151109T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:new summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canEditThisAndFuture()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(2);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
        });

       // edit property
       TextField summaryTextField = find("#summaryTextField");
       summaryTextField.setText("new summary");

       // save changes to THIS AND FUTURE
       click("#saveComponentButton");
       ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
       TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.THIS_AND_FUTURE));
       click("#changeDialogOkButton");

       String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
               .map(v -> v.toContent())
               .collect(Collectors.joining(System.lineSeparator()));
       String dtstamp = iTIPMessage.split(System.lineSeparator())[27];
       String expectedDTStamp = new DateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z"))).toContent();
       assertEquals(expectedDTStamp.substring(0, 16), dtstamp.substring(0, 16)); // check date, month and time
       String uid = iTIPMessage.split(System.lineSeparator())[28];
       assertFalse(uid.equals(vevent.getUniqueIdentifier().toContent()));
       String expectediTIPMessage =
               "BEGIN:VCALENDAR" + System.lineSeparator() +
               "METHOD:REQUEST" + System.lineSeparator() +
               "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
               "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
               "BEGIN:VEVENT" + System.lineSeparator() +
               "CATEGORIES:group05" + System.lineSeparator() +
               "DTSTART:20151109T100000" + System.lineSeparator() +
               "DTEND:20151109T110000" + System.lineSeparator() +
               "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
               "SUMMARY:Daily1 Summary" + System.lineSeparator() +
               "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
               "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
               "RRULE:FREQ=DAILY;UNTIL=20151110T180000Z" + System.lineSeparator() +
               "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
               "SEQUENCE:1" + System.lineSeparator() +
               "END:VEVENT" + System.lineSeparator() +
               "END:VCALENDAR" + System.lineSeparator() +
               "BEGIN:VCALENDAR" + System.lineSeparator() +
               "METHOD:PUBLISH" + System.lineSeparator() +
               "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
               "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
               "BEGIN:VEVENT" + System.lineSeparator() +
               "CATEGORIES:group05" + System.lineSeparator() +
               "DTSTART:20151111T100000" + System.lineSeparator() +
               "DTEND:20151111T110000" + System.lineSeparator() +
               "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
               "SUMMARY:new summary" + System.lineSeparator() +
               dtstamp + System.lineSeparator() +
               uid + System.lineSeparator() +
               "RRULE:FREQ=DAILY" + System.lineSeparator() +
               "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
               "RELATED-TO:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
               "END:VEVENT" + System.lineSeparator() +
               "END:VCALENDAR";
       assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canDeleteAll()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(2);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
        });

        // delete VComponent
        click("#deleteComponentButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        click("#changeDialogOkButton");

        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:CANCEL" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "STATUS:CANCELLED" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    // edit descriptive properties of a repeating event to make a special recurrence instance
    @Test
    public void canEditDescribableProperties()
    {
        RecurrenceFactory<Appointment> recurrenceFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        recurrenceFactory.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceFactory.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));        
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        List<Appointment> newAppointments = recurrenceFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
        });

        // Get properties
        LocalDateTimeTextField startDateTimeTextField = find("#startDateTimeTextField");
        TextField summaryTextField = find("#summaryTextField");
        TextArea descriptionTextArea = find("#descriptionTextArea");
        TextField locationTextField = find("#locationTextField");
        TextField categoryTextField = find("#categoryTextField");
        CategorySelectionGridPane categorySelectionGridPane = find("#categorySelectionGridPane");
        
        // Edit properties
        startDateTimeTextField.setLocalDateTime(LocalDateTime.of(2016, 5, 15, 8, 0));
        summaryTextField.setText("new summary");
        descriptionTextArea.setText("new description");
        locationTextField.setText("new location");
        TestUtil.runThenWaitForPaintPulse(() -> categorySelectionGridPane.setCategorySelected(11));
        categoryTextField.setText("new group name");
        
        // Save changes
        click("#saveComponentButton");        
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ONE));
        click("#changeDialogOkButton");
        
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        String dtstamp = iTIPMessage.split(System.lineSeparator())[10];
        String expectedDTStamp = new DateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z"))).toContent();
        assertEquals(expectedDTStamp.substring(0, 16), dtstamp.substring(0, 16)); // check date, month and time
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:new group name" + System.lineSeparator() +
                "DTSTART:20160515T080000" + System.lineSeparator() +
                "DTEND:20160515T090000" + System.lineSeparator() +
                "DESCRIPTION:new description" + System.lineSeparator() +
                "SUMMARY:new summary" + System.lineSeparator() +
                dtstamp + System.lineSeparator() + // need to match time exactly
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "LOCATION:new location" + System.lineSeparator() +
                "RECURRENCE-ID:20160515T100000" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
  
    @Test
    public void canChangeWholeDayToTimeBased()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getWholeDayDaily3();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(1);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
//                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
        });
        CheckBox wholeDayCheckBox = find("#wholeDayCheckBox");
        assertTrue(wholeDayCheckBox.isSelected());
        LocalDateTextField start1 = find("#startDateTextField");
        assertEquals(LocalDate.of(2015, 11, 11), start1.getLocalDate());
        click(wholeDayCheckBox); // turn off wholeDayCheckBox

        LocalDateTimeTextField start2 = find("#startDateTimeTextField");
        assertEquals(LocalDateTime.of(2015, 11, 11, 10, 0), start2.getLocalDateTime());
        start2.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 13, 0)); // adds 3 hour shift
        LocalDateTimeTextField end2 = find("#endDateTimeTextField");
        assertEquals(LocalDateTime.of(2015, 11, 12, 14, 0), end2.getLocalDateTime());
        end2.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 14, 0)); // make 1 hour long
        
        // Save changes
        click("#saveComponentButton");        
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        click("#changeDialogOkButton");
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "RRULE:UNTIL=20151123T210000Z;FREQ=DAILY;INTERVAL=3" + System.lineSeparator() +
                "DTSTART;TZID=America/Los_Angeles:20151108T130000" + System.lineSeparator() +
                "DTEND;TZID=America/Los_Angeles:20151108T140000" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test
    public void canChangeToWholeDayAll()
    {
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));   
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1()
                .withLocation("Here");
        myCalendar.getVEvents().add(vevent);
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
        });
        CheckBox wholeDayCheckBox = find("#wholeDayCheckBox");
        click(wholeDayCheckBox);

        LocalDateTextField start = find("#startDateTextField");
        assertEquals(LocalDate.of(2016, 5, 15), start.getLocalDate());
        start.setLocalDate(LocalDate.of(2016, 5, 16)); // adds 1 day shift
        
        // Save changes
        click("#saveComponentButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        click("#changeDialogOkButton");
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151110" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151111" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "LOCATION:Here" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
