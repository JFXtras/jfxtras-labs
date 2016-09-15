package jfxtras.labs.icalendaragenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendaragenda.itip.ReviseComponent;

@RunWith(Suite.class)
@SuiteClasses({ 
    AgendaEditPopupTest.class,
    AgendaStringConverstionTest.class,
    ComponentChangeDialogTest.class,
    MakeAppointmentsTest.class,
    MakeNewVEventsTest.class,
    ReviseComponent.class,
    RenderVEventsTest.class,
    VEventEditPopupTest.class,
    VJournalEditPopupTest.class,
    VTodoEditPopupTest.class,
              })
public class AllTests {

}
