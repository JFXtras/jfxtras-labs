package jfxtras.labs;

import java.util.concurrent.CountDownLatch;

import javafx.embed.swing.JFXPanel;

import javax.swing.SwingUtilities;

import org.junit.BeforeClass;

public class JavaFXPlatformAbstractTest {
    @BeforeClass public static final void initJavaFXPlatform() {
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JFXPanel(); // initializes JavaFX environment
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
    }
}
