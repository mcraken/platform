/**
 * 
 */
package org.mcplissken.oauth;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 15, 2015
 */
public class NotHandshakingException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public NotHandshakingException() {
		
		super("This user has not started handshaking operation");
	}

}
