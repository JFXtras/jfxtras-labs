package jfxtras.labs.fxml;

//import java.util.ServiceLoader;

import java.util.ServiceLoader;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

/**
 * An extended BuilderFactory that uses ServiceLoader to detect any builders on the classpath.
 * 
 * @author Tom Eugelink
 *
 */
public class JFXtrasBuilderFactory implements BuilderFactory {
	
	// support Java META-INF services
	private static ServiceLoader<BuilderService> builderServiceLoader = ServiceLoader.load(BuilderService.class);

	/**
	 * 
	 */
    public JFXtrasBuilderFactory() {
    	javaFXBuilderFactory = new JavaFXBuilderFactory();
    }
    final private BuilderFactory javaFXBuilderFactory;

    /**
     * 
     */
    @Override
    public Builder<?> getBuilder(Class<?> aClass) {
    	
    	// try the ServiceLoader configured classes
    	for (BuilderService builderService : builderServiceLoader) {
    		if (builderService.hasBuilderFor(aClass)) {
    			return builderService.createBuilder();
    		}
        }
    	
    	// fall back to default
        return javaFXBuilderFactory.getBuilder(aClass);
    }
}