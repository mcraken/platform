/**
 * 
 */
package org.cradle.repository.key.exception;

/**
 * @author sherif.shawky
 *
 */
public class InvalidKeyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidKeyException(){
		super("Key is invalid");
	}
	
	public InvalidKeyException(Throwable e){
		super("Key is invalid", e);
	}

}
