package jfxtras.labs.scene.layout;

import java.util.WeakHashMap;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;

/**
 * A drop-in replacement for JavaFX's VBox using layout constraints.
 * So instead of:
 * 	VBox lVBox = new VBox(5.0);		
 * 	Button b1 = new Button("short");
 * 	lVBox.getChildren().add(b1);
 * 	VBox.setVgrow(b1, Priority.ALWAYS); 
 *
 * You can write:
 * 	VBox lVBox = new VBox(5.0);		
 *  lVBox.add(new Button("short"), new VBox.C().vgrow(Priority.ALWAYS));
 *  
 * This class is not a reimplementation of VBox, but only applies a different API.
 * 
 * @author Tom Eugelink
 *
 */
public class VBox extends javafx.scene.layout.VBox
{
	// ========================================================================================================================================================
	// Constructors
	
	/**
	 * 
	 */
	public VBox()
	{
		super();
		construct();
	}
	
	/**
	 * 
	 * @param spacing
	 */
	public VBox(double spacing)
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
	public VBox withSpacing(double value)
	{
		super.setSpacing(value);
		return this;
	}
	
	
	// ========================================================================================================================================================
	// Layout constraints
	
	/**
	 * The layout constraints
	 */
	public static class C extends GenericLayoutConstraints.C<C>
	{
		// vgrow
		public C vgrow(javafx.scene.layout.Priority value) { this.vgrow = value; return this; }
		private javafx.scene.layout.Priority vgrow = null;
		private javafx.scene.layout.Priority vgrowReset = null;
		
		// margin
		public C margin(javafx.geometry.Insets value) { this.margin= value; return this; }
		javafx.geometry.Insets margin = null;
		javafx.geometry.Insets marginReset = null;
		
		/**
		 * @param node
		 */
		protected void rememberResetValues(Node node)
		{
			super.rememberResetValues(node);
			vgrowReset = javafx.scene.layout.VBox.getVgrow(node);
			marginReset = javafx.scene.layout.VBox.getMargin(node);
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
			if (vgrow != null) GenericLayoutConstraints.overrideMaxWidth(node, this);
			javafx.scene.layout.VBox.setVgrow(node, vgrow != null ? vgrow : vgrowReset);
			javafx.scene.layout.VBox.setMargin(node, margin != null ? margin : marginReset);
		}
	}
	
	/**
	 * The collection of layout constraints
	 */
	private WeakHashMap<Node, C> cMap = new WeakHashMap<>();

	/**
	 * 
	 * @param node
	 */
	public void add(Node node)
	{
		// add node
		getChildren().add(node);
	}

	/**
	 * 
	 * @param node
	 * @param c
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
	 * @param node
	 * @param c
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
