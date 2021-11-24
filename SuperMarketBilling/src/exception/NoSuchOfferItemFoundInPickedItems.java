/**
 * 
 */
package exception;

/**
 * This is Exception class to handle No Such Offer Exception
 * @author Neelanshu Parnami
 *
 */
public class NoSuchOfferItemFoundInPickedItems extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String exceptionMessage;
	
	public NoSuchOfferItemFoundInPickedItems(String exceptionMessage) {
		super(exceptionMessage);
		this.exceptionMessage = exceptionMessage;
	}

	/**
	 * @return the exceptionMessage
	 */
	public String getExceptionMessage() {
		return exceptionMessage;
	}

}
