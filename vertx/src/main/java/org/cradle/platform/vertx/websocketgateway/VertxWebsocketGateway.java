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
import org.cradle.platform.eventbus.spi.EventbusHandler;
import org.cradle.platform.eventbus.spi.TypeEventbusHandler;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.filter.FilterInvokationHandler;
import org.cradle.platform.httpgateway.filter.PrecedenceFilter;
import org.cradle.platform.httpgateway.spi.GatewayResponse;
import org.cradle.platform.httpgateway.spi.ResponseObject;
import org.cradle.platform.httpgateway.spi.handler.AsyncIOtHttpHandler;
import org.cradle.platform.httpgateway.spi.handler.BasicHttpHandler;
import org.cradle.platform.httpgateway.spi.registration.FilterRegistartionPrinicipal;
import org.cradle.platform.spi.BasicCradleProvider;
import org.cradle.platform.spi.RegistrationAgent;
import org.cradle.platform.spi.RegistrationPrincipal;
import org.cradle.platform.vertx.HttpFilterAgent;
import org.cradle.platform.vertx.eventbus.VertxEventbusService;
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

	private VertxEventbusService eventbus;

	private RegistrationAgent<BasicHttpHandler, WebSocket> httpHandlerAgent = new RegistrationAgent<BasicHttpHandler, WebSocket>(){

		@Override
		public void register(WebSocket annotation, BasicHttpHandler handler) {

			handler.setLocalizationService(localizationService);

			registerHttpHandler(annotation, handler);
		}

	};

	public VertxWebsocketGateway(
			Map<String, DocumentReader> documentReaders,
			Map<String, DocumentWriter> documentWriters,
			LocalizationService localizationService,
			VertxEventbusService eventbus,
			SockJSServer sockJSServer) {

		this.filters = HashMultimap.create();

		principals = new RegistrationPrincipal<?, ?>[]{ 
				new WebsocketHandlerRegistrationPrinicipal(httpHandlerAgent),
				new FilterRegistartionPrinicipal(new HttpFilterAgent(filters))
		};

		this.documentReaders = documentReaders;
		this.documentWriters = documentWriters;
		this.localizationService = localizationService;
		this.eventbus = eventbus;

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

	public void registerHttpHandler(final WebSocket annotation, final BasicHttpHandler handler) {

		JsonObject config = createSockJsConfig(annotation.path());

		final FilterInvokationHandler invokationHandler = new FilterInvokationHandler(annotation.path(), documentReaders, documentWriters, handler, filters);

		sockJSServer.installApp(config, new Handler<SockJSSocket>(){

			@Override
			public void handle(SockJSSocket socket) {

				final VertxWebsocketAdapter httpAdapter = new VertxWebsocketAdapter(socket);

				try {

					if(annotation.type() == WebSocket.Type.BROADCAST){

						registerSocketEventbusHandler(annotation, handler,
								httpAdapter);
					}

					invokationHandler.filter(httpAdapter);

				} catch (HttpException e) {

					socket.close();
				}
			}



		});

	}

	/**
	 * @param annotation
	 * @param handler
	 * @param httpAdapter
	 */
	private void registerSocketEventbusHandler(
			final WebSocket annotation, final BasicHttpHandler handler,
			final VertxWebsocketAdapter httpAdapter) {

		Class<?> documentType = ((AsyncIOtHttpHandler) handler).getDocumentType();

		if(documentType == String.class){
			eventbus.subscribe("ws:" + annotation.path(), new EventbusHandler() {

				@Override
				public void recieve(String message) {

					transmitEventbusMessage(handler, httpAdapter, message);
				}
			});

		} else{

			eventbus.subscribe("ws:" + annotation.path(), new TypeEventbusHandler(documentType) {

				@Override
				protected <T> void recieve(T message) {

					transmitEventbusMessage(handler, httpAdapter, message);
				}


			});
		}
	}

	/** 
	 * @param handler
	 * @param httpAdapter
	 * @param message
	 */
	private <T> void transmitEventbusMessage(
			final BasicHttpHandler handler,
			final VertxWebsocketAdapter httpAdapter, T message) {

		GatewayResponse response = new GatewayResponse(httpAdapter, documentWriters);

		ResponseObject responseObject = new ResponseObject(message);

		handler.writeServiceResponse(httpAdapter, response, responseObject, false);
	}

}
