package jfxtras.labs.fxml;

//import java.util.ServiceLoader;

import java.lang.reflect.InvocationTargetException;
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
    	
    	// fall back to default
        return javaFXBuilderFactory.getBuilder(clazz);
    }
}