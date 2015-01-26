/**
 * 
 */
package org.mcplissken.repository.key.exception;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 4, 2014
 */
public class InvalidCriteriaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public InvalidCriteriaException(Throwable e) {
		super("Invalid criteria", e);
	}
	
	public InvalidCriteriaException(String name){
		super("Invalid criteria " + name);
	}
}
