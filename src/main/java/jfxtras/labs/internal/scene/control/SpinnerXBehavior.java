/**
 * Copyright (c) 2011, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.internal.scene.control;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import jfxtras.labs.scene.control.SpinnerX;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;

/**
 * 
 * @author Tom Eugelink
 *
 */
public class SpinnerXBehavior<T> extends BehaviorBase<SpinnerX<T>>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 * @param control
	 */
	public SpinnerXBehavior(SpinnerX<T> control)
	{
		super(control);
		construct();
	}
	
	/*
	 * 
	 */
	private void construct()
	{
		
	}

	// ==================================================================================================================
	// EDITABLE
	
	/**
	 * 
	 */
	protected void parse(String text)
	{
		// convert from string to value
		T lValue = getControl().getStringConverter().fromString(text);
		
		// if the value does exists in the domain
		if (getControl().getItems().indexOf(lValue) >= 0)
		{
			// accept value and bail out
			getControl().setValue(lValue);
			return;
		}
		
		// check to see if we have a addCallback
		Callback<T, Integer> lAddCallback = getControl().getAddCallback();
		if (lAddCallback != null)
		{
			// call the callback
			Integer lIndex = lAddCallback.call(lValue);
			
			// if the callback reports that it has processed the value by returning the index it added the item
			if (lIndex != null)
			{
				// accept value and bail out
				getControl().setIndex(lIndex);
				return;
			}
		}
	}
	
	// ==================================================================================================================
	// MOUSE EVENTS
	
	/**
	 * 
	 */
	@Override public void mousePressed(MouseEvent evt)
	{
		// get the control
		SpinnerX lControl = getControl();
		
		// if a control does not have the focus, request focus
		if (!lControl.isFocused() && lControl.isFocusTraversable()) {
			lControl.requestFocus();
		}
	}
	
	// ==================================================================================================================
	// KEY EVENTS
	// TODO: not working
	
	protected final static List<KeyBinding> KEY_BINDINGS = new ArrayList<KeyBinding>();
	static 
	{
		KEY_BINDINGS.addAll(TRAVERSAL_BINDINGS);
		KEY_BINDINGS.add( new KeyBinding(KeyCode.MINUS, "NextPressed") );
		KEY_BINDINGS.add( new KeyBinding(KeyCode.PLUS, "PreviousPressed") );		
	}
	
	@Override protected List<KeyBinding> createKeyBindings() 
	{		
		return KEY_BINDINGS;
	}
	
	@Override protected void callAction(String name) {
		if ("NextPressed".equals(name)) {
			getControl().decrement();
		} 
		else if ("PreviousPressed".equals(name)) {
			getControl().increment();
		} 
		else {
			super.callAction(name);
		}
	}
}
