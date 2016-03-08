package jfxtras.labs.icalendaragenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.temporal.ChronoUnit;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar.ButtonData;
import jfxtras.labs.icalendar.DateTimeUtilities;
import jfxtras.labs.icalendar.VComponent;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;

public class AgendaRenderAppointmentsTest extends AgendaTestAbstract
{   
    @Override
    public Parent getRootNode()
    {
        return super.getRootNode();
    }
    
    @Test
    public void renderAppointments()
    {
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.appointments().removeListener(agenda.getAppointmentsChangeListener());
            agenda.setAppointmentsChangeListener(null);
            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
                    .withStartLocalDateTime(LocalDateTime.of(2015, 11, 11, 10, 0))
                    .withEndLocalDateTime(LocalDateTime.of(2015, 11, 11, 12, 0))
                    .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(1)));
            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(LocalDateTime.of(2015, 11, 11, 10, 0))
                    .withEndTemporal(LocalDateTime.of(2015, 11, 11, 12, 0))
                    .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(1)));
            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(LocalDate.of(2015, 11, 11))
                    .withEndTemporal(LocalDate.of(2015, 11, 12))
                    .withWholeDay(true)
                    .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(1)));
            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.systemDefault()))
                    .withEndTemporal(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 12, 0), ZoneId.systemDefault()))
                    .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(1)));
            HijrahDate h = HijrahDate.from(LocalDate.of(2015, 11, 13));
            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(h)
                    .withEndTemporal(h.plus(2, ChronoUnit.DAYS))
                    .withWholeDay(true)
                    .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(1)));
        });

        Node n = find("#AppointmentRegularBodyPane2015-11-11/0");
        new AssertNode(n).assertXYWH(5.5, 402.5, 69.0, 81.0, 0.01);
        Node n2 = find("#AppointmentRegularBodyPane2015-11-11/1");
        new AssertNode(n2).assertXYWH(44.5, 402.5, 69.0, 81.0, 0.01);
        Node n3 = find("#AppointmentRegularBodyPane2015-11-11/2");
        new AssertNode(n3).assertXYWH(84.5, 402.5, 40.0, 81.0, 0.01);
        Node n4 = find("#AppointmentWholedayBodyPane2015-11-11/0");
        Node n5 = find("#AppointmentWholedayBodyPane2015-11-13/0");
//        AssertNode.generateSource("n4", n4, null, false, jfxtras.test.AssertNode.A.XYWH);
//        new AssertNode(n4).assertXYWH(0.5, 0.0, 5.0, 966.375, 0.01); // JDWP exit error JVMTI_ERROR_WRONG_PHASE(112): on getting class status [util.c:1285]
        
        // OS check not needed after Robo font - I think
//        AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
//        String os = System.getProperty("os.name");
//        if (os.equals("Linux"))
//        {
//            new AssertNode(n).assertXYWH(0.5, 402.5, 69.0, 81.0, 0.01);
//        } else
//        {
//            new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
//        }
    }
    
