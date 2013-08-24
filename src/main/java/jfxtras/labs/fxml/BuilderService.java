package jfxtras.labs.fxml;

import javafx.util.Builder;

public interface BuilderService 
{
	public boolean hasBuilderFor(Class clazz);
	public Builder createBuilder();
}
