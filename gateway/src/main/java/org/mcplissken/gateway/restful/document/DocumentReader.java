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
public interface DocumentReader {
	
	public Object read(Class<?> documentType, ByteBuffer buffer) throws DocumentReadingExcetion;
	
}
