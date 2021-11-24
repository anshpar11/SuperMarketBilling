/**
 * 
 */
package exception;

/**
 * This is Exception class to handle when there is No Such Item In Inventory Exception
 * @author Neelanshu Parnami
 *
 */
public class NoSuchItemInInventoryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String exceptionMessage;
	
	public NoSuchItemInInventoryException(String exceptionMessage) {
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
