package jfxtras.labs.icalendarfx.components;

public abstract class VTodo<T, I> extends VComponentLocatableBase<T, I> implements VTodoInt<T,I>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VTODO;
    }
    
    /*
     * CONSTRUCTORS
     */
    public VTodo() { }
    
    public VTodo(String contentLines)
    {
        super(contentLines);
    }
}
