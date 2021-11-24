/**
 * 
 */
package util;

import exception.OutOfRangeException;

/**
 * @author Neelanshu Parnami
 *
 */
public class Percentage {
	
	
	/**
	 * @param value
	 * @throws OutOfRangeException 
	 */
	public Percentage(double value) throws OutOfRangeException {
		range=new Range(value, min, max);
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	public static double getMin() {
		return min;
	}
	public static double getMax() {
		return max;
	}
	public Range getRange() {
		return range;
	}


	private final double value;
	// TODO - Can be moved to special constants file in utility
	private final static double min = 0.00;
	private final static double max = 100.00;
	private final Range range;
	
	

}
