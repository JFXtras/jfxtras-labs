package jfxtras.labs.scene.control.test.jemmy;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import jfxtras.labs.scene.control.Spinner;

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

@ControlType(Spinner.class)
@ControlInterfaces(value = Selectable.class)
public class SpinnerWrap<T extends Spinner> extends ControlWrap<T>
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
