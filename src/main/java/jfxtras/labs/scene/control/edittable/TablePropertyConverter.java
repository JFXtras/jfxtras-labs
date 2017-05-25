package jfxtras.labs.scene.control.edittable;

public interface TablePropertyConverter<T,P>
{
	TableProperty<P> fromBeanElement(T beanElement);
	T toBeanElement(TableProperty<P> triple);
}
