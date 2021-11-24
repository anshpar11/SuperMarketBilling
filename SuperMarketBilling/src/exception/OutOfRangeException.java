/**
 * 
 */
package exception;

/**
 * This is Exception class to handle Out Of Range Exception
 * @author Neelanshu Parnami
 *
 */
public class OutOfRangeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String exceptionMessage;
	
	public OutOfRangeException(String exceptionMessage) {
		super(exceptionMessage);
		this.exceptionMessage = exceptionMessage;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

}
