/**
 * 
 */
package cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.InvalidInputException;
import exception.OutOfRangeException;
import model.Constants;
import util.CombinationUtil;

/**
 * @author Neelanshu Parnami
 *
 */
public class NumericCombinationCache implements ICombinationCache {
	
	/**
	 * This stores and caches the combinations for various input/sum to enhance performance
	 */
	private static Map<Integer, List<ArrayList<Integer>>> combinationCache = new HashMap<Integer, List<ArrayList<Integer>>>();

	/**
	 * This is an implementation method for generating combinations for Combinational Sum
	 */
	@Override
	public List<ArrayList<Integer>> getCombinations(Integer input, Boolean isDebug) throws InvalidInputException, OutOfRangeException {
		
		// Check if Integer is positive
		if(input < Constants.ZERO) {
			throw new InvalidInputException("Only Positive Integers are allowed");
		}
		
		if(input > Constants.NUMERIC_COMBINATION_MAX_LIMIT) {
			throw new OutOfRangeException("Only Integers less up to "+Constants.NUMERIC_COMBINATION_MAX_LIMIT+" are allowed");
		}
		// Check if combination is present in cache, if present return from cache,
		// else call combinationUtil for Input Number
		if(combinationCache.containsKey(input)) {
			return combinationCache.get(input);
		}
		List<ArrayList<Integer>> combinations = CombinationUtil.findCombinationsForSum(input, isDebug);
		combinationCache.put(input, combinations);
		return combinations;
	}

	@Override
	public void freeCache(long toSize) {
		// We can add a strategy implementation here to free up cache space whenever required to avoid Out of Space Errors
		// TODO - Add Implementation
	}
	
}
