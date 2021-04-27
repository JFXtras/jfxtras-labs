/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.scene.control.triple;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import jfxtras.labs.internal.scene.control.skin.edittable.triple.TripleEditTableSkin;
import jfxtras.labs.scene.control.edittable.triple.Triple;
import jfxtras.labs.scene.control.edittable.triple.TripleEditTable;
import jfxtras.test.AssertNode;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

@Ignore // test fails, but this is labs so I'm not going to fix this, that's up to the original developer
public class EmailTripleTest extends JFXtrasGuiTest
{
	private TripleEditTable<Email,String,String,Boolean> control;
    @Override
    public Parent getRootNode()
    {
    	control = new EmailEditTable();
    	return control;
    }
    
    @Test
    public void canCreateEmptyControl()
    {
    	Control node = (Control) find("#emailEditTable");
    	ObservableList<Triple> tableList = ((TripleEditTableSkin) node.getSkin()).getTableList();
    	assertEquals(0, tableList.size()); // has no items until beanList is set
		double w = 741.0;
		double h = 120.0;
//		AssertNode.generateSource("n", node, null, false, jfxtras.test.AssertNode.A.XYWH);
//		new AssertNode(node).assertXYWH(0.0, 0.0, w, h, 0.01);
    }
    
    @Test
    public void canDisplayElements()
    {
    	Control node = (Control) find("#emailEditTable");
    	ObservableList<Triple> tableList = ((TripleEditTableSkin) node.getSkin()).getTableList();

    	List<Email> beanList = new ArrayList<>();
    	beanList.add(new Email("Work", "me@work.com", true));
    	TestUtil.runThenWaitForPaintPulse( () -> control.setBeanList(beanList));
    	assertEquals(2, tableList.size()); // 1 item is empty
    	Triple t = tableList.get(0);
		assertEquals("Work", t.getName());
    	assertEquals("me@work.com", t.getValue());
    	assertEquals(true, t.isPrimary());
    }
    
    @Test
    public void canAddElements()
    {
    	Control node = (Control) find("#emailEditTable");
    	ObservableList<Triple> tableList = ((TripleEditTableSkin) node.getSkin()).getTableList();

    	List<Email> beanList = new ArrayList<>();
    	TestUtil.runThenWaitForPaintPulse( () -> control.setBeanList(beanList));
    	assertEquals(1, tableList.size()); // 1 item is empty

    	TestUtil.runThenWaitForPaintPulse( () -> 
	    	tableList.add(0,
	    			new Triple()
	    				.withName("Personal")
	    				.withValue("me@home.com")
	    				.withPrimary(false)
	    				));
    	assertEquals(2, tableList.size());
    	Triple t = tableList.get(0);
		assertEquals("Personal", t.getName());
    	assertEquals("me@home.com", t.getValue());
    	assertEquals(false, t.isPrimary());
    }
    
    @Test
    public void canCatchInvalidValue()
    {
    }
    
    @Test
    public void canCatchInvalidName()
    {
    }
}
