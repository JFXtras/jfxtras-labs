/**
 * AbstractJemmyTest.java
 *
 * Copyright (c) 2011-2013, JFXtras All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the organization nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs;

import java.awt.GraphicsEnvironment;
import org.jemmy.fx.AppExecutor;
import org.jemmy.fx.SceneDock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import javafx.application.Platform;
import javafx.scene.Node;

/**
 * Parent class for all Jemmy tests which allows us to run several test in a
 * complete build.
 *
 * @author Mario Schroeder
 *
 */
public abstract class AbstractJemmyTest {

    private Node testContent;
    private SceneDock sceneDock;

    @BeforeClass
    public static void setUpClass() throws Exception {
        if (!isHeadless()) {
            AppExecutor.executeNoBlock(JemmyTestApp.class);
        }
    }

    @Before
    public void setUp() {
        if (!isHeadless()) {
            sceneDock = new SceneDock();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    JemmyTestApp.addContent(getTestContent());
                }
            });
        }
    }

    @After
    public void shutDown() throws Exception {
        if (!isHeadless()) {
            Thread.sleep(1000);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    JemmyTestApp.removeContent(getTestContent());
                }
            });
        }
    }

    private Node getTestContent() {
        if (testContent == null) {
            testContent = createTestContent();
        }
        return testContent;
    }

    /**
     * Return the wrapper to interact with the scene.
     *
     * @return {@link SceneDock}
     */
    protected SceneDock getSceneDock() {
        return sceneDock;
    }

    /**
     * This method should create the actual content for test purpose.
     *
     * @return the node for testing
     */
    protected abstract Node createTestContent();

    protected static boolean isHeadless() {
        return true;//GraphicsEnvironment.isHeadless();
    }
}
