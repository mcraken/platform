/**
 * 
 */
package org.mcplissken.gateway.restful.document;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 6, 2014
 */
public class DocumentReadingExcetion extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public DocumentReadingExcetion(Throwable e) {
		super("Failed to read document", e);
	}

}
