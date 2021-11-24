/**
 * 
 */
package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Neelanshu Parnami
 *
 */
public class Inventory {
	
	
	/**
	 * Method to fetch Stock of Items
	 * @return
	 */
	public static Map<String, Item> getStockOfItems() {
		return stockOfItems;
	}

	/**
	 * Method to update Stock of Items
	 * @param stockOfItems
	 */
	public static void updateStockOfItems(Map<String, Item> stockOfItems) {
		Inventory.stockOfItems = stockOfItems;
	}

	private static Map<String, Item> stockOfItems = getStockFromFile();

	/**
	 * Method to get static Stock of Items, can be replaced with file while scaling
	 * @return
	 */
	private static Map<String, Item> getStockFromFile() {
		Map<String, Item> items = new HashMap<String, Item>();
		// TODO - We can declare constants for Description, Stock and Price as well
		items.put(Constants.SUGAR_500G, new Item(Constants.SUGAR_500G, "Sugar 500g", 10, 50));
		items.put(Constants.SUGAR_1KG, new Item(Constants.SUGAR_1KG, "Sugar 1kg", 15, 80));
		items.put(Constants.FLOUR_5KG, new Item(Constants.FLOUR_5KG, "Flour 5Kg", 10, 350));
		items.put(Constants.FLOUR_1KG, new Item(Constants.FLOUR_1KG, "Flour 1kg", 20, 80));
		return items;
	}

	
	/**
	 * Method to release stock of items from any checkout, used when transaction is cancelled or failed
	 * @param checkout
	 */
	public static void releaseStockForCheckout(Checkout checkout) {
		if(checkout.getPickedSkuQuatityMap() != null) {
			Map<String, Item> stockOfItems = Inventory.getStockOfItems();
			boolean anyItemUpdated = false;
			for (Entry<String, Integer> skuQuantity : checkout.getPickedSkuQuatityMap().entrySet()) {
				String sku = skuQuantity.getKey();
				if(stockOfItems.containsKey(sku)) {
					Item item = stockOfItems.get(sku);
					int quantityToRelease = skuQuantity.getValue();
					item.setQuantity(item.getQuantity() + quantityToRelease);
					stockOfItems.put(sku, item);
					anyItemUpdated = true;
				}
			}
			if(anyItemUpdated) {
				Inventory.updateStockOfItems(stockOfItems);
			}
		}
	}
	
	
	

}

