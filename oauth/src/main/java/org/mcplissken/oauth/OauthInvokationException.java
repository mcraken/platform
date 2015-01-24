/**
 * 
 */
package org.mcplissken.oauth;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 15, 2015
 */
public class OauthInvokationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public OauthInvokationException() {
		super("Handshake failed");
	}
	
}
