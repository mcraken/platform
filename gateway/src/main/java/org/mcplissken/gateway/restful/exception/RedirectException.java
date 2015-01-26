/**
 * 
 */
package org.mcplissken.gateway.restful.exception;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 20, 2015
 */
public class RedirectException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String url;
	
	/**
	 * 
	 */
	public RedirectException(String url) {
		
		super("Redirect required");
		
		this.url = url;
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

}
