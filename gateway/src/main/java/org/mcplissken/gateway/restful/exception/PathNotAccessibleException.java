/**
 * 
 */
package org.mcplissken.gateway.restful.exception;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 10, 2014
 */
public class PathNotAccessibleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public PathNotAccessibleException(String path, String method) {
		super(path + " is not accessible using: " + method + " for this user.");
	}

}
