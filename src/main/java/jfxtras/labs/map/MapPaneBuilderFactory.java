package jfxtras.labs.map;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

/**
 * Builder factory for the {@link MapPane}
 * @author Mario Schroeder
 *
 */
public class MapPaneBuilderFactory implements BuilderFactory {
	
	private JavaFXBuilderFactory defaultBuilderFactory = new JavaFXBuilderFactory();
	
	private MapPaneBuilder builder = new MapPaneBuilder();

	@Override
	public Builder<?> getBuilder(Class<?> type) {
		if(type == MapPane.class){
			return builder;
		}else{
			return defaultBuilderFactory.getBuilder(type);
		}
	}

	
}
