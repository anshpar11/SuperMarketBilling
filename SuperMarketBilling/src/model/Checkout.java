/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cache.ICombinationCache;
import cache.NumericCombinationCache;
import exception.InvalidInputException;
import exception.ItemOutOfStockException;
import exception.NoSuchItemInInventoryException;
import exception.NoSuchOfferItemFoundInPickedItems;
import exception.OutOfRangeException;

/**
 * Core class used for Checkout process, can be moved to separate package while scaling
 * @author Neelanshu Parnami
 *
 */
public class Checkout {
	
	
	
	/**
	 * Constructor for Checkout Object
	 * @param customer Customer who initiated Checkout
	 * @param availableOffers All available offers/pricing rules in system
	 */
	public Checkout(Customer customer, List<Offer> availableOffers) {
		this.customer = customer;
		this.availableOffers = availableOffers;
		// Convert available offers to maintainable availableSkuQuatityOfferMap, 
		// which stores mapping of each Item (Unique - sku) in a format such as
		// SKU1@3 - Represents mapping to store offer with item with Unique ID - SKU1 is purchased 3 times
		// This is only useful for Multi-Quantity offers
		this.availableSkuQuatityOfferMap = convertAvailableOffersToSkuQuantityMap(availableOffers);
	}

	/**
	 * Convert available offers to maintainable availableSkuQuatityOfferMap, 
	 * which stores mapping of each Item (Unique - sku) in a format such as
	 * SKU1@3 - Represents mapping to store offer with item with Unique ID - SKU1 is purchased 3 times
	 * This is only useful for Multi-Quantity offers
	 * @param availableOffers List of available offers
	 * @return Mapping easy to maintain and operate over Multi-Quantity offers
	 */
	private Map<String, Offer> convertAvailableOffersToSkuQuantityMap(List<Offer> availableOffers) {
		
		// As available offers would be static, to gain performance
		if(this.availableSkuQuatityOfferMap!= null) {
			return availableSkuQuatityOfferMap;
		}
		// If calling for first time call generic method to fetch mapping for available offers
		return convertOffersToSkuQuantityMap(availableOffers);
	}
	
	
	/**
	 * Generic method to convert offers to maintainable skuQuatityOfferMap, 
	 * which stores mapping of each Item (Unique - sku) in a format such as
	 * SKU1@3 - Represents mapping to store offer with item with Unique ID - SKU1 is purchased 3 times
	 * This is only useful for Multi-Quantity offers
	 * @param offers List of available offers
	 * @return Mapping easy to maintain and operate over Multi-Quantity offers
	 */
	private Map<String, Offer> convertOffersToSkuQuantityMap(List<Offer> offers) {
		
		Map<String, Offer> skuQuatityOfferMap = new HashMap<String, Offer>();
		for (Offer offer : offers) {
			switch (offer.getType()) {
			// For Offer of type MULTIBUY
			case MULTIBUY:
				// Assuming only same items in List, can expect all elements have same sku and size to be quantity for which offer is given
				String sku = offer.getItemSkuList().get(Constants.ARR_FIRST_INDEX);
				int quantity = offer.getItemSkuList().size();
				String skuQuantityKey = sku + Constants.SKU_QUANTITY_DELIMITER + quantity;
				skuQuatityOfferMap.put(skuQuantityKey, offer);
				break;

			default:
				// For Offers not supported raise an alarm
				System.err.println("Offer Type : [" + offer + "] is not supported, skipping offer and continuing...");
				break;
			}
			
		}
		return skuQuatityOfferMap;
	}

