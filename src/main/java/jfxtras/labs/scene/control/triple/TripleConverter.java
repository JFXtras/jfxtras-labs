package jfxtras.labs.scene.control.triple;

public interface TripleConverter<T>
{
	Triple fromBeanElement(T beanElement);
	T toBeanElement(Triple triple);
}
