/**
 * 
 */
package org.cradle.platform.httpgateway.restful.exception;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 26, 2014
 */
public class BadRequestException extends RESTfulException{

	/**
	 * @param e
	 */
	public BadRequestException(Throwable e) {
		super("Bad request", e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.exception.RESTfulException#getErrorCode()
	 */
	@Override
	public int getErrorCode() {
		return 400;
	}

}
