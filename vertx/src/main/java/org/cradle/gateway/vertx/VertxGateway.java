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
package org.cradle.gateway.vertx;

import java.util.Map;

import org.cradle.gateway.BasicHttpHandler;
import org.cradle.gateway.HttpGateway;
import org.cradle.gateway.restful.document.DocumentReader;
import org.cradle.gateway.restful.document.DocumentWriter;
import org.cradle.gateway.restful.filter.RESTfulFilter;
import org.cradle.gateway.restful.filter.RESTfulFilterFactory;
import org.cradle.gateway.restful.filter.RESTfullServiceFilterConfig;
import org.cradle.gateway.vertx.restful.VertxRESTfulFilter;
import org.cradle.localization.LocalizationService;
import org.cradle.osgi.vertx.handlers.ContentRequestHandler;
import org.cradle.osgi.vertx.handlers.FileRequestHandler;
import org.cradle.osgi.vertx.handlers.FilterInvokationHandler;
import org.cradle.osgi.vertx.sockjs.SockJsApplication;
import org.cradle.osgi.vertx.sockjs.SockJsGateway;
import org.cradle.reporting.SystemReportingService;
import org.cradle.repository.ModelRepository;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 2, 2014
 */
public class VertxGateway implements HttpGateway, SockJsGateway {

	private HttpServer httpServer;
	private SockJSServer sockJSServer;
	private RemovableRouteMatcher routeMatcher;
	private Map<String, RESTfulFilterFactory> filtersFactoryMap;

	private Map<String, DocumentReader> documentReaders;
	private Map<String, DocumentWriter> documentWriters;
	private String fileTemp;
	private String fileRoot;
	private String webRoot;
	private String webRootRegEx;
	private ModelRepository repository;
	private String contentRoot;
	private String contentRootRegEx;
	private String host;
	private int port;
	
	private Vertx vertx;

	private LocalizationService localizationService;
	
	private SystemReportingService reportingService;

	public VertxGateway(
			Vertx vertx,
			Map<String, DocumentReader> documentReaders,
			Map<String, DocumentWriter> documentWriters, 
			String fileRoot,
			String fileTemp,
			String webRoot, 
			String webRootRegEx, 
			ModelRepository repository,
			String contentRoot, 
			String contentRootRegEx,
			String host,
			int port,
			Map<String, RESTfulFilterFactory> filtersFactoryMap,
			LocalizationService localizationService,
			SystemReportingService reportingService) {

		this.filtersFactoryMap = filtersFactoryMap;
		this.documentReaders = documentReaders;
		this.documentWriters = documentWriters;
		this.fileRoot = fileRoot;
		this.fileTemp = fileTemp;
		this.webRoot = webRoot;
		this.webRootRegEx = webRootRegEx;
		this.repository = repository;
		this.contentRoot = contentRoot;
		this.contentRootRegEx = contentRootRegEx;
		this.host = host;
		this.port = port;
		this.vertx = vertx;
		this.localizationService = localizationService;
		this.reportingService = reportingService;
	}

	public void start(){
		
		try {
			
			httpServer = vertx.createHttpServer();

			routeMatcher = new RemovableRouteMatcher();

			registerStaticFileHandler();

			registerContentHandler();
			
			httpServer.requestHandler(routeMatcher);

			sockJSServer = vertx.createSockJSServer(httpServer);

			httpServer.listen(port, host);

		} catch (Exception e) {
			
			reportingService.exception(getClass().getSimpleName(), SystemReportingService.CONSOLE, e);
		}
	}

	private void registerContentHandler() {
		
		ContentRequestHandler contentRequestHandler;

		contentRequestHandler = new ContentRequestHandler(repository, contentRoot,  reportingService);

		routeMatcher.get(contentRootRegEx, contentRequestHandler);
	}

	private void registerStaticFileHandler() {
		
		FileRequestHandler fileRequestHandler = new FileRequestHandler(fileRoot, webRoot);

		routeMatcher.get(webRootRegEx, fileRequestHandler);
	}

	public void stop(){

		sockJSServer.close();

		httpServer.close();

	}

	private void registerMatcherHandler(String url, Handler<HttpServerRequest> handler, String method, RESTfullServiceFilterConfig serviceConfig) {

		switch (method) {

		case "GET":
			routeMatcher.get(url, handler);
			break;
		case "POST":
			routeMatcher.post(url, handler);
			break;
		case "PUT":
			routeMatcher.put(url, handler);
			break;
		case "DELETE":
			routeMatcher.delete(url, handler);
			break;
		}
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.gateway.HttpGateway#unregisterRESTfulHandler(io.netty.handler.codec.http.HttpMethod, java.lang.String)
	 */
	@Override
	public void unregisterHttpHandler(String method, String path) {

		routeMatcher.removeHandler(method, path);

	}

	private JsonObject createSockJsConfig(String path) {
		JsonObject config = new JsonObject().putString("prefix", path);
		return config;
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.SockJsGateway#registerAgentApplication(java.lang.String, java.lang.Object)
	 */
	@Override
	public void registerAgentApplication(String path, String contentType, SockJsApplication app) {

		JsonObject config = createSockJsConfig(path);

		DocumentWriter writer = documentWriters.get(contentType);

		app.setWriter(writer);

		sockJSServer.installApp(config, app);

	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.HttpGateway#regitserHttpHandler(java.lang.String, java.lang.String, org.cradle.gateway.BasicHttpHandler, org.cradle.gateway.restful.filter.RESTfullServiceFilterConfig)
	 */
	@Override
	public void registerHttpHandler(
			String method, 
			String path,
			BasicHttpHandler httpHandler,
			RESTfullServiceFilterConfig serviceConfig) {
		
		VertxRESTfulFilter vertxFilter = new VertxRESTfulFilter(httpHandler, documentWriters, documentReaders, fileTemp);
		
		RESTfulFilter firstFilter = serviceConfig.buildChain(vertxFilter, filtersFactoryMap);
		
		httpHandler.setLocalizationService(localizationService);
		
		Handler<HttpServerRequest> requestHandler = new FilterInvokationHandler(firstFilter, reportingService);

		registerMatcherHandler(path, requestHandler, method, serviceConfig);
	}

}
