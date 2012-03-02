package jfxtras.labs.scene.control;

/**
 * Items for Spinner providing an integer range without actually creating a list with all values.
 */
public class SpinnerIntegerList extends java.util.AbstractList<Integer>
{
	/**
	 * 
	 */
	public SpinnerIntegerList()
	{
		this( (Integer.MIN_VALUE / 2) + 1, Integer.MAX_VALUE / 2, 1);
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 */
	public SpinnerIntegerList(int min, int max)
	{
		this(min, max, 1);
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @param step
	 */
	public SpinnerIntegerList(int min, int max, int step)
	{
		this.min = min;
		this.size = ((max - min) / step) + 1;
		if (size < 0) throw new IllegalArgumentException("This results in a negative size: " + min + ", " + max + "," + step);
		this.step = step;
	}
	private int min;
	private int size;
	private int step;
	
	
	// ===============================================================================
	// List interface
	
	@Override
	public Integer get(int index)
	{
		if (index < 0) throw new IllegalArgumentException("Index cannot be < 0: " + index);
		int lValue = this.min + (index * this.step);
		return lValue;
	}

	@Override
	public int indexOf(Object o)
	{
		int lValue = ((Integer)o).intValue();
		int lIndex = (lValue - this.min) / this.step;
		return lIndex;
	}
	
	@Override
	public int size()
	{
		return this.size;
	}
}