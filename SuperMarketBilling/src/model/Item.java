/**
 * 
 */
package model;

import exception.NoSuchItemInInventoryException;

/**
 * @author Neelanshu Parnami
 *
 */
public class Item {
	
	/**
	 * Friendly Constructor to hold any Offer, cant be called by user as we need to hide quantity and price supply from user
	 * @param sku
	 * @param name
	 * @param quantity Available quantity for item in inventory/stock
	 * @param unitPrice
	 */
	Item(String sku, String name, long quantity, double unitPrice) {
		super();
		this.sku = sku;
		this.name = name;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.anyOfferApplied = false;
	}
	
	
	/**
	 * Constructor exposed to user, so that behind the scenes item is always fetched from Inventory
	 * @param sku
	 * @throws NoSuchItemInInventoryException
	 */
	public Item(String sku) throws NoSuchItemInInventoryException {
		if(!Inventory.getStockOfItems().containsKey(sku)) {
			throw new NoSuchItemInInventoryException("No Such Item with SKU:["+ sku +"] in Inventory, please contact Inventory Manager.");
		}
		Item item = Inventory.getStockOfItems().get(sku);
		this.sku = sku;
		this.name = item.getName();
		this.quantity = item.getQuantity();
		this.unitPrice = item.getUnitPrice();
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public String getSku() {
		return sku;
	}
	public String getName() {
		return name;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	
	/**
	 * @return the anyOfferApplied
	 */
	public boolean isAnyOfferApplied() {
		return anyOfferApplied;
	}
	/**
	 * @param anyOfferApplied the anyOfferApplied to set
	 */
	public void setAnyOfferApplied(boolean anyOfferApplied) {
		this.anyOfferApplied = anyOfferApplied;
	}
	
	

	@Override
	public String toString() {
		return "Item [sku=" + sku + ", name=" + name + ", quantity=" + quantity + ", unitPrice=" + unitPrice
				+ ", anyOfferApplied=" + anyOfferApplied + "]";
	}



	private final String sku;
	private final String name;
	private long quantity;
	private double unitPrice;
	private boolean anyOfferApplied;
	
}
