package jfxtras.labs.icalendaragenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendaragenda.agenda.VEventDisplayPopupTest;
import jfxtras.labs.icalendaragenda.editors.ReviseWithRecurrencesTest;
import jfxtras.labs.icalendaragenda.editors.SimpleDeleteTest;
import jfxtras.labs.icalendaragenda.editors.SimpleReviseTest;
import jfxtras.labs.icalendaragenda.popup.VEventEditPopupTest;
import jfxtras.labs.icalendaragenda.popup.VJournalEditPopupTest;
import jfxtras.labs.icalendaragenda.popup.VTodoEditPopupTest;

@RunWith(Suite.class)
@SuiteClasses({ 
    VEventDisplayPopupTest.class,
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
