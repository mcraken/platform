/**
 * 
 */
package org.mcplissken.gateway.restful.document;

import java.nio.ByteBuffer;
/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 11, 2014
 */
public interface DocumentWriter {

	/**
	 * @param document
	 * @param output
	 */
	public void write(Object document, ByteBuffer output);
	
	public void write(Object document, StringBuffer buffer);
}