	/**
	 * Core method which scans item, applies offers and create/update invoice per changes in checkout transaction
	 * TODO this method can be reduced further to lesser statements when we plan to scale by modularizing more 
	 * @param sku
	 * @throws NoSuchItemInInventoryException
	 * @throws ItemOutOfStockException
	 * @throws NoSuchOfferItemFoundInPickedItems
	 * @throws InvalidInputException
	 * @throws OutOfRangeException
	 */
	public void scan(String sku) throws NoSuchItemInInventoryException, ItemOutOfStockException, NoSuchOfferItemFoundInPickedItems, InvalidInputException, OutOfRangeException {
		// Fetch Inventory
		Map<String, Item> stockOfItems = Inventory.getStockOfItems();
		// Check If Item is present in inventory
		if(!stockOfItems.containsKey(sku)) {
			throw new NoSuchItemInInventoryException("No Such Item with SKU:["+ sku +"] in Inventory, please contact Inventory Manager.");
		}
		// Fetch Item from Inventory
		Item itemToAdd = stockOfItems.get(sku);
		if(itemToAdd.getQuantity()==0) {
			throw new ItemOutOfStockException("Item:["+ itemToAdd +"] is Out of Stock per Inventory, please contact Inventory Manager.");
		}
		
		itemToAdd.setQuantity(itemToAdd.getQuantity() - Constants.ONE);
		stockOfItems.put(sku, itemToAdd);
		// Update Inventory by reducing Inventory
		Inventory.updateStockOfItems(stockOfItems);
		
		itemToAdd = new Item(sku);
		// First item Flow
		if(invoice==null) {
			// Pick the item
			List<Item> pickedItems = new ArrayList<Item>();
			Map<String, Integer> pickedSkuQuatityMap = new HashMap<String, Integer>();
			pickedSkuQuatityMap.put(itemToAdd.getSku(), Constants.ONE);
			pickedItems.add(itemToAdd);
			this.pickedSkuQuatityMap = pickedSkuQuatityMap;
			Map<List<Item>, Double> pickedItemsPriceMap = new HashMap<List<Item>, Double>();
			pickedItemsPriceMap.put(pickedItems, itemToAdd.getUnitPrice());
			// Store the transaction details to invoice
			invoice = new Invoice(customer, null, pickedItems, pickedItemsPriceMap, pickedSkuQuatityMap);

			// Flow when we have more than one item in checkout transaction
		} else {
			//If we already have something in picked items
			List<Item> pickedItems = invoice.getPickedItems();
			// Pick the item
			pickedItems.add(itemToAdd);
			Map<String, Integer> pickedSkuQuatityMap = invoice.getPickedSkuQuatityMap();
			Integer pickedItemQuantity = 0;
			if(pickedSkuQuatityMap.containsKey(itemToAdd.getSku())) {
				pickedItemQuantity = pickedSkuQuatityMap.get(itemToAdd.getSku());
			} 
			pickedItemQuantity++;
			pickedSkuQuatityMap.put(itemToAdd.getSku(), pickedItemQuantity);
			this.pickedSkuQuatityMap = pickedSkuQuatityMap;
			// Update pickedItemsPriceMap by applying offers
			List<Offer> preAppliedOffers = invoice.getAppliedOffers();
			// Reset offer applied flag from items and we are going to apply fresh offers again
			resetOffers(pickedItems);
			// Chose and apply all possible offers such that user gets maximum discount
			List<Offer> appliedOffers = applyOffers(itemToAdd, pickedItems, pickedSkuQuatityMap, availableOffers, preAppliedOffers);
			// Based on chosen/applied offers, generate Picked Items Cost map 
			// which will be used for calculating total and printing invoice whenever required
			Map<List<Item>, Double> pickedItemsPriceMap = getPickedItemsPriceMap(pickedItems, appliedOffers);
			// Store the transaction details to invoice
			invoice = new Invoice(customer, appliedOffers, pickedItems, pickedItemsPriceMap, pickedSkuQuatityMap);
		}
		
	}
	
	/**
	 * Reset offer applied flag from items and we are going to apply fresh offers again
	 * @param pickedItems
	 */
	private void resetOffers(List<Item> pickedItems) {
		pickedItems.forEach(item -> item.setAnyOfferApplied(false));
	}

	
	/**
	 * This method can be used to improve some performance while generating cost map if we consider existing cost map
	 * TODO - Can use this method to scale performance for large checkout, just uncomment when needed
	 * @param itemToAdd
	 * @param pickedItems
	 */
//	private void resetOffersForItem(Item itemToAdd, List<Item> pickedItems) {
//		String sku = itemToAdd.getSku();
//		for (Item item : pickedItems) {
//			if(sku.equals(item.getSku())) {
//				item.setAnyOfferApplied(false);
//			}
//		}
//	}
	
	

	/**
	 * Based on chosen/applied offers, generate Picked Items Cost map, 
	 * which will be used for calculating total and printing invoice whenever required
	 * @param pickedItems
	 * @param appliedOffers
	 * @return
	 * @throws NoSuchOfferItemFoundInPickedItems
	 */
	private Map<List<Item>, Double> getPickedItemsPriceMap(List<Item> pickedItems, List<Offer> appliedOffers) throws NoSuchOfferItemFoundInPickedItems {
		Map<List<Item>, Double> pickedItemsPriceMap = new HashMap<List<Item>, Double>();
		for (Offer offer : appliedOffers) {
			List<Item> items = new ArrayList<Item>();
			for (String sku : offer.getItemSkuList()) {
				Item item = fetchItemWithSku(sku, pickedItems);
				if(item == null) {
					throw new NoSuchOfferItemFoundInPickedItems("Offer Item : ["+sku+"] not present in Picked Items, please re initiate checkout or contact Invoice Manager");
				}
				items.add(item);
			}
			pickedItemsPriceMap.put(items, offer.getFlatRate());
		}
		// Add left over items to price map on which no offer is applied
		for (Item item : pickedItems) {
			if(!item.isAnyOfferApplied()) {
				List<Item> items = new ArrayList<Item>();
				items.add(item);
				pickedItemsPriceMap.put(items, item.getUnitPrice());
			}
		}
		return pickedItemsPriceMap;
	}

