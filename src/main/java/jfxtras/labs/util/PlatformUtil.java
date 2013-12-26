package jfxtras.labs.util;

import javafx.application.Platform;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tbee on 26-12-13.
 */
public class PlatformUtil {

	/**
	 * Invokes a Runnable in JFX Thread and waits while it's finished. Like
	 * SwingUtilities.invokeAndWait does for EDT.
	 *
	 * @param runnable
	 *            The Runnable that has to be called on JFX thread.
	 */
	static public void runAndWait(final Runnable runnable) {
		try {
			FutureTask future = new FutureTask(runnable, null);
			Platform.runLater(future);
			future.get();
		}
		catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Invokes a Callable in JFX Thread and waits while it's finished. Similar to SwingUtilities.invokeAndWait.
	 *
	 * @param callable
	 *            The Runnable that has to be called on JFX thread.
	 * @return the result of callable.call();
	 */
	static public <V> V runAndWait(final Callable<V> callable) throws InterruptedException, ExecutionException {
		FutureTask<V> future = new FutureTask<>(callable);
		Platform.runLater(future);
		return future.get();
	}
}

