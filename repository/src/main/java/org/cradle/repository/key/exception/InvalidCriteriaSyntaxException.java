/**
 * @author Sherief Shawky(raken123@yahoo.com)
 */
package org.cradle.repository.key.exception;

/**
 * @author Sherief Shawky(raken123@yahoo.com)
 *
 */
public class InvalidCriteriaSyntaxException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidCriteriaSyntaxException(Throwable e){
		super("Criteria Specified is Invalid, check the nested exception.", e);
	}

}
