package jfxtras.labs.scene.control;

import java.math.BigInteger;

/**
 * Items for Spinner providing an BigInteger range without actually creating a list with all values.
 * Beware: because this is still based on the list inteface, the maximum size of the list is limited by the type used for the index in the list: an integer (Integer.MAX_VALUE).
 * So the difference between the from and to values (to-from) cannot be larger than Integer.MAX_VALUE.
 * What this class allows is that this range can be anywhere in the BigInteger's range. 
 */
public class SpinnerBigIntegerList extends java.util.AbstractList<BigInteger>
{
	/**
	 * 
	 */
	public SpinnerBigIntegerList()
	{
		this( BigInteger.valueOf(Integer.MIN_VALUE / 2).add(BigInteger.ONE), BigInteger.valueOf(Integer.MAX_VALUE / 2), BigInteger.ONE);
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 */
	public SpinnerBigIntegerList(BigInteger from, BigInteger to)
	{
		this(from, to, from.compareTo(to) > 0 ? BigInteger.valueOf(-1) : BigInteger.ONE);
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param step
	 */
	public SpinnerBigIntegerList(BigInteger from, BigInteger to, BigInteger step)
	{
		this.from = from;
		this.size = to.subtract(from).divide(step).add(BigInteger.ONE).intValue();
		if (this.size < 0) throw new IllegalArgumentException("This results in a negative size: " + from + ", " + to + "," + step);
		this.step = step;
	}
	private BigInteger from;
	private int size;
	private BigInteger step;
	
	
	// ===============================================================================
	// List interface
	
	@Override
	public BigInteger get(int index)
	{
		if (index < 0) throw new IllegalArgumentException("Index cannot be < 0: " + index);
		BigInteger lValue = this.from.add(BigInteger.valueOf(index).multiply(this.step));
		return lValue;
	}

	@Override
	public int indexOf(Object o)
	{
		BigInteger lValue = (BigInteger)o;
		BigInteger lIndex = lValue.subtract(this.from).divide(this.step);
		return lIndex.intValue();
	}
	
	@Override
	public int size()
	{
		return this.size;
	}
}