package jfxtras.labs.fxml;

//import java.util.ServiceLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import jfxtras.fxml.BuilderService;

/**
 * An extended BuilderFactory that uses ServiceLoader to detect any builders on the classpath.
 * This means builders are discovered semi-automatically by this factory.
 * If none of the discovered builders match, the builder forwards the request to JavaFXBuilderFactory, so  this class can be used instead of.
 * 
 * In order to make automatically discovered builders:
 * 1. Implement the jfxtras.fxml.BuilderService interface instead of the javafx.util.Builder interface on all builder implementations.
 * 2. Create a file in your project / jar called "META-INF/services/jfxtras.fxml.BuilderService"
 * 3. In that file specify the full class name of all builders from 1 that you want to make auto-discoverable, each name on a name line.
 * 4. Use this builder instead of the default, for example like so: FXMLLoader.load(url, null, new JFXtrasBuilderFactory());
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
    public Builder<?> getBuilder(Class<?> clazz) {
    	
    	// try the ServiceLoader configured classes
    	for (BuilderService builderService : builderServiceLoader) {
    		if (builderService.isBuilderFor(clazz)) {
    			try
				{
					return builderService.getClass().getConstructor(new Class<?>[]{}).newInstance(new Object[]{});
				}
				catch (NoSuchMethodException e) { throw new RuntimeException(e); }
				catch (SecurityException e) { throw new RuntimeException(e); }
				catch (InstantiationException e) { throw new RuntimeException(e); }
				catch (IllegalAccessException e) { throw new RuntimeException(e); }
				catch (IllegalArgumentException e) { throw new RuntimeException(e); }
				catch (InvocationTargetException e) { throw new RuntimeException(e); }
    		}
        }
    	
    	// fall back to default factory
        return javaFXBuilderFactory.getBuilder(clazz);
    }
}