package jfxtras.labs.scene.control.edittable.triple;


public interface TripleConverter<T,A,B,C>
{
	Triple<A,B,C> fromBeanElement(T beanElement);
	T toBeanElement(Triple<A,B,C> triple);
}
