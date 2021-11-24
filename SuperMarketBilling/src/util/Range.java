/**
 * 
 */
package util;

import exception.OutOfRangeException;

/**
 * @author Neelanshu Parnami
 *
 */
public class Range {
	
	/**
	 * Can be used as utility to define any range
	 * @param value
	 * @param min
	 * @param max
	 * @throws OutOfRangeException 
	 */
	public Range(double value, double min, double max) throws OutOfRangeException {
		if(value<min || value>max) {
			throw new OutOfRangeException("Supplied value:" + value + " is not in allowed range, Min:" + min + ", Max:" + max);
		}
		
		this.value = value;
		this.min = min;
		this.max = max;
	}

	
	public double getValue() {
		return value;
	}
	public double getMin() {
		return min;
	}
	public double getMax() {
		return max;
	}


	private final double value;
	private final double min;
	private final double max;

}
