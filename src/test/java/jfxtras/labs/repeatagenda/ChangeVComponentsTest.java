package jfxtras.labs.repeatagenda;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;

/**
 * Tests adding and removing VComponents outside the ICalendarAgenda implementation.
 * Inside ICalendarAgenda adding and removing VComponents is handled by removing
 * instances (Appointments) by Agenda through the popups.
 * 
 * The change vComponentsListener should only fire by changes made outside the implementation.
 *
 */
public class ChangeVComponentsTest extends ICalendarTestAbstract
{
    @Test
    public void canAddVComponent()
    {
        ICalendarAgenda agenda = new ICalendarAgenda();
        agenda.vComponents().add(getDaily1());
    }

}
