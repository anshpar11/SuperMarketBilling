/**
 * 
 */
package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.Percentage;

/**
 * This class is to store various available offers or pricing rules
 * @author Neelanshu Parnami
 *
 */
public class Offer {
	
	/**
	 * Constructor to hold any Offer
	 * @param code Offer code applicable in case on online purchasing
	 * @param type Various Offer types available, consider enum values for idea on these
	 * @param itemSkuList List storing item combination for offers
	 * @param discountType Various Discount types available, consider enum values for idea on these
	 * @param discountPercentage This can be used directly if Discount type is PERCENTAGE
	 * @param minBill Minimum Billing amount
	 * @param maxDiscount Maximum Discount amount that can be availed
	 * @param flatRate Flat rate amount for offer
	 * @param isMultiOffersAllowed True If this offer can be clubbed with any other offer
	 * @param isActive If offer is active
	 * @param expiry Holds Expire date for offers to manage offers automatically at the time of 
	 * heavy load or sale, when system restart offer refresh is not possible
	 */
	public Offer(String code, OFFER_TYPE type, List<String> itemSkuList, DISCOUNT_TYPE discountType,
			Percentage discountPercentage, Double minBill, Double flatRate, Double maxDiscount, Boolean isMultiOffersAllowed,
			Boolean isActive, LocalDateTime expiry) {
		this.code = code;
		this.type = type;
		this.itemSkuList = itemSkuList;
		this.discountType = discountType;
		this.discountPercentage = discountPercentage;
		this.minBill = minBill;
		this.flatRate = flatRate;
		if(flatRate != null) {
			this.maxDiscount = calculateDiscountPerFlatRate(itemSkuList, flatRate);
		} else {
			this.maxDiscount = maxDiscount;
		}
		this.isMultiOffersAllowed = isMultiOffersAllowed;
		this.isActive = isActive;
		this.expiry = expiry;
	}
	
	

	/**
	 * Calculates max discount based on offer flat rate considering inventory price
	 * @param itemSkuList
	 * @param flatRate
	 * @return
	 */
	private double calculateDiscountPerFlatRate(List<String> itemSkuList, Double flatRate) {
		Map<String, Item> stockOfItems = Inventory.getStockOfItems();
		double nonDiscountedTotal = 0;
		for (String sku : itemSkuList) {
			nonDiscountedTotal += stockOfItems.get(sku).getUnitPrice();
		}
		return nonDiscountedTotal - flatRate;
	}



	public String getCode() {
		return code;
	}

	public OFFER_TYPE getType() {
		return type;
	}

	public List<String> getItemSkuList() {
		return itemSkuList;
	}

	public DISCOUNT_TYPE getDiscountType() {
		return discountType;
	}

	public Percentage getDiscountPercentage() {
		return discountPercentage;
	}

	public double getMinBill() {
		return minBill;
	}

	public double getMaxDiscount() {
		return maxDiscount;
	}

	public boolean isMultiOffersAllowed() {
		return isMultiOffersAllowed;
	}

	public boolean isActive() {
		return isActive;
	}

	public LocalDateTime getExpiry() {
		return expiry;
	}
	
	/**
	 * @return the flatRate
	 */
	public double getFlatRate() {
		return flatRate;
	}

	public enum OFFER_TYPE {
		MULTIBUY, 
		CARTVALUE,
		NEW,
		SPECIAL
	}
	
	public enum DISCOUNT_TYPE {
		CARD, 
		WALLET,
		CASH,
		FREEBIE,
		GIFTCARD,
		PERCENTAGE
	}
	
