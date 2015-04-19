/**
 *  Copyright mcplissken.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cradle.platform.vertx.httpgateway.client;


import java.nio.ByteBuffer;

import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.httpgateway.client.HttpDocumentResponseCallback;
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
