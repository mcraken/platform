/**
 * 
 */
package org.mcplissken.security;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 18, 2015
 */
public class AuthenticationFailureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public AuthenticationFailureException(Throwable e) {
		super("Authentication failed", e);
	}

}
