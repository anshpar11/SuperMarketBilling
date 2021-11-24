/**
 * 
 */
package exception;

/**
 * This is Exception class to handle Invalid Input Exception
 * @author Neelanshu Parnami
 *
 */
public class InvalidInputException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String exceptionMessage;
	
	public InvalidInputException(String exceptionMessage) {
		super(exceptionMessage);
		this.exceptionMessage = exceptionMessage;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

}
