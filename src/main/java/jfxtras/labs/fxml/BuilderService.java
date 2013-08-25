package jfxtras.labs.fxml;

import javafx.util.Builder;

/**
 * The implementing class should also have a zero argument constructor.
 */
public interface BuilderService<T> extends Builder<T> 
{
	public boolean isBuilderFor(Class<?> clazz);
}
