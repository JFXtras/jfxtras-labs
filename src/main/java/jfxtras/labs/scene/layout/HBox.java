package jfxtras.labs.scene.layout;

import java.util.WeakHashMap;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;

/**
 * A drop-in replace ment for JavaFX's HBox using layout constraints.
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
						applyConstraints(lNode);
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
	public static class C extends LayoutUtil.C
	{
		// hgrow
		public C hgrow(javafx.scene.layout.Priority value) { this.priority = value; return this; }
		private javafx.scene.layout.Priority priority = null;
		
		// margin
		public C margin(javafx.geometry.Insets value) { this.margin= value; return this; }
		private javafx.geometry.Insets margin = null;
	}
	
	/**
	 * 
	 * @param node
	 */
	private void applyConstraints(Node node)
	{
		// get constraints
		C lC = cMap.get(node);
		if (lC == null) return; // old style is also allowed (for easy migration)
		
		// sanatize the node
		LayoutUtil.resetSizesToDefault(node);
		LayoutUtil.applyConstraints(node,  lC);

		// apply constraints
		if (lC.priority != null) 
		{
			LayoutUtil.setMaxHeight(node, lC);
			javafx.scene.layout.HBox.setHgrow(node, lC.priority);
		}
		if (lC.margin != null) 
		{
			javafx.scene.layout.HBox.setMargin(node, lC.margin);
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
