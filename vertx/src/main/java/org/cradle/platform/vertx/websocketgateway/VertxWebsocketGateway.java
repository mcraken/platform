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
package org.cradle.platform.vertx.websocketgateway;

import java.util.Map;

import org.cradle.localization.LocalizationService;
import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.httpgateway.HttpMethod;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.filter.FilterInvokationHandler;
import org.cradle.platform.httpgateway.spi.BasicHttpGateway;
import org.cradle.platform.websocketgateway.WebSocket;
import org.cradle.platform.websocketgateway.spi.WebsocketHandlerRegistrationPrinicipal;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 19, 2015
 */
public class VertxWebsocketGateway extends BasicHttpGateway  {

	private SockJSServer sockJSServer;

	public VertxWebsocketGateway(Map<String, DocumentReader> documentReaders,
			Map<String, DocumentWriter> documentWriters,
			LocalizationService localizationService,
			SockJSServer sockJSServer) {

		super( 
				new WebsocketHandlerRegistrationPrinicipal(null),
				documentReaders, 
				documentWriters, 
				localizationService
				);

		this.sockJSServer = sockJSServer;
	}

	private JsonObject createSockJsConfig(String path) {
		JsonObject config = new JsonObject().putString("prefix", path);
		return config;
	}

	public void stop() {

		if(sockJSServer != null){
			sockJSServer.close();
		}
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.BasicGateway#registerFilterChain(java.lang.String, java.lang.String, org.cradle.platform.httpgateway.filter.ServiceFilterConfig, org.cradle.platform.httpgateway.filter.Filter)
	 */
	@Override
	protected void registerHttpHandler(HttpMethod annotation, final FilterInvokationHandler invokationHandler) {

		JsonObject config = createSockJsConfig(((WebSocket) annotation).path());

		sockJSServer.installApp(config, new Handler<SockJSSocket>(){

			@Override
			public void handle(SockJSSocket socket) {

				VertxWebsocketAdapter httpAdapter = new VertxWebsocketAdapter(socket);

				try {
					
					invokationHandler.filter(httpAdapter);
					
				} catch (HttpException e) {
					
					socket.close();
				}

			}

		});

	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.BasicGateway#unregisterHttpHandler(java.lang.String, java.lang.String)
	 */
	@Override
	protected void unregisterHttpHandler(String method, String path) {

		throw new UnsupportedOperationException();

	}

}
