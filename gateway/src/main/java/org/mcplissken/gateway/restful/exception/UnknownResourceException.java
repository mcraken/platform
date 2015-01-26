/**
 * 
 */
package org.mcplissken.gateway.restful.exception;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 6, 2015
 */
public class UnknownResourceException extends RESTfulException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public UnknownResourceException(Throwable e) {
		
		super("Unknown resource exception.", e);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.restful.exception.RESTfulException#getErrorCode()
	 */
	@Override
	public int getErrorCode() {
		
		return 404;
	}

}
