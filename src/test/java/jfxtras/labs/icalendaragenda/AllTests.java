package jfxtras.labs.icalendaragenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendaragenda.editors.deletor.DeleteThisAndFutureTest;

@RunWith(Suite.class)
@SuiteClasses({ 

    // misc tests
//    ComponentChangeDialogTest.class,
//    MakeAppointmentsTest.class,
    
    // revise tests
//    CancelRevisionTest.class,
//    ReviseAllTest.class,
//    ReviseThisAndFutureTest.class,
//    ReviseOneTest.class,
    
    // delete tests
//    DeleteAllTest.class,
    DeleteThisAndFutureTest.class,
//    DeleteOneTest.class,
    
    // popup tests
//    VEventMakeiTIPTest.class,
//    VJournalMakeiTIPTest.class,
//    VTodoMakeiTIPTest.class,
//    PopupBindingsTest.class,
//    PopupRecurrenceDescriptionTest.class,
//    RecurrenceExceptionsTests.class,
    
    // agenda tests
//    MakeNewVEventsTest.class,
//    PopupRecurrenceDescriptionTest.class,
//    RenderVEventsTest.class,
//    VEventDisplayPopupTest.class,
    
              })
public class AllTests {

}
