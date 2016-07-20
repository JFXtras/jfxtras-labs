package jfxtras.labs.icalendaragenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
    AgendaEditPopupTest.class,
    AgendaStringConverstionTest.class,
    ComponentChangeDialogTest.class,
    MakeAppointmentsTest.class,
    RenderVEventsTest.class,
    VEventEditPopupTest.class,
    VJournalEditPopupTest.class,
    VTodoEditPopupTest.class,
              })
public class AllTests {

}
