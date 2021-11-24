/**
 * 
 */
package cache;

import java.util.ArrayList;
import java.util.List;

import exception.InvalidInputException;
import exception.OutOfRangeException;

/**
 * @author Neelanshu Parnami
 * This is an Interface for generating combinations with additional caching functionality for improved performance
 * It used CombinationUtil to generate combinations but caches combinations for same input to avoid extra processing for same input each time
 */
public interface ICombinationCache {
	
	/**
	 * This is an interface method for generating combinations for Combinational Sum, 
	 * similarly we can add more methods to this interface for mixed combinations
	 * @param input Target combinational sum input for which combinations needs to be generated
	 * @param isDebug flag to run in debug mode whenever required for testing
	 * @return The set of combinations for provided combinational sum input
	 * @throws InvalidInputException
	 * @throws OutOfRangeException
	 */
	public List<ArrayList<Integer>> getCombinations(Integer input, Boolean isDebug) throws InvalidInputException, OutOfRangeException;
	
	/**
	 * This abstract method allows to provides the strategy to free Cache whenever required to avoid Out of Space Errors
	 * @param toSize
	 */
	public void freeCache(long toSize);
}
