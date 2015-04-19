/**
 * 
 */
package org.cradle.platform.document;

import java.nio.ByteBuffer;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 11, 2014
 */
public interface DocumentReader {
	
	public Object read(Class<?> documentType, ByteBuffer buffer) throws DocumentReadingExcetion;
	
}
