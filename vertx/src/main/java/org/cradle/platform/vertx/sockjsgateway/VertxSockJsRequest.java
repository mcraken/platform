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
package org.cradle.platform.vertx.sockjsgateway;

import java.util.Map;

import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.document.DocumentReadingExcetion;
import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.spi.AsynchronusRequestHandler;
import org.cradle.platform.httpgateway.spi.GatewayRequest;
import org.cradle.platform.httpgateway.spi.GatewayResponse;
import org.cradle.platform.httpgateway.spi.MultipartRequestHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.sockjs.SockJSSocket;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 19, 2015
 */
public class VertxSockJsRequest extends GatewayRequest {

	private VertxSockJsAdapter sockJsAdapter;
	
	/**
	 * @param httpAdapter
	 * @param documentReaders
	 * @param tempFolder
	 */
	public VertxSockJsRequest(HttpAdapter httpAdapter,
			Map<String, DocumentReader> documentReaders, String tempFolder) {
		
		super(httpAdapter, documentReaders, tempFolder);
		
		sockJsAdapter = (VertxSockJsAdapter) httpAdapter;
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.spi.GatewayRequest#readDocumentObjectAsynchronously(org.cradle.platform.httpgateway.spi.AsynchronusRequestHandler, java.lang.Class)
	 */
	@Override
	public void readDocumentObjectAsynchronously(
			final AsynchronusRequestHandler handler, final Class<?> documentType) {
		
		SockJSSocket socket = sockJsAdapter.socket();
		
		socket.dataHandler(new Handler<Buffer>(){

			@Override
			public void handle(Buffer buffer) {

				byte[] input = buffer.getByteBuf().array();

				appendInput(input);
				
				try {

					handler.handleDocument(readDocumentObject(documentType));

				} catch (DocumentReadingExcetion e) {

					sockJsAdapter.exception(new BadRequestException(e));
				}
			}
			
		});
		
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.httpgateway.spi.GatewayRequest#readMultipartRequest(org.cradle.platform.httpgateway.spi.MultipartRequestHandler, org.cradle.platform.httpgateway.spi.GatewayResponse)
	 */
	@Override
	public void readMultipartRequest(MultipartRequestHandler handler,
			GatewayResponse response) {
		
		 throw new UnsupportedOperationException();

	}

}
