package jfxtras.labs.icalendaragenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendaragenda.itip.ReviseWithRecurrencesTest;
import jfxtras.labs.icalendaragenda.itip.SimpleDeleteTest;
import jfxtras.labs.icalendaragenda.itip.SimpleReviseTest;

@RunWith(Suite.class)
@SuiteClasses({ 
    AgendaEditPopupTest.class,
    AgendaStringConverstionTest.class,
    ComponentChangeDialogTest.class,
    MakeAppointmentsTest.class,
    MakeNewVEventsTest.class,
    SimpleReviseTest.class,
    RenderVEventsTest.class,
    VEventEditPopupTest.class,
    VJournalEditPopupTest.class,
    VTodoEditPopupTest.class,
    
    // iTIP tests
    ReviseWithRecurrencesTest.class,
    SimpleDeleteTest.class,
    SimpleReviseTest.class
              })
public class AllTests {

}
