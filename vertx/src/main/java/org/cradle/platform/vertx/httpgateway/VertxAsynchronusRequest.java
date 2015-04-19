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
package org.cradle.platform.vertx.httpgateway;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.document.DocumentReadingExcetion;
import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.spi.AsynchronusRequestHandler;
import org.cradle.platform.httpgateway.spi.MultipartRequestHandler;
import org.cradle.platform.httpgateway.spi.GatewayRequest;
import org.cradle.platform.httpgateway.spi.GatewayResponse;
import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerFileUpload;
import org.vertx.java.core.http.HttpServerRequest;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 12, 2014
 */
public class VertxAsynchronusRequest extends GatewayRequest implements Handler<Buffer>{

	private VertxHttpAdapter vertxHttpAdapter;
	
	/**
	 * @param httpAdapter
	 * @param documentReaders
	 * @param documentType
	 */
	public VertxAsynchronusRequest(
			HttpAdapter httpAdapter,
			Map<String, DocumentReader> documentReaders, 
			String tempFolder) {

		super(httpAdapter, documentReaders, tempFolder);
		
		this.vertxHttpAdapter = ((VertxHttpAdapter) httpAdapter);
	}

	/* (non-Javadoc)
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(Buffer event) {

		byte[] input = event.getByteBuf().array();

		appendInput(input);
	}


	/* (non-Javadoc)
	 * @see org.cradle.osgi.restful.RESTfulRequest#readDocumentObjectAsynchronously(org.cradle.osgi.restful.AsynchronusRESTfulRequestHandler)
	 */
	@Override
	public void readDocumentObjectAsynchronously(
			final AsynchronusRequestHandler handler, final Class<?> documentType) {

		HttpServerRequest httpServerRequest = vertxHttpAdapter.getRequest();

		httpServerRequest.dataHandler(this);

		httpServerRequest.endHandler(new VoidHandler() {

			@Override
			protected void handle() {
				try {
					
					handler.handleDocument(readDocumentObject(documentType));
					
				} catch (DocumentReadingExcetion e) {
					
					vertxHttpAdapter.exception(new BadRequestException(e));
				}
			}
		});

	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.RESTfulRequest#readMultipartRequest(org.cradle.gateway.multipart.MultipartRequestHandler)
	 */
	@Override
	public void readMultipartRequest(final MultipartRequestHandler handler, final GatewayResponse response) {
		
		final HttpServerRequest request =  vertxHttpAdapter.getRequest();
		
		final GatewayRequest restfulRequest = this;
		
		request.response().setChunked(true);
		
		request.expectMultiPart(true);
		
		request.uploadHandler(new Handler<HttpServerFileUpload>() {

			@Override
			public void handle(HttpServerFileUpload upload) {

				String fileFullPath = createUploadName(upload.filename());

				upload.streamToFileSystem(fileFullPath);

				addUpload(fileFullPath);
			}

		});

		request.endHandler(new VoidHandler() {

			public void handle() {
				
				try {
					
					MultiMap attrs = request.formAttributes();

					Iterator<Entry<String, String>> formAttrs = attrs.iterator();

					HashMap<String, String> formAttrsMap = new HashMap<>();

					Entry<String, String> entry = null;

					while(formAttrs.hasNext()){

						entry = formAttrs.next();

						formAttrsMap.put(entry.getKey(), entry.getValue());
					}

					multipartRequestEnded(vertxHttpAdapter, restfulRequest, response, handler, formAttrsMap);

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});
		
		request.exceptionHandler(new Handler<Throwable>() {

			@Override
			public void handle(Throwable e) {
				e.printStackTrace();
			}
		});
	}

}
