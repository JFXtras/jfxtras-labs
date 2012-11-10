package jfxtras.labs.scene.layout;

import javafx.scene.Node;

public class LayoutUtil
{
	/*
	 * 
	 */
	static public void sanatizeNodeReset(Node node)
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
	
	/*
	 * 
	 */
	static public void sanatizeNodeSetMaxWidth(Node node)
	{
		// just to prevent problems
		if (node == null) return;
		
		// make things go away
		if (node instanceof javafx.scene.control.Button)
		{
			javafx.scene.control.Button lButton = (javafx.scene.control.Button)node;
			lButton.setMaxWidth(Integer.MAX_VALUE);
		}
	}
	
	/*
	 * 
	 */
	static public void sanatizeNodeSetMaxHeight(Node node)
	{
		// just to prevent problems
		if (node == null) return;
		
		// make things go away
		if (node instanceof javafx.scene.control.Button)
		{
			javafx.scene.control.Button lButton = (javafx.scene.control.Button)node;
			lButton.setMaxHeight(Integer.MAX_VALUE);
		}
	}
}
