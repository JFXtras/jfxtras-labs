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
import jfxtras.labs.icalendaragenda.editors.revisor.ReviseOneTest;
import jfxtras.labs.icalendaragenda.editors.revisor.ReviseThisAndFutureTest;
import jfxtras.labs.icalendaragenda.misc.ComponentChangeDialogTest;
import jfxtras.labs.icalendaragenda.misc.MakeAppointmentsTest;
import jfxtras.labs.icalendaragenda.popup.PopupBindingsTest;
import jfxtras.labs.icalendaragenda.popup.PopupRecurrenceDescriptionTest;
import jfxtras.labs.icalendaragenda.popup.ExceptionDateTests;
import jfxtras.labs.icalendaragenda.popup.VEventMakeiTIPTest;
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
    ReviseThisAndFutureTest.class,
    ReviseOneTest.class,
    
    // delete tests
    DeleteAllTest.class,
    DeleteThisAndFutureTest.class,
    DeleteOneTest.class,
    
    // popup tests
    PopupBindingsTest.class,
    PopupRecurrenceDescriptionTest.class,
    ExceptionDateTests.class,
    VEventMakeiTIPTest.class,
    VJournalMakeiTIPTest.class,
    VTodoMakeiTIPTest.class,
    
    // agenda tests
    DeleteVEventTest.class,
    GraphicallyChangeTest.class,
    MakeNewVEventsTest.class,
    PopupRecurrenceDescriptionTest.class,
    RenderVEventsTest.class,
    RevisePopupTest.class,
    VEventDisplayPopupTest.class,
    
              })
public class AllTests {

}
