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
package jfxtras.labs.internal.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import jfxtras.labs.scene.control.ListSpinner;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;

/**
 * 
 * @author Tom Eugelink
 *
 */
public class ListSpinnerBehavior<T> extends BehaviorBase<ListSpinner<T>>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 * @param control
	 */
	public ListSpinnerBehavior(ListSpinner<T> control)
	{
		super(control,KEY_BINDINGS);
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
	 * Parse the value (which usually comes from the TextField in the skin).
	 * If the value exists in the current items, select it.
	 * If not and a callback is defined, call the callback to have it handle it.
	 * Otherwise do nothing (leave it to the skin).
	 */
	public void parse(String text)
	{
		// strip
		String lText = text;
		String lPostfix = getControl().getPostfix();
		if (lPostfix.length() > 0 && lText.endsWith(lPostfix)) lText = lText.substring(0, lText.length() - lPostfix.length());
		String lPrefix = getControl().getPrefix();
		if (lPrefix.length() > 0 && lText.startsWith(lPrefix)) lText = lText.substring(lPrefix.length());
		
		// convert from string to value
		T lValue = getControl().getStringConverter().fromString(lText);
		
		// if the value does exists in the domain
		int lItemIndex = getControl().getItems().indexOf(lValue);
		if (lItemIndex >= 0)
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
			
			// if the callback reports that it has processed the value by returning the index where it has added the item. (Or at least the index it wants to show now.)
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
		ListSpinner<T> lControl = getControl();
		
		// if a control does not have the focus, request focus
		if (!lControl.isFocused() && lControl.isFocusTraversable()) {
			lControl.requestFocus();
		}
	}
	
	// ==================================================================================================================
	// KEY EVENTS
	
	final static private String EVENT_PREVIOUS = "PreviousPressed";
	final static private String EVENT_NEXT = "NextPressed";
	protected final static List<KeyBinding> KEY_BINDINGS = new ArrayList<KeyBinding>();
	static 
	{
		KEY_BINDINGS.add( new KeyBinding(KeyCode.MINUS, EVENT_PREVIOUS) ); // keyboard -		
		KEY_BINDINGS.add( new KeyBinding(KeyCode.PLUS, EVENT_NEXT) ); // keyboard +
		KEY_BINDINGS.add( new KeyBinding(KeyCode.SUBTRACT, EVENT_PREVIOUS) ); // keypad -		
		KEY_BINDINGS.add( new KeyBinding(KeyCode.ADD, EVENT_NEXT) ); // keypad + 
		KEY_BINDINGS.add(new KeyBinding(KeyCode.UP, EVENT_NEXT));
		KEY_BINDINGS.add(new KeyBinding(KeyCode.DOWN, EVENT_PREVIOUS));
		KEY_BINDINGS.add(new KeyBinding(KeyCode.LEFT, EVENT_PREVIOUS));
		KEY_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, EVENT_NEXT));
		KEY_BINDINGS.addAll(TRAVERSAL_BINDINGS);
	}
	
	@Override protected void callAction(String name) {
		if (EVENT_PREVIOUS.equals(name)) {
			getControl().decrement();
		} 
		else if (EVENT_NEXT.equals(name)) {
			getControl().increment();
		} 
		else {
			super.callAction(name);
		}
	}
}
