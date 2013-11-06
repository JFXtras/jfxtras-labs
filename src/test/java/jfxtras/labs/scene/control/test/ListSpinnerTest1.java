/**
 * ListSpinnerTest1.java
 *
 * Copyright (c) 2011-2013, JFXtras All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the <organization> nor the names
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
package jfxtras.labs.scene.control.test;

import javafx.scene.Node;
import jfxtras.labs.AbstractJemmyTest;
import jfxtras.labs.scene.control.ListSpinner;
import jfxtras.labs.scene.control.test.jemmy.SpinnerWrap;

import org.jemmy.control.Wrap;
import org.jemmy.fx.ByStyleClass;
import org.jemmy.fx.Root;
import org.jemmy.timing.State;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author shura
 */
public class ListSpinnerTest1 extends AbstractJemmyTest {

    @Test
    public void select() throws InterruptedException {
        // get the control
//        org.jemmy.interfaces.Parent<Node> lParent = Root.ROOT.lookup().as(org.jemmy.interfaces.Parent.class, Node.class);
//        Wrap<? extends ListSpinner> lControlWrapper = lParent.lookup(ListSpinner.class).wrap();
//        // hack: the lookup only knows controls and their wrappers that are in JemmyFX; this lines creates the unknown SpinnerWrapper by ripping data from the ControlWrapper 
//        final SpinnerWrap<ListSpinner<String>> lSpinnerWrapper = new SpinnerWrap<ListSpinner<String>>(lControlWrapper.getEnvironment(), lControlWrapper.getControl());
//        Wrap lDecArrowWrapper = lSpinnerWrapper.as(org.jemmy.interfaces.Parent.class, Node.class).lookup(new ByStyleClass<Node>("left-arrow")).wrap();
//        Wrap lIncArrowWrapper = lSpinnerWrapper.as(org.jemmy.interfaces.Parent.class, Node.class).lookup(new ByStyleClass<Node>("right-arrow")).wrap();
//
//        // check to see what the current value is
//        Assert.assertEquals("a", lSpinnerWrapper.getControl().getValue());
//
//        // select next
//        lIncArrowWrapper.mouse().click(1);
//        lSpinnerWrapper.waitState(new State<String>() {
//            @Override
//            public String reached() {
//                return lSpinnerWrapper.getControl().getValue();
//            }
//        }, "b");
//
//        // select next
//        lIncArrowWrapper.mouse().click(1);
//        lSpinnerWrapper.waitState(new State<String>() {
//            @Override
//            public String reached() {
//                return lSpinnerWrapper.getControl().getValue();
//            }
//        }, "c");
//
//        // select next (cyclic)
//        lIncArrowWrapper.mouse().click(1);
//        lSpinnerWrapper.waitState(new State<String>() {
//            @Override
//            public String reached() {
//                return lSpinnerWrapper.getControl().getValue();
//            }
//        }, "a");
//
//
//        // type "b"
//        lSpinnerWrapper.as(org.jemmy.interfaces.Text.class).clear();
//        lSpinnerWrapper.as(org.jemmy.interfaces.Text.class).type("b");
//        lIncArrowWrapper.mouse().click(1); // we need a way to move the focus so the contents of the textfield are processed
//        lSpinnerWrapper.waitState(new State<String>() {
//            @Override
//            public String reached() {
//                return lSpinnerWrapper.getControl().getValue();
//            }
//        }, "c");
    }

    @Override
    protected Node createTestContent() {
        return ListSpinnerTestFactory.create();
    }
}
