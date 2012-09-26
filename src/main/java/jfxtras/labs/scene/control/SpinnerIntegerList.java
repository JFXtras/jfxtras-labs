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
	 * @param from
	 * @param to
	 */
	public SpinnerIntegerList(int from, int to)
	{
		this(from, to, from > to ? -1 : 1);
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param step
	 */
	public SpinnerIntegerList(int from, int to, int step)
	{
		this.from = from;
		this.size = ((to - from) / step) + 1;
		if (size < 0) throw new IllegalArgumentException("This results in a negative size: " + from + ", " + to + "," + step);
		this.step = step;
	}
	private int from;
	private int size;
	private int step;
	
	
	// ===============================================================================
	// List interface
	
	@Override
	public Integer get(int index)
	{
		if (index < 0) throw new IllegalArgumentException("Index cannot be < 0: " + index);
		int lValue = this.from + (index * this.step);
		return lValue;
	}

	@Override
	public int indexOf(Object o)
	{
		// calculate the index
		int lValue = ((Integer)o).intValue();
		int lIndex = (lValue - this.from) / this.step;
		if (lIndex > size) return -1;
		
		// check if that what is at the index matches with out value
		Integer lValueAtIndex = get(lIndex);
		if (o.equals(lValueAtIndex) == false) return -1;
		
		// found it
		return lIndex;
	}
	
	@Override
	public int size()
	{
		return this.size;
	}
}