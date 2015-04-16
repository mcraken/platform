/**
 * 
 */
package org.cradle.gateway.restful.document;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 6, 2014
 */
public class DocumentReadingExcetion extends Exception {
	
	private String contentType;
	
	
	/**
	 * @param contentType
	 */
	public DocumentReadingExcetion(String contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	
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
