/**
 * 
 */
package impl;

import java.util.List;

import exception.InvalidInputException;
import exception.ItemOutOfStockException;
import exception.NoSuchItemInInventoryException;
import exception.NoSuchOfferItemFoundInPickedItems;
import exception.OutOfRangeException;
import model.Checkout;
import model.Constants;
import model.Customer;
import model.Inventory;
import model.Invoice;
import model.Offer;

/**
 * This is the main class used for running Super Market Shopping Application
 * @author Neelanshu Parnami
 *
 */
public class ShoppingMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Get the list of available Shopping Offers/Pricing Rules
		List<Offer> availableOffers = Offer.getStaticOffers();
		// Create a customer who wants to shop
		Customer customer1 = new Customer(1, "Amit Jangid");
		// Initiate shopping by customer by creating Checkout Object, passing available offers/pricing rules
		Checkout checkout1 = new Checkout(customer1, availableOffers);
		try {
			// Initiate the scanning process for various items
			checkout1.scan(Constants.FLOUR_5KG);
			checkout1.scan(Constants.FLOUR_1KG);
			checkout1.scan(Constants.SUGAR_1KG);
			checkout1.scan(Constants.SUGAR_500G);
			checkout1.scan(Constants.SUGAR_500G);
			checkout1.scan(Constants.SUGAR_500G);
			checkout1.scan(Constants.SUGAR_1KG);
			checkout1.scan(Constants.SUGAR_1KG);
			checkout1.scan(Constants.SUGAR_500G);
			checkout1.scan(Constants.SUGAR_500G);
			checkout1.scan(Constants.SUGAR_1KG);
			checkout1.scan(Constants.SUGAR_1KG);
			checkout1.scan(Constants.FLOUR_5KG);
			// Uncomment below statement to test release stock functionality
			//checkout1.scan("INVALID");
			// Fetch invoice from current checkout, whenever you need to see the current total or print bill for customer
			Invoice invoice1 = checkout1.getInvoice();
			//Uncomment below statement to check the current total for customer
			//System.out.println("Checkout-Total:"+invoice1.getTotal());
			// Print customer bill using fetched invoice in previous steps
			invoice1.printBill();
			// For all known exceptions handle exceptional flow
		} catch (ItemOutOfStockException | NoSuchOfferItemFoundInPickedItems | InvalidInputException
				| OutOfRangeException | NoSuchItemInInventoryException e) {
			// Release locked stock of items in case of any known exceptions so that system remains consistent with available stocks
			Inventory.releaseStockForCheckout(checkout1);
			// Print Error to user to review checkout plan and try again
			System.err.println(e.getMessage());
			// For any unknown exceptions handle exceptional flow
		} catch (Exception e) {
			// Release locked stock of items in case of any known exceptions so that system remains consistent with available stocks
			Inventory.releaseStockForCheckout(checkout1);
			// Print Error to user to review checkout plan and try again
			System.err.println("System is experiencing some issues please try again after some time, "
					+ "if the issue persists reach out to Application Support Team at xyz@xyz.com");
			// Notify administrator to reach out to application Developers to handle any unknown flow to improve the product/system
			System.err.println(e.getMessage());
			
		}
		
		// Create a another customer who wants to shop
		Customer customer2 = new Customer(2, "Neelanshu Parnami");
		// Initiate shopping by customer by creating Checkout Object, passing available offers/pricing rules
		Checkout checkout2 = new Checkout(customer2, availableOffers);
		try {
			// Initiate the scanning process for various items
			checkout2.scan("SGR500");
			checkout2.scan("SGR1000");
			checkout2.scan("FLR1000");
			checkout2.scan("SGR1000");
			checkout2.scan("FLR5000");
			checkout2.scan("SGR1000");
			checkout2.scan("SGR1000");
			checkout2.scan("SGR1000");
			checkout2.scan("FLR1000");
			checkout2.scan("SGR1000");
			checkout2.scan("SGR1000");
			checkout2.scan("SGR1000");
			
			// Fetch invoice from current checkout, whenever you need to see the current total or print bill for customer
			Invoice invoice2 = checkout2.getInvoice();	
			//System.out.println("Checkout-Total:"+invoice2.getTotal());
			// Print customer bill using fetched invoice in previous steps
			invoice2.printBill();// For all known exceptions handle exceptional flow
		} catch (ItemOutOfStockException | NoSuchOfferItemFoundInPickedItems | InvalidInputException
				| OutOfRangeException | NoSuchItemInInventoryException e) {
			// Release locked stock of items in case of any known exceptions so that system remains consistent with available stocks
			Inventory.releaseStockForCheckout(checkout1);
			// Print Error to user to review checkout plan and try again
			System.err.println(e.getMessage());
			// For any unknown exceptions handle exceptional flow
		} catch (Exception e) {
			// Release locked stock of items in case of any known exceptions so that system remains consistent with available stocks
			Inventory.releaseStockForCheckout(checkout1);
			// Print Error to user to review checkout plan and try again
			System.err.println("System is experiencing some issues please try again after some time, "
					+ "if the issue persists reach out to Application Support Team at xyz@xyz.com");
			// Notify administrator to reach out to application Developers to handle any unknown flow to improve the product/system
			System.err.println(e.getMessage());
			
		}
	}


}
