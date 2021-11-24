/**
 * 
 */
package util;

import java.util.ArrayList;

/**
 * @author Neelanshu Parnami
 *
 */
public class CombinationUtil {

	/*
	 * combination - array list to store the current combination 
	 * index - next location in array
	 * sum - given sum
	 * reducedSum - reduced sum
	 */
	private static void findCombinationsForSum(ArrayList<ArrayList<Integer>> combinations, 
			ArrayList<Integer> tempCombination, int index, int sum, int reducedSum, boolean isDebug) {
		
		// Base condition
		if (reducedSum < 0)
			return;

		// If combination is
		// found, print it
		if (reducedSum == 0) {
			
			ArrayList<Integer> selectedCombination = new ArrayList<Integer>();
			
			for (int i = 0; i < index; i++) {
				selectedCombination.add(tempCombination.get(i));
				if(isDebug)
					System.out.print(tempCombination.get(i) + " ");
			}
			if(isDebug)
				System.out.println();
			combinations.add(selectedCombination);
			return;
		}

		// Find the previous number
		// stored in combination. It helps
		// in maintaining increasing
		// order
		int previous = (index == 0) ? 1 : tempCombination.get(index - 1);

		// note loop starts from
		// previous number i.e. at
		// array location index - 1
		for (int current = previous; current <= sum; current++) {
			// next element of
			// array is current
			if(tempCombination.size()==index) {
				tempCombination.add(index, current);
			} else {
				tempCombination.set(index, current);
			}

			// call recursively with
			// reduced sum
			findCombinationsForSum(combinations, tempCombination, index + 1, sum, reducedSum - current, isDebug);
		}
	}

	/*
	 * Function to find out all combinations of the positive numbers that add up to given
	 * number. It uses findCombinationsForSum()
	 */
	static public ArrayList<ArrayList<Integer>> findCombinationsForSum(int sum, boolean isDebug) {
		// array to store the current combinations
		// It can contain max sum no elements
		ArrayList<Integer> currentCombination = new ArrayList<Integer>();
		
		ArrayList<ArrayList<Integer>> combinations = new ArrayList<ArrayList<Integer>>(); 

		// find all combinations for given sum
		findCombinationsForSum(combinations, currentCombination, 0, sum, sum, isDebug);
		return combinations;
	}

}
