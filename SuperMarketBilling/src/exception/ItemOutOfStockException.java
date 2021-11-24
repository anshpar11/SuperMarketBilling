/**
 * 
 */
package exception;

/**
 * This is Exception class to handle Item Out Of Stock Exception
 * @author Neelanshu Parnami
 *
 */
public class ItemOutOfStockException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String exceptionMessage;
	
	public ItemOutOfStockException(String exceptionMessage) {
		super(exceptionMessage);
		this.exceptionMessage = exceptionMessage;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

}
