/**
 * 
 */
package org.cradle.platform.httpgateway.spi;

import java.nio.ByteBuffer;
import java.util.Map;

import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.ContentTypeNotSupported;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 11, 2014
 */
public class GatewayResponse {

	private HttpAdapter httpAdapter;
	
	private Map<String, DocumentWriter> documentWriters;
	
	private ByteBuffer buffer;
	
	public GatewayResponse(HttpAdapter httpAdapter,
			Map<String, DocumentWriter> documentWriters) {
		
		this.httpAdapter = httpAdapter;
		
		this.documentWriters = documentWriters;
		
		this.buffer = ByteBuffer.allocate(1024 * 1024 * 3);
	}

	public void writeResponseDocument(Object document, String contentType, boolean chunked)
			throws BadRequestException {
		
		if(chunked){
			
			buffer.put(new String("Content-Type: " + contentType + "\n\n").getBytes());
		}
		
		DocumentWriter writer = selectContentWriter(contentType);
		
		writer.write(document, buffer);
		
		end();
	}

	private void end() {
		
		buffer.flip();
		
		httpAdapter.writeResponse(buffer);
		
		buffer.clear();
	}
	
	
	
	private DocumentWriter selectContentWriter(String contentType)
			throws BadRequestException {
		
		DocumentWriter writer = documentWriters.get(contentType);
		
		if(writer == null){
			throw new BadRequestException(new ContentTypeNotSupported(contentType));
		}
		return writer;
	}
	
}
