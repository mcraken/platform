/**
 * 
 */
package org.mcplissken.oauth;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 18, 2015
 */
public class OauthProviderNotSupported extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public OauthProviderNotSupported(String provider) {
		super("Oauth provider " + provider + " not found");
	}

}
