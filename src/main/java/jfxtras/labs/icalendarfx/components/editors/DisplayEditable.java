package jfxtras.labs.icalendarfx.components.editors;

import java.time.temporal.Temporal;
import java.util.Map;

import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;

/**
 * 
 * @author david
 *
 * @param <T> - subclass
 * @param <U> - VComponent class
 * @param <V> - Temporal class of recurrence start and end
 */
public interface DisplayEditable<T, U extends VComponentDisplayable<U>> extends Editable<T, U>
{
    U getVComponentEdited();
    void setVComponentEdited(U vComponentEdited);
    default T withVComponentEdited(U vComponentEdited) { setVComponentEdited(vComponentEdited); return (T) this; }
    
    U getVComponentOriginal();
    void setVComponentOriginal(U vComponentOriginal);
    default T withVComponentOriginal(U vComponentOriginal) { setVComponentOriginal(vComponentOriginal); return (T) this; }

    Temporal getStartOriginalRecurrence();
    void setStartOriginalRecurrence(Temporal startOriginalRecurrence);
    default T withStartOriginalRecurrence(Temporal startOriginalRecurrence) { setStartRecurrence(startOriginalRecurrence); return (T) this; }
    
    Temporal getStartRecurrence();
    void setStartRecurrence(Temporal startRecurrence);
    default T withStartRecurrence(Temporal startRecurrence) { setStartRecurrence(startRecurrence); return (T) this; }
    
    Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> getDialogCallback();
    void setDialogCallback(Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> dialogCallback);
    default T withDialogCallback(Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> dialogCallback)
    {
        setDialogCallback(dialogCallback);
        return (T) this;
    }
}
