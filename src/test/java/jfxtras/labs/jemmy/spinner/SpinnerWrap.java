/**
 * SpinnerWrap.java
 *
 * Copyright (c) 2011-2013, JFXtras
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

package jfxtras.labs.jemmy.spinner;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import jfxtras.labs.scene.control.ListSpinner;

import org.jemmy.action.GetAction;
import org.jemmy.control.ControlInterfaces;
import org.jemmy.control.ControlType;
import org.jemmy.control.Wrap;
import org.jemmy.env.Environment;
import org.jemmy.fx.ByStyleClass;
import org.jemmy.fx.control.ControlWrap;
import org.jemmy.interfaces.ControlInterface;
import org.jemmy.interfaces.Focus;
import org.jemmy.interfaces.Focusable;
import org.jemmy.interfaces.Parent;
import org.jemmy.interfaces.Selectable;
import org.jemmy.lookup.Lookup;
import org.jemmy.timing.State;

@ControlType(ListSpinner.class)
@ControlInterfaces(value = Selectable.class)
public class SpinnerWrap<T extends ListSpinner> extends ControlWrap<T>
{

	public SpinnerWrap(Environment env, T node)
	{
		super(env, node);
	}

	@Override
	public <INTERFACE extends ControlInterface> boolean is(Class<INTERFACE> interfaceClass)
	{
		if (interfaceClass.equals(Focusable.class)) { return true; }
		Wrap<? extends TextField> inputField = getTextField();
		if (inputField != null) { return inputField.is(interfaceClass); }
		return super.is(interfaceClass);
	}

	@Override
	public <INTERFACE extends ControlInterface> INTERFACE as(Class<INTERFACE> interfaceClass)
	{
		if (interfaceClass.equals(Focusable.class)) { return (INTERFACE) this; }
		Wrap<? extends TextField> inputField = getTextField();
		if (inputField != null) { return inputField.as(interfaceClass); }
		return super.as(interfaceClass);
	}

	@Override
	public Focus focuser()
	{
		return focus;
	}

	private Focus focus = new Focus()
	{
		public void focus()
		{
			if (!isFocused())
			{
				SpinnerWrap.this.as(Parent.class, Node.class).lookup(new ByStyleClass<Node>("arrow-button")).wrap().mouse().click(1);
			}
			waitState(focusedState, true);
		}
	};

	private State<Boolean> focusedState = new State<Boolean>()
	{
		public Boolean reached()
		{
			return isFocused();
		}
	};

	protected Wrap<? extends TextField> getTextField()
	{
		Lookup lookup = as(Parent.class, Node.class).lookup(TextField.class);
		if (lookup.size() > 0)
		{
			Wrap<? extends TextField> inputField = as(Parent.class, Node.class).lookup(TextField.class).wrap();
			return inputField;
		}
		return null;
	}

	public List getStates()
	{
		return new GetAction<List>()
		{
			@Override
			public void run(Object... os) throws Exception
			{
				setResult(getControl().getItems());
			}
		}.dispatch(getEnvironment());
	}

	public Object getState()
	{
		return new GetAction()
		{
			@Override
			public void run(Object... os) throws Exception
			{
				setResult(getControl().getValue());
			}
		}.dispatch(getEnvironment());
	}

	public Class<Object> getType()
	{
		return Object.class;
	}

}
