package jfxtras.labs.icalendaragenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendaragenda.agenda.DeleteVEventTest;
import jfxtras.labs.icalendaragenda.agenda.GraphicallyChangeTest;
import jfxtras.labs.icalendaragenda.agenda.MakeNewVEventsTest;
import jfxtras.labs.icalendaragenda.agenda.RenderVEventsTest;
import jfxtras.labs.icalendaragenda.agenda.RevisePopupTest;
import jfxtras.labs.icalendaragenda.agenda.VEventDisplayPopupTest;
import jfxtras.labs.icalendaragenda.editors.deletor.DeleteAllTest;
import jfxtras.labs.icalendaragenda.editors.deletor.DeleteOneTest;
import jfxtras.labs.icalendaragenda.editors.deletor.DeleteThisAndFutureTest;
import jfxtras.labs.icalendaragenda.editors.revisor.CancelRevisionTest;
import jfxtras.labs.icalendaragenda.editors.revisor.ReviseAllTest;
import jfxtras.labs.icalendaragenda.editors.revisor.ReviseNonRepeatingTest;
import jfxtras.labs.icalendaragenda.editors.revisor.ReviseOneTest;
import jfxtras.labs.icalendaragenda.editors.revisor.ReviseThisAndFutureTest;
import jfxtras.labs.icalendaragenda.misc.ComponentChangeDialogTest;
import jfxtras.labs.icalendaragenda.misc.MakeAppointmentsTest;
import jfxtras.labs.icalendaragenda.popup.ChangeDialogOptionsTest;
import jfxtras.labs.icalendaragenda.popup.ExceptionDateTests;
import jfxtras.labs.icalendaragenda.popup.MiscPopupTest;
import jfxtras.labs.icalendaragenda.popup.PopupDeleteAllTest;
import jfxtras.labs.icalendaragenda.popup.PopupReviseAllTest;
import jfxtras.labs.icalendaragenda.popup.PopupReviseOneTest;
import jfxtras.labs.icalendaragenda.popup.PopupReviseThisAndFutureTest;
import jfxtras.labs.icalendaragenda.popup.RecurrenceRuleDescriptionTest;
import jfxtras.labs.icalendaragenda.popup.VJournalMakeiTIPTest;
import jfxtras.labs.icalendaragenda.popup.VTodoMakeiTIPTest;

@RunWith(Suite.class)
@SuiteClasses({ 

    // misc tests
    ComponentChangeDialogTest.class,
    MakeAppointmentsTest.class,
    
    // revise tests
    CancelRevisionTest.class,
    ReviseAllTest.class,
    ReviseNonRepeatingTest.class,
    ReviseThisAndFutureTest.class,
    ReviseOneTest.class,
    
    // delete tests
    DeleteAllTest.class,
    DeleteThisAndFutureTest.class,
    DeleteOneTest.class,
    
    // popup tests
    ChangeDialogOptionsTest.class,
    ExceptionDateTests.class,
    MiscPopupTest.class,
    PopupDeleteAllTest.class,
    PopupReviseAllTest.class,
    PopupReviseThisAndFutureTest.class,
    PopupReviseOneTest.class,
    RecurrenceRuleDescriptionTest.class,
    VJournalMakeiTIPTest.class,
    VTodoMakeiTIPTest.class,
    
    // agenda tests
    DeleteVEventTest.class,
    GraphicallyChangeTest.class,
    MakeNewVEventsTest.class,
    RenderVEventsTest.class,
    RevisePopupTest.class,
    VEventDisplayPopupTest.class,
    
              })
public class AllTests {

}