//    /**
//     * 
//     */
//    @Test
//    public void renderRegularAppointmentZoned()
//    {
//        TestUtil.runThenWaitForPaintPulse( () -> {
//            agenda.appointments().removeListener(agenda.getAppointmentsChangeListener());
//            agenda.setAppointmentsChangeListener(null);
//            agenda.setDisplayedLocalDateTime(LocalDate.of(2014, 1, 1).atStartOfDay());
//            
//            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
//                .withStartTemporal(ZonedDateTime.of(LocalDateTime.of(2014, 1, 1, 10, 0), ZoneId.systemDefault()))
//                .withEndTemporal(ZonedDateTime.of(LocalDateTime.of(2014, 1, 1, 12, 0), ZoneId.systemDefault()))
//                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(0))
//            );
//        });
//
//        Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
//        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
//        new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
//        //TestUtil.sleep(3000);
//    }
//    
//    @Test
//    public void renderWholeDayAppointmentLocalDate()
//    {
//        TestUtil.runThenWaitForPaintPulse( () -> {
//            agenda.appointments().removeListener(agenda.getAppointmentsChangeListener());
//            agenda.setAppointmentsChangeListener(null);
//            agenda.setDisplayedLocalDateTime(LocalDate.of(2014, 1, 1).atStartOfDay());
//            
//            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
//                .withStartTemporal(LocalDate.of(2014, 1, 1))
//                .withEndTemporal(LocalDate.of(2014, 1, 2))
//                .withWholeDay(true)
//                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(1))
//            );
//        });
//
//        Node n = find("#AppointmentWholedayBodyPane2014-01-01/0");
//        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
//        new AssertNode(n).assertXYWH(0.5, 0.0, 5.0, 966.375, 0.01);
//        //TestUtil.sleep(3000);
//    }
//    
//    @Test
//    public void renderWholeDayAppointmentHijrahDate()
//    {
//        TestUtil.runThenWaitForPaintPulse( () -> {
//            agenda.appointments().removeListener(agenda.getAppointmentsChangeListener());
//            agenda.setAppointmentsChangeListener(null);
//            agenda.setDisplayedLocalDateTime(LocalDate.of(2014, 1, 1).atStartOfDay());
//            
//            Temporal h = HijrahDate.from(LocalDate.of(2014, 1, 1));
//            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
//                .withStartTemporal(h)
//                .withEndTemporal(h.plus(1, ChronoUnit.DAYS))
//                .withWholeDay(true)
//                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(1))
//            );
//        });
//
//        Node n = find("#AppointmentWholedayBodyPane2014-01-01/0");
//        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
//        new AssertNode(n).assertXYWH(0.5, 0.0, 5.0, 966.375, 0.01);
//        //TestUtil.sleep(3000);
//    }
//    
//    @Test
//    public void renderRegularAppointmentOffset()
//    {
//        TestUtil.runThenWaitForPaintPulse( () -> {
//            agenda.appointments().removeListener(agenda.getAppointmentsChangeListener());
//            agenda.setAppointmentsChangeListener(null);
//            agenda.setDisplayedLocalDateTime(LocalDate.of(2014, 1, 1).atStartOfDay());
//            
//            ZoneOffset offset = ZonedDateTime.now().getOffset();
//            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
//                .withStartTemporal(OffsetDateTime.of(LocalDateTime.of(2014, 1, 1, 10, 0), offset))
//                .withEndTemporal(OffsetDateTime.of(LocalDateTime.of(2014, 1, 1, 12, 0), offset))
//                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(0))
//            );
//        });
//
//        Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
//        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
//        new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
//        //TestUtil.sleep(3000);
//    }
//    
//    @Test
//    public void renderRegularAppointmentInstant()
//    {
//        TestUtil.runThenWaitForPaintPulse( () -> {
//            agenda.appointments().removeListener(agenda.getAppointmentsChangeListener());
//            agenda.setAppointmentsChangeListener(null);
//            agenda.setDisplayedLocalDateTime(LocalDate.of(2014, 1, 1).atStartOfDay());
//            
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
//            String dateInString = "2014-1-1 10:00:00";
//            Date date = null;
//            try {
//                date = sdf.parse(dateInString);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Instant instant = date.toInstant();
//            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
//                .withStartTemporal(instant)
//                .withEndTemporal(instant.plus(2, ChronoUnit.HOURS))
//                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(0))
//            );
//        });
//
//        Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
//        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
//        new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
//        //TestUtil.sleep(3000);
//    }
    
    @Test
    public void renderRegularAppointment2()
    {
        agenda.setNewAppointmentDrawnCallback((a) -> ButtonData.OK_DONE); // remove dialog, just return OK
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
                .withStartTemporal(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Japan")))
                .withEndTemporal(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 12, 0), ZoneId.of("Japan")))
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(0))
            );
        });

        int dayOfMonth = ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Japan"))
                .withZoneSameInstant(ZoneId.systemDefault())
                .toLocalDate()
                .getDayOfMonth();
        find("#AppointmentRegularBodyPane2015-11-" + dayOfMonth + "/0");
        
        Appointment a = agenda.appointments().get(0);
        System.out.println(a.getStartTemporal() + " " + a.getEndTemporal());
        // TODO - somewhere - not in Agenda - AUTOMATICALLY CONVERTS TO DEFAULT TIME ZONE
        
        Assert.assertEquals(1, agenda.appointments().size());
        VComponent<Appointment> v = agenda.vComponents().get(0);
        System.out.println(v.getDateTimeStart() + " ");
        String dateTimeStamp = DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(v.getDateTimeStamp());
        String dateTimeCreated = DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(v.getDateTimeCreated());
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                + "CATEGORIES:group00" + System.lineSeparator()
                + "CREATED:" + dateTimeCreated + System.lineSeparator()
                + "DTEND;TZID=Japan:20151111T120000" + System.lineSeparator()
                + "DTSTAMP:" + dateTimeStamp + System.lineSeparator()
                + "DTSTART;TZID=Japan:20151111T100000" + System.lineSeparator()
                + "UID:20151108T000000-0jfxtras.org" + System.lineSeparator()
                + "END:VEVENT";
        Assert.assertEquals(expectedString, v.toComponentText());
    }  

}
