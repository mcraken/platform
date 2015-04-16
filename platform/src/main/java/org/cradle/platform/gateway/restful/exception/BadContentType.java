/**
 * 
 */
package org.cradle.platform.gateway.restful.exception;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 27, 2014
 */
public class BadContentType extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public BadContentType() {
		super("Missing or bad contentType");
	}

}
