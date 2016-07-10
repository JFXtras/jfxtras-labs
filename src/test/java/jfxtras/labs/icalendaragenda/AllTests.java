package jfxtras.labs.icalendaragenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
    AgendaStringConverstionTests.class,
    VEventEditPopupTests.class,
    VJournalEditPopupTests.class,
    VTodoEditPopupTests.class,
              })
public class AllTests {

}
