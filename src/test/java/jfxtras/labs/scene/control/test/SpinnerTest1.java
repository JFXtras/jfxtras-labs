/*
 * Copyright (c) 2009, 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package jfxtras.labs.scene.control.test;

import javafx.scene.Node;
import javafx.scene.control.Button;
import jfxtras.labs.scene.control.Spinner;
import jfxtras.labs.scene.control.test.jemmy.SpinnerWrap;

import org.jemmy.control.Wrap;
import org.jemmy.fx.AppExecutor;
import org.jemmy.fx.ByStyleClass;
import org.jemmy.fx.Root;
import org.jemmy.lookup.Lookup;
import org.jemmy.timing.State;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author shura
 */
public class SpinnerTest1 {

    public SpinnerTest1() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        AppExecutor.executeNoBlock(SpinnerTest1App.class);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void select() throws InterruptedException {
    	
    	// get the control
    	org.jemmy.interfaces.Parent<Node> lParent = Root.ROOT.lookup().as(org.jemmy.interfaces.Parent.class, Node.class);
        Wrap<? extends Spinner> lControlWrapper = lParent.lookup(Spinner.class).wrap();
        // hack: the lookup only knows controls and their wrappers that are in JemmyFX; this lines creates the unknown SpinnerWrapper by ripping data from the ControlWrapper 
        final SpinnerWrap<Spinner<String>> lSpinnerWrapper = new SpinnerWrap<Spinner<String>>(lControlWrapper.getEnvironment(), lControlWrapper.getControl());
        Wrap lDecArrowWrapper = lSpinnerWrapper.as(org.jemmy.interfaces.Parent.class, Node.class).lookup(new ByStyleClass<Node>("left-arrow")).wrap();
        Wrap lIncArrowWrapper = lSpinnerWrapper.as(org.jemmy.interfaces.Parent.class, Node.class).lookup(new ByStyleClass<Node>("right-arrow")).wrap();
        
        // check to see what the current value is
        Assert.assertEquals("a", lSpinnerWrapper.getControl().getValue());

        // select next
		lIncArrowWrapper.mouse().click(1);
        lSpinnerWrapper.waitState(new State<String>() { @Override public String reached() {
				return lSpinnerWrapper.getControl().getValue();
        }}, "b");

        // select next
		lIncArrowWrapper.mouse().click(1);
        lSpinnerWrapper.waitState(new State<String>() { @Override public String reached() {
				return lSpinnerWrapper.getControl().getValue();
        }}, "c");

        // select next (cyclic)
		lIncArrowWrapper.mouse().click(1);
        lSpinnerWrapper.waitState(new State<String>() { @Override public String reached() {
				return lSpinnerWrapper.getControl().getValue();
        }}, "a");


        // type "b"
        lSpinnerWrapper.as(org.jemmy.interfaces.Text.class).clear();
        lSpinnerWrapper.as(org.jemmy.interfaces.Text.class).type("b");
		lIncArrowWrapper.mouse().click(1); // we need a way to move the focus so the contents of the textfield are processed
        lSpinnerWrapper.waitState(new State<String>() { @Override public String reached() {
				return lSpinnerWrapper.getControl().getValue();
        }}, "c");
    }

}
