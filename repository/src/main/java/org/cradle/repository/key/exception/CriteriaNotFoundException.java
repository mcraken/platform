/**
 * 
 */
package org.cradle.repository.key.exception;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 4, 2014
 */
public class CriteriaNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public CriteriaNotFoundException(String crt) {
		super("Could not find criteria: " + crt);
	}
 
}
