/**
 * 
 */
package org.mcplissken.gateway.vertx.restful.client;


import java.nio.ByteBuffer;

import org.mcplissken.gateway.restful.client.HttpDocumentResponseCallback;
import org.mcplissken.gateway.restful.document.DocumentReader;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClientResponse;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 27, 2014
 */
public class DocumentRequestHandler implements Handler<HttpClientResponse> {

	private DocumentReader documentReader;
	private Class<?> documentClass;
	private HttpDocumentResponseCallback callback;
	private Handler<Throwable> errorHandler;

	public DocumentRequestHandler(DocumentReader documentReader,
			Class<?> documentClass, final HttpDocumentResponseCallback callback, Handler<Throwable> errorHandler) {
		this.documentReader = documentReader;
		this.documentClass = documentClass;
		this.callback = callback;
		
		this.errorHandler = errorHandler; 
	}


	/* (non-Javadoc)
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(HttpClientResponse response) {

		response.bodyHandler(new Handler<Buffer>() {

			@Override
			public void handle(Buffer buffer) {
				
				try {
					
					ByteBuffer responseBody = ByteBuffer.allocate(buffer.length());

					responseBody.put(buffer.getBytes());
					
					Object result = documentReader.read(documentClass, responseBody);

					callback.response(result);

				} catch (Exception e) {
					
					callback.error(e);
				}

			}
		});
		
		response.exceptionHandler(errorHandler);
	}

}