	/**
	 * Returns various available static offers or pricing rules, 
	 * can be replaced with reading from file instead to scale this and allow auto reload features so that system is always up
	 * @return
	 */
	public static List<Offer> getStaticOffers() {
		List<Offer> availableOffers = new ArrayList<Offer>();
		
		List<String> itemSkuList1 = new ArrayList<String>();
		itemSkuList1.add(Constants.SUGAR_500G);
		itemSkuList1.add(Constants.SUGAR_500G);
		availableOffers.add(new Offer("MULTI", OFFER_TYPE.MULTIBUY, itemSkuList1, 
				DISCOUNT_TYPE.CASH, null, null, 80.00, null, true, true, 
				LocalDateTime.of(2021, 12, 31, 23, 59)));
		
		List<String> itemSkuList2 = new ArrayList<String>();
		itemSkuList2.add(Constants.SUGAR_500G);
		itemSkuList2.add(Constants.SUGAR_500G);
		itemSkuList2.add(Constants.SUGAR_500G);	
		availableOffers.add(new Offer("MULTI", OFFER_TYPE.MULTIBUY, itemSkuList2, 
				DISCOUNT_TYPE.CASH, null, null, 120.00, null, true, true, 
				LocalDateTime.of(2021, 12, 31, 23, 59)));
		
		List<String> itemSkuList3 = new ArrayList<String>();
		itemSkuList3.add(Constants.SUGAR_1KG);
		itemSkuList3.add(Constants.SUGAR_1KG);
		availableOffers.add(new Offer("MULTI", OFFER_TYPE.MULTIBUY, itemSkuList3, 
				DISCOUNT_TYPE.CASH, null, null, 150.00, null, true, true, 
				LocalDateTime.of(2021, 12, 31, 23, 59)));
		
		List<String> itemSkuList4 = new ArrayList<String>();
		itemSkuList4.add(Constants.SUGAR_1KG);
		itemSkuList4.add(Constants.SUGAR_1KG);
		itemSkuList4.add(Constants.SUGAR_1KG);	
		availableOffers.add(new Offer("MULTI", OFFER_TYPE.MULTIBUY, itemSkuList4, 
				DISCOUNT_TYPE.CASH, null, null, 210.00, null, true, true, 
				LocalDateTime.of(2021, 12, 31, 23, 59)));
		
		List<String> itemSkuList5 = new ArrayList<String>();
		itemSkuList5.add(Constants.FLOUR_5KG);
		itemSkuList5.add(Constants.FLOUR_5KG);	
		availableOffers.add(new Offer("MULTI", OFFER_TYPE.MULTIBUY, itemSkuList5, 
				DISCOUNT_TYPE.CASH, null, null, 600.00, null, true, true, 
				LocalDateTime.of(2021, 12, 31, 23, 59)));
		
		List<String> itemSkuList6 = new ArrayList<String>();
		itemSkuList6.add(Constants.FLOUR_1KG);
		itemSkuList6.add(Constants.FLOUR_1KG);	
		availableOffers.add(new Offer("MULTI", OFFER_TYPE.MULTIBUY, itemSkuList6, 
				DISCOUNT_TYPE.CASH, null, null, 120.00, null, true, true, 
				LocalDateTime.of(2021, 12, 31, 23, 59)));
		
		return availableOffers;
	}
	
	@Override
	public String toString() {
		return "Offer [code=" + code + ", type=" + type + ", itemSkuList=" + itemSkuList + ", discountType="
				+ discountType + ", discountPercentage=" + discountPercentage + ", minBill=" + minBill
				+ ", maxDiscount=" + maxDiscount + ", flatRate=" + flatRate + ", isMultiOffersAllowed="
				+ isMultiOffersAllowed + ", isActive=" + isActive + ", expiry=" + expiry + "]";
	}

	private final String code;
	private final OFFER_TYPE type;
	private final List<String> itemSkuList;
	private final DISCOUNT_TYPE discountType;
	private final Percentage discountPercentage;
	private final Double minBill;
	private final Double maxDiscount;
	private final Double flatRate;
	private final Boolean isMultiOffersAllowed;
	private final Boolean isActive;
	private final LocalDateTime expiry;


}
