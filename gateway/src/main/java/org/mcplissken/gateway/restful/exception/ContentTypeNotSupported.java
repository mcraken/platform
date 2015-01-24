/**
 * 
 */
package org.mcplissken.gateway.restful.exception;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 27, 2014
 */
public class ContentTypeNotSupported extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public ContentTypeNotSupported(String contentType) {
		super("Content type " + contentType + " is not supported");
	}

}
