package jfxtras.labs.icalendaragenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendaragenda.agenda.VEventDisplayPopupTest;
import jfxtras.labs.icalendaragenda.editors.delete.DeleteAllTest;
import jfxtras.labs.icalendaragenda.editors.delete.DeleteOneTest;
import jfxtras.labs.icalendaragenda.editors.delete.DeleteThisAndFutureTest;
import jfxtras.labs.icalendaragenda.editors.revise.CancelRevisionTest;
import jfxtras.labs.icalendaragenda.editors.revise.ReviseAllTest;
import jfxtras.labs.icalendaragenda.editors.revise.ReviseOneTest;
import jfxtras.labs.icalendaragenda.editors.revise.ReviseThisAndFutureTest;
import jfxtras.labs.icalendaragenda.popup.RecurrenceExceptionsTests;
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
    CancelRevisionTest.class,
    RenderVEventsTest.class,
    VEventEditPopupTest.class,
    VJournalEditPopupTest.class,
    VTodoEditPopupTest.class,
    
    // revise tests
    ReviseAllTest.class,
    ReviseThisAndFutureTest.class,
    ReviseOneTest.class,
    
    // delete tests
    DeleteAllTest.class,
    DeleteThisAndFutureTest.class,
    DeleteOneTest.class,
    
    // edit popup tests
    RecurrenceExceptionsTests.class,
    
    // agenda tests
              })
public class AllTests {

}
