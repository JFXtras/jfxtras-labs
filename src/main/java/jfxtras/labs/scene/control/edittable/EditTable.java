package jfxtras.labs.scene.control.edittable;

import java.util.List;

import javafx.scene.control.Control;

public abstract class EditTable<T> extends Control
{
	private List<T> beanList;
	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
	}
	public List<T> getBeanList() {
		return beanList;
	}
}
