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

import java.util.Map;

import org.cradle.localization.LocalizationService;
import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.httpgateway.HttpMethod;
import org.cradle.platform.httpgateway.filter.FilterInvokationHandler;
import org.cradle.platform.httpgateway.filter.PrecedenceFilter;
import org.cradle.platform.httpgateway.spi.handler.BasicHttpHandler;
import org.cradle.platform.httpgateway.spi.registration.FilterRegistartionPrinicipal;
import org.cradle.platform.httpgateway.spi.registration.HttpHandlerResgisterationPrinicipal;
import org.cradle.platform.spi.BasicCradleProvider;
import org.cradle.platform.spi.RegistrationAgent;
import org.cradle.platform.spi.RegistrationPrincipal;
import org.cradle.platform.vertx.HttpFilterAgent;
import org.cradle.platform.vertx.handlers.FileRequestHandler;
import org.cradle.platform.vertx.handlers.HttpInvokationHandler;
import org.cradle.reporting.SystemReportingService;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 2, 2014
 */
public class VertxHttpGateway extends BasicCradleProvider {

	private HttpServer httpServer;
	private RemovableRouteMatcher routeMatcher;
	private Multimap<String, PrecedenceFilter> filters;
	
	
	private String fileRoot;
	private String webRoot;
	private String host;
	private int port;
	
	private Map<String, DocumentReader> documentReaders;
	private Map<String, DocumentWriter> documentWriters;
	
	private LocalizationService localizationService;
	private SystemReportingService reportingService;
	
	private RegistrationAgent<BasicHttpHandler, HttpMethod> httpHandlerAgent = new RegistrationAgent<BasicHttpHandler, HttpMethod>() {

		@Override
		public void register(HttpMethod annotation, BasicHttpHandler handler) {
			
			handler.setLocalizationService(localizationService);
			
			registerHttpHandler(
					annotation, 
					new FilterInvokationHandler(annotation.path(), documentReaders, documentWriters, handler, filters));
		}
	
	};
	
	public VertxHttpGateway(
			HttpServer httpServer,
			RemovableRouteMatcher routeMatcher,
			Map<String, DocumentReader> documentReaders,
			Map<String, DocumentWriter> documentWriters, 
			String fileRoot,
			String fileTemp,
			String webRoot, 
			String host,
			int port,
			LocalizationService localizationService,
			SystemReportingService reportingService) {

		this.filters = HashMultimap.create();
		
		principals = new RegistrationPrincipal<?, ?>[]{ 
				new HttpHandlerResgisterationPrinicipal(fileTemp, httpHandlerAgent),
				new FilterRegistartionPrinicipal(new HttpFilterAgent(filters))
		};
		
		this.fileRoot = fileRoot;
		this.webRoot = webRoot;
		this.host = host;
		this.port = port;
		this.httpServer = httpServer;
		this.routeMatcher = routeMatcher;
		this.documentReaders = documentReaders;
		this.documentWriters = documentWriters;
		this.reportingService = reportingService;
		this.localizationService = localizationService;
		
	}

	public void start(){

		try {

			registerStaticFileHandler();

			httpServer.listen(port, host);

		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	private void registerStaticFileHandler() {

		FileRequestHandler fileRequestHandler = new FileRequestHandler(fileRoot, webRoot);

		routeMatcher.get("^" + webRoot + "/.*", fileRequestHandler);
	}

	public void stop(){

		httpServer.close();

	}

	protected void unregisterHttpHandler(String method, String path) {

		routeMatcher.removeHandler(method, path);

	}

	protected void registerHttpHandler(HttpMethod annotation,
			FilterInvokationHandler invokationHandler) {

		Handler<HttpServerRequest> requestHandler = new HttpInvokationHandler(invokationHandler, reportingService);

		String method =  annotation.method().getValue();

		String path = annotation.path();

		switch (method) {

		case "GET":
			routeMatcher.get(path, requestHandler);
			break;
		case "POST":
			routeMatcher.post(path, requestHandler);
			break;
		case "PUT":
			routeMatcher.put(path, requestHandler);
			break;
		case "DELETE":
			routeMatcher.delete(path, requestHandler);
			break;
		}
	}

}
