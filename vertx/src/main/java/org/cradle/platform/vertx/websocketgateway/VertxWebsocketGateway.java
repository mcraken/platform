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
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.filter.FilterInvokationHandler;
import org.cradle.platform.httpgateway.filter.PrecedenceFilter;
import org.cradle.platform.httpgateway.spi.handler.BasicHttpHandler;
import org.cradle.platform.httpgateway.spi.registration.FilterRegistartionPrinicipal;
import org.cradle.platform.spi.BasicCradleProvider;
import org.cradle.platform.spi.RegistrationAgent;
import org.cradle.platform.spi.RegistrationPrincipal;
import org.cradle.platform.vertx.HttpFilterAgent;
import org.cradle.platform.websocketgateway.WebSocket;
import org.cradle.platform.websocketgateway.spi.WebsocketHandlerRegistrationPrinicipal;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 19, 2015
 */
public class VertxWebsocketGateway extends BasicCradleProvider  {

	private Map<String, DocumentReader> documentReaders;
	private Map<String, DocumentWriter> documentWriters;
	
	private LocalizationService localizationService;
	
	private SockJSServer sockJSServer;
	private Multimap<String, PrecedenceFilter> filters;

	private RegistrationAgent<BasicHttpHandler, WebSocket> httpHandlerAgent = new RegistrationAgent<BasicHttpHandler, WebSocket>(){

		@Override
		public void register(WebSocket annotation, BasicHttpHandler handler) {
			
			handler.setLocalizationService(localizationService);
			
			registerHttpHandler(annotation, 
					new FilterInvokationHandler(annotation.path(), documentReaders, documentWriters, handler, filters));
		}
		
	};
	
	public VertxWebsocketGateway(
			Map<String, DocumentReader> documentReaders,
			Map<String, DocumentWriter> documentWriters,
			LocalizationService localizationService, 
			SockJSServer sockJSServer) {
		
		this.filters = HashMultimap.create();
		
		principals = new RegistrationPrincipal<?, ?>[]{ 
				new WebsocketHandlerRegistrationPrinicipal(httpHandlerAgent),
				new FilterRegistartionPrinicipal(new HttpFilterAgent(filters))
		};
		
		this.documentReaders = documentReaders;
		this.documentWriters = documentWriters;
		this.localizationService = localizationService;
		
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

	public void registerHttpHandler(WebSocket annotation, final FilterInvokationHandler invokationHandler) {

		JsonObject config = createSockJsConfig(annotation.path());

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

}
