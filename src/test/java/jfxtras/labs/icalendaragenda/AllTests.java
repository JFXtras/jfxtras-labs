package jfxtras.labs.icalendaragenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
    AgendaStringConverstionTests.class,
    VEventPopupTests.class,
    VJournalPopupTests.class,
    VTodoPopupTests.class,
              })
public class AllTests {

}
