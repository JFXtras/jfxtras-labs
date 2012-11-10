package jfxtras.labs.scene.layout;

import javafx.scene.Node;

public class LayoutUtil
{
	/**
	 * The layout constraints
	 *
	 */
	public static class C
	{
		// minWidth
		public C minWidth(double value) { this.minWidth = value; return this; }
		double minWidth = -1;
		
		// maxWidth
		public C maxWidth(double value) { this.maxWidth = value; return this; }
		double maxWidth = -1;
		
		// minHeight
		public C minHeight(double value) { this.minHeight = value; return this; }
		double minHeight = -1;
		
		// maxHeight
		public C maxHeight(double value) { this.maxHeight = value; return this; }
		double maxHeight = -1;
	}
	
	static public void applyConstraints(Node node, C c)
	{
		setMinWidth(node, c);
		setMaxWidth(node, c);
		setMinHeight(node, c);
		setMaxHeight(node, c);
	}
	
	/**
	 * 
	 */
	static public void resetSizesToDefault(Node node)
	{
		// just to prevent problems
		if (node == null) return;
		
		// make things go away
		if (node instanceof javafx.scene.control.Button)
		{
			javafx.scene.control.Button lButton = (javafx.scene.control.Button)node;
			lButton.setMaxWidth(lButton.getPrefWidth());
			lButton.setMaxHeight(lButton.getPrefHeight());
		}
	}
	
	/**
	 * 
	 */
	static public void setMinWidth(Node node, C c)
	{
		// just to prevent problems
		if (node == null) return;
		if (c.minWidth < 0) return;
		
		// make things go away
		if (node instanceof javafx.scene.control.Button)
		{
			javafx.scene.control.Button lButton = (javafx.scene.control.Button)node;
			lButton.setMinWidth( c.minWidth );
		}
	}
	
	/**
	 * 
	 */
	static public void setMaxWidth(Node node, C c)
	{
		// just to prevent problems
		if (node == null) return;
		
		// make things go away
		if (node instanceof javafx.scene.control.Button)
		{
			javafx.scene.control.Button lButton = (javafx.scene.control.Button)node;
			lButton.setMaxWidth( c.maxWidth >= 0 ? c.maxWidth : Double.MAX_VALUE);
		}
	}
	
	/**
	 * 
	 */
	static public void setMinHeight(Node node, C c)
	{
		// just to prevent problems
		if (node == null) return;
		if (c.minHeight < 0) return;
		
		// make things go away
		if (node instanceof javafx.scene.control.Button)
		{
			javafx.scene.control.Button lButton = (javafx.scene.control.Button)node;
			lButton.setMinHeight(c.minHeight);
		}
	}
	
	/**
	 * 
	 */
	static public void setMaxHeight(Node node, C c)
	{
		// just to prevent problems
		if (node == null) return;
		
		// make things go away
		if (node instanceof javafx.scene.control.Button)
		{
			javafx.scene.control.Button lButton = (javafx.scene.control.Button)node;
			lButton.setMaxHeight( c.maxHeight >= 0 ? c.maxHeight : Double.MAX_VALUE);
		}
	}
	
}
