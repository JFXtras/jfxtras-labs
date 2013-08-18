package jfxtras.labs.map;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

/**
 * Builder factory for the {@link MapPane}
 * @author Mario Schroeder
 *
 */
public class MapBuilderFactory implements BuilderFactory {
	
	private JavaFXBuilderFactory defaultBuilderFactory = new JavaFXBuilderFactory();
	
	private MapBuilder builder = new MapBuilder();

	@Override
	public Builder<?> getBuilder(Class<?> type) {
		if(type == MapPane.class){
			return builder;
		}else{
			return defaultBuilderFactory.getBuilder(type);
		}
	}

	
}
