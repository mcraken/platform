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

import java.util.HashMap;
import java.util.Map;

import org.cradle.localization.LocalizationService;
import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.UnauthorizedException;
import org.cradle.platform.httpgateway.exception.UnknownResourceException;
import org.cradle.platform.httpgateway.filter.Filter;
import org.cradle.platform.httpgateway.filter.FilterFactory;
import org.cradle.platform.httpgateway.filter.ServiceFilterConfig;
import org.cradle.platform.sockjsgateway.spi.SockJsHandlerRegistrationPrinicipal;
import org.cradle.platform.spi.BasicGateway;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 19, 2015
 */
public class VertxSockJsGateway extends BasicGateway  {

	private SockJSServer sockJSServer;

	public VertxSockJsGateway(Map<String, DocumentReader> documentReaders,
			Map<String, DocumentWriter> documentWriters,
			LocalizationService localizationService, String tempFolder,
			SockJSServer sockJSServer) {

		super(
				new SockJsHandlerRegistrationPrinicipal(null),
				documentReaders, 
				documentWriters, 
				new HashMap<String, FilterFactory>(),
				localizationService, 
				tempFolder
				);

		this.sockJSServer = sockJSServer;
	}

	private JsonObject createSockJsConfig(String path) {
		JsonObject config = new JsonObject().putString("prefix", path);
		return config;
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.sockjsgateway.SockJsGateway#stop()
	 */
	@Override
	public void stop() {

		if(sockJSServer != null){
			sockJSServer.close();
		}
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.BasicGateway#registerFilterChain(java.lang.String, java.lang.String, org.cradle.platform.httpgateway.filter.ServiceFilterConfig, org.cradle.platform.httpgateway.filter.Filter)
	 */
	@Override
	protected void registerFilterChain(String method, String path,
			ServiceFilterConfig serviceConfig, final Filter firstFilter) {

		JsonObject config = createSockJsConfig(path);

		sockJSServer.installApp(config, new Handler<SockJSSocket>(){

			@Override
			public void handle(SockJSSocket socket) {

				VertxSockJsAdapter httpAdapter = new VertxSockJsAdapter(socket);

				try {
					
					firstFilter.filter(httpAdapter);
					
				} catch (BadRequestException | UnauthorizedException
						| UnknownResourceException e) {
					
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
