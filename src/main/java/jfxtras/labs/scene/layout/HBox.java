package jfxtras.labs.scene.layout;

import java.util.WeakHashMap;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;

/**
 * A drop-in replacement for JavaFX's HBox using layout constraints.
 * So instead of:
 * 	HBox lHBox = new HBox(5.0);		
 * 	Button b1 = new Button("short");
 * 	lHBox.getChildren().add(b1);
 * 	HBox.setHgrow(b1, Priority.ALWAYS); 
 *
 * You can write:
 * 	HBox lHBox = new HBox(5.0);		
 *  lHBox.add(new Button("short"), new HBox.C().hgrow(Priority.ALWAYS));
 *
 * This class is not a reimplementation of HBox, but only applies a different API.
 *   
 * @author Tom Eugelink
 *
 */
public class HBox extends javafx.scene.layout.HBox
{
	// ========================================================================================================================================================
	// Constructors
	
	/**
	 * 
	 */
	public HBox()
	{
		super();
		construct();
	}
	
	/**
	 * 
	 * @param spacing
	 */
	public HBox(double spacing)
	{
		super(spacing);
		construct();
	}

	/**
	 * 
	 */
	private void construct()
	{
		getChildren().addListener(new ListChangeListener<Node>()
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Node> changes)
			{
				while (changes.next())
				{
					for (Node lNode : changes.getAddedSubList())
					{
						C lC = cMap.get(lNode);
						if (lC != null) lC.apply(lNode);
					}
				}
			}
		});
	}
	
	
	// ========================================================================================================================================================
	// Properties
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public HBox withSpacing(double value)
	{
		super.setSpacing(value);
		return this;
	}
	
	
	// ========================================================================================================================================================
	// Layout constraints
	
	/**
	 * The layout constraints
	 *
	 */
	public static class C extends GenericLayoutConstraints.C<C>
	{
		// hgrow
		public C hgrow(javafx.scene.layout.Priority value) { this.priority = value; return this; }
		private javafx.scene.layout.Priority priority = null;
		private javafx.scene.layout.Priority priorityReset = null;
		
		// margin
		public C margin(javafx.geometry.Insets value) { this.margin= value; return this; }
		private javafx.geometry.Insets margin = null;
		private javafx.geometry.Insets marginReset = null;
		
		/**
		 * @param node
		 */
		protected void rememberResetValues(Node node)
		{
			super.rememberResetValues(node);
			priorityReset = javafx.scene.layout.HBox.getHgrow(node);
			marginReset = javafx.scene.layout.HBox.getMargin(node);
		}
		
		/**
		 * 
		 * @param node
		 */
		protected void apply(Node node)
		{
			// sanatize the node
			super.apply(node);

			// apply constraints
			if (priority != null) GenericLayoutConstraints.overrideMaxHeight(node, this);
			javafx.scene.layout.HBox.setHgrow(node, priority != null ? priority : priorityReset);
			javafx.scene.layout.HBox.setMargin(node, margin != null ? margin : marginReset);
		}
	}
	
	/**
	 * The collection of layout constraints
	 */
	private WeakHashMap<Node, C> cMap = new WeakHashMap<>();
	
	/**
	 * Add
	 */
	public void add(Node node)
	{
		// add node
		getChildren().add(node);
	}

	/**
	 * Add
	 */
	public void add(Node node, C c)
	{
		// remember constraints
		cMap.put(node, c);
		c.rememberResetValues(node);
		
		// add node
		getChildren().add(node);
	}

	/**
	 * Remove a node completely
	 * @param node
	 */
	public void remove(Node node)
	{
		// remove node
		getChildren().remove(node);
		
		// remove constraints
		cMap.remove(node);
	}

	/**
	 * set constraint without adding the node (in case the node might end up here because of an animation or something) 
	 */
	public void setConstraint(Node node, C c)
	{
		// remember constraints
		cMap.put(node, c);
		c.rememberResetValues(node);
	}

	/**
	 * Remove a constraint, not the node.
	 * @param node
	 */
	public void removeConstraintsFor(Node node)
	{
		cMap.remove(node);
	}
	
	/**
	 * Remove a node, not the constraints.
	 * @param node
	 */
	public void removeNode(Node node)
	{
		getChildren().remove(node);
	}
}
