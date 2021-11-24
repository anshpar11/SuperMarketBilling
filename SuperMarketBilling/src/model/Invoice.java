/**
 * 
 */
package model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * This is used to store Invoice Object which is used back forth to utilize existing applied offers, chosen item 
 * i.e. system state while we are continuously scanning items for seamless experience
 * @author Neelanshu Parnami
 *
 */
public class Invoice {
	
	
	/**
	 * @param appliedOffers
	 * @param pickedItems
	 * @param pickedItemsPriceMap
	 * @param total
	 */
	public Invoice(Customer customer, List<Offer> appliedOffers, List<Item> pickedItems, Map<List<Item>, Double> pickedItemsPriceMap,
			Map<String, Integer> pickedSkuQuatityMap) {
		this.appliedOffers = appliedOffers;
		this.pickedItems = pickedItems;
		this.pickedItemsPriceMap = pickedItemsPriceMap;
		this.pickedSkuQuatityMap = pickedSkuQuatityMap;
		this.invoiceDateTime = LocalDateTime.now();
		this.invoiceId = generateNewInvoiceId(customer, this.invoiceDateTime);
		this.customer = customer;
	}
	
	/**
	 * @param customer
	 * @param invoiceDateTime
	 * @return
	 */
	private String generateNewInvoiceId(Customer customer, LocalDateTime invoiceDateTime) {
		
		return customer.getId() + "-" +getInvoiceSequentialId()+"-"+invoiceDateTime.toEpochSecond(ZoneOffset.UTC);
	}
	
	/**
	 * @return
	 */
	public double getTotal() {
		
		this.total = calculateTotal(true);
		return this.total;
	}
	
	/**
	 * @param toPrint Flag used to print invoice vs just calculation
	 * @return
	 */
	private double calculateTotal(boolean toPrint) {
		double localTotal = 0;
		for (Entry<List<Item>, Double> pickedItemsPriceEntry : pickedItemsPriceMap.entrySet()) {
			if(pickedItemsPriceEntry.getKey().size()>Constants.ONE) {
				if(toPrint)
					System.out.println("******************Combo Offer********************");
				for (Item item : pickedItemsPriceEntry.getKey()) {
					if(toPrint)
						System.out.println(item.getName()+"	||	"+item.getSku()+"	||	On Offer");
				}
				if(toPrint) {
					System.out.println("Combo Offer Price :                     "+pickedItemsPriceEntry.getValue());
					System.out.println("*************************************************");
				}
				localTotal = localTotal + pickedItemsPriceEntry.getValue();
			}
			else {
				for (Item item : pickedItemsPriceEntry.getKey()) {
					if(toPrint)
						System.out.println(item.getName()+"	||	"+item.getSku()+"	||	"+item.getUnitPrice());
					localTotal = localTotal + item.getUnitPrice();
				}
			}
		}
		return localTotal;
	}
	
	/**
	 * Method used to Print Invoice Bill
	 */
	public void printBill() {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("********************Invoice**********************");
		System.out.println("ID :    " + invoiceId);
		System.out.println("Customer Name :    " + customer.getName());
		System.out.println("Date :    " + invoiceDateTime);
		System.out.println("*****************Purchased Items*****************");
		System.out.println("Name	||	Code	||	Unit Price");
		this.total = calculateTotal(true);
		System.out.println("Total :                                 " + total);
		System.out.println();
		System.out.println();
		System.out.println();
	}
	private List<Offer> appliedOffers;
	private List<Item> pickedItems;
	private Map<List<Item>, Double> pickedItemsPriceMap = new HashMap<List<Item>, Double>();
	/**
	 * @return the pickedItemsPriceMap
	 */
	public Map<List<Item>, Double> getPickedItemsPriceMap() {
		return pickedItemsPriceMap;
	}
	/**
	 * @param pickedItemsPriceMap the pickedItemsPriceMap to set
	 */
	public void setPickedItemsPriceMap(Map<List<Item>, Double> pickedItemsPriceMap) {
		this.pickedItemsPriceMap = pickedItemsPriceMap;
	}
	private Map<String, Integer> pickedSkuQuatityMap;
	private double total;
	private String invoiceId;
	private Customer customer;
	private LocalDateTime invoiceDateTime;
	private static int getInvoiceSequentialId() {
		return new Random().nextInt() & Integer.MAX_VALUE;
	}
	/**
	 * @return the pickedItems
	 */
	public List<Item> getPickedItems() {
		return pickedItems;
	}
	/**
	 * @param pickedItems the pickedItems to set
	 */
	public void setPickedItems(List<Item> pickedItems) {
		this.pickedItems = pickedItems;
	}
	/**
	 * @return the appliedOffers
	 */
	public List<Offer> getAppliedOffers() {
		return appliedOffers;
	}
	/**
	 * @param appliedOffers the appliedOffers to set
	 */
	public void setAppliedOffers(List<Offer> appliedOffers) {
		this.appliedOffers = appliedOffers;
	}
	/**
	 * @return the pickedSkuQuatityMap
	 */
	public Map<String, Integer> getPickedSkuQuatityMap() {
		return pickedSkuQuatityMap;
	}
	/**
	 * @param pickedSkuQuatityMap the pickedSkuQuatityMap to set
	 */
	public void setPickedSkuQuatityMap(Map<String, Integer> pickedSkuQuatityMap) {
		this.pickedSkuQuatityMap = pickedSkuQuatityMap;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}
	
}
