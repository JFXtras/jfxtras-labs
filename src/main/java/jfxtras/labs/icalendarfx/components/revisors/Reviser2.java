package jfxtras.labs.icalendarfx.components.revisors;

/**
 * Revise edited VComponent by template method pattern
 * 
 * @author David Bal
 *
 */
public interface Reviser2
{
    default void revise()
    {
        initialValidate();
        copyComponents();
        validateStartRecurrenceAndDTStart();
        displayChangeDialog();
        adjustDateTime();
        outputValidate();
        returnNewComponents();
    }

    void initialValidate();

    void returnNewComponents();

    void outputValidate();

    void adjustDateTime();

    void displayChangeDialog();

    void validateStartRecurrenceAndDTStart();

    void copyComponents();
    
}
