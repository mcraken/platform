/**
 * 
 */
package org.mcplissken.gateway.restful.exception;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 26, 2014
 */
public abstract class RESTfulException extends Exception{
	
	/**
	 * 
	 */
	public RESTfulException(String msg, Throwable e) {
		super(msg, e);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract int getErrorCode();
	
}