	private Item fetchItemWithSku(String sku, List<Item> pickedItems) {
		for (Item item : pickedItems) {
			// Check for only items with no offers applied
			if(!item.isAnyOfferApplied()) {
				if(sku == item.getSku()) {
					item.setAnyOfferApplied(true);
					return item;
				} 
			}
		}
		return null;
	}

	/**
	 * Chose and apply all possible offers such that user gets maximum discount
	 * @param itemToAdd
	 * @param pickedItems
	 * @param pickedSkuQuatityMap
	 * @param availableOffers
	 * @param preAppliedOffers
	 * @return
	 * @throws InvalidInputException
	 * @throws OutOfRangeException
	 */
	private List<Offer> applyOffers(Item itemToAdd, List<Item> pickedItems, Map<String, Integer> pickedSkuQuatityMap,
			List<Offer> availableOffers, List<Offer> preAppliedOffers) throws InvalidInputException, OutOfRangeException {
		
		List<Offer> filteredOffers = filterOffersForItem(itemToAdd, preAppliedOffers);
		List<Offer> appliedOffers = applyOffersForItem(itemToAdd, availableOffers, pickedSkuQuatityMap);
		appliedOffers.addAll(filteredOffers);
		return appliedOffers;
	}

	/**
	 * Chose and apply all possible offers for a particular item such that user gets maximum discount
	 * @param itemToAdd
	 * @param availableOffers
	 * @param pickedSkuQuatityMap
	 * @return
	 * @throws InvalidInputException
	 * @throws OutOfRangeException
	 */
	private List<Offer> applyOffersForItem(Item itemToAdd, List<Offer> availableOffers, Map<String, Integer> pickedSkuQuatityMap) throws InvalidInputException, OutOfRangeException {
		ICombinationCache iCache = new NumericCombinationCache();
		List<ArrayList<Integer>> combinations = null;
		Integer pickedItemQuantity = pickedSkuQuatityMap.get(itemToAdd.getSku());
		combinations = iCache.getCombinations(pickedItemQuantity, false);
		return selectOffersForItem(itemToAdd, availableOffers, combinations);	
	}

	/**
	 * @param itemToAdd
	 * @param preAppliedOffers
	 * @return
	 */
	private List<Offer> filterOffersForItem(Item itemToAdd, List<Offer> preAppliedOffers) {
		// Check if no filtering required
		if(preAppliedOffers == null || preAppliedOffers.isEmpty()) {
			// Return empty array to avoid null pointers
			return new ArrayList<Offer>();
		} else {
			String sku = itemToAdd.getSku();
			preAppliedOffers.removeIf(offer -> offer.getItemSkuList().contains(sku));
			return preAppliedOffers;
		}
	}

	/**
	 * Chose best offers such that user gets maximum discount
	 * @param itemToAdd
	 * @param availableOffers
	 * @param combinations
	 * @return
	 */
	private List<Offer> selectOffersForItem(Item itemToAdd, List<Offer> availableOffers, List<ArrayList<Integer>> combinations) {
		List<Offer> bestOffers = new ArrayList<Offer>();
		double maxDiscount = 0.00;
		// For each combination, select best offers
		for (ArrayList<Integer> combination : combinations) {
			
			double discount = 0.00;
			List<Offer> currentOffers = new ArrayList<Offer>();
			for(Integer quantity : combination) {		
				String skuCombinationKey = itemToAdd.getSku() + Constants.SKU_QUANTITY_DELIMITER + quantity;
				Map<String, Offer> availableSkuQuatityOfferMap = getAvailableSkuQuatityPriceMap();
				if(availableSkuQuatityOfferMap.containsKey(skuCombinationKey)) {
					Offer offer = availableSkuQuatityOfferMap.get(skuCombinationKey);
					discount = discount + offer.getMaxDiscount();
					currentOffers.add(offer);
					if(discount > maxDiscount) {
						bestOffers = currentOffers;
						maxDiscount = discount;
					}
				}
			}
		}
		return bestOffers;
	}

	/**
	 * @return the invoice
	 */
	public Invoice getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice the invoice to set
	 */
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	/**
	 * @return the checkoutTotal
	 */

	/**
	 * @return the availableOffers
	 */
	public List<Offer> getAvailableOffers() {
		return availableOffers;
	}
	
	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the availableSkuQuatityPriceMap
	 */
	public Map<String, Offer> getAvailableSkuQuatityPriceMap() {
		return availableSkuQuatityOfferMap;
	}

	/**
	 * @return the pickedSkuQuatityMap
	 */
	public Map<String, Integer> getPickedSkuQuatityMap() {
		return pickedSkuQuatityMap;
	}

	private final List<Offer> availableOffers;
	
	private Invoice invoice;
	
	private final Customer customer;
	
	private final Map<String, Offer> availableSkuQuatityOfferMap;
	
	private Map<String, Integer> pickedSkuQuatityMap;

}
