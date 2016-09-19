package jfxtras.labs.icalendaragenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendaragenda.itip.ReviseWithRecurrencesWithiITIPTest;
import jfxtras.labs.icalendaragenda.itip.SimpleReviseWithITIPTest;

@RunWith(Suite.class)
@SuiteClasses({ 
    AgendaEditPopupTest.class,
    AgendaStringConverstionTest.class,
    ComponentChangeDialogTest.class,
    MakeAppointmentsTest.class,
    MakeNewVEventsTest.class,
    SimpleReviseWithITIPTest.class,
    RenderVEventsTest.class,
    VEventEditPopupTest.class,
    VJournalEditPopupTest.class,
    VTodoEditPopupTest.class,
    
    // iTIP tests
    ReviseWithRecurrencesWithiITIPTest.class,
    SimpleReviseWithITIPTest.class
              })
public class AllTests {

}
