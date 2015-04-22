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

import java.lang.annotation.Annotation;
import java.util.Map;

import org.cradle.localization.LocalizationService;
import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.httpgateway.HttpMethod;
import org.cradle.platform.httpgateway.filter.Filter;
import org.cradle.platform.httpgateway.filter.FilterFactory;
import org.cradle.platform.httpgateway.filter.ServiceFilterConfig;
import org.cradle.platform.httpgateway.spi.IOHttpHandlerRegistrationPrincipal;
import org.cradle.platform.httpgateway.spi.MultipartHttpHandlerRegistrationPrincipal;
import org.cradle.platform.httpgateway.spi.OutputHttpHandlerResgistrationPrincipal;
import org.cradle.platform.spi.BasicHttpCradleGateway;
import org.cradle.platform.vertx.handlers.FileRequestHandler;
import org.cradle.platform.vertx.handlers.FilterInvokationHandler;
import org.cradle.reporting.SystemReportingService;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 2, 2014
 */
public class VertxHttpGateway extends BasicHttpCradleGateway {

	private HttpServer httpServer;
	private RemovableRouteMatcher routeMatcher;

	private String fileRoot;
	private String webRoot;
	private String host;
	private int port;

	private SystemReportingService reportingService;

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
			Map<String, FilterFactory> filtersFactoryMap,
			LocalizationService localizationService,
			SystemReportingService reportingService) {
		
		super(
				new OutputHttpHandlerResgistrationPrincipal(
						new IOHttpHandlerRegistrationPrincipal(
								new MultipartHttpHandlerRegistrationPrincipal(null, fileTemp)
								)
						),
				documentReaders, 
				documentWriters, 
				filtersFactoryMap, 
				localizationService);

		this.fileRoot = fileRoot;
		this.webRoot = webRoot;
		this.host = host;
		this.port = port;
		this.httpServer = httpServer;
		this.routeMatcher = routeMatcher;
		this.reportingService = reportingService;
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

	/* (non-Javadoc)
	 * @see org.cradle.osgi.gateway.HttpGateway#unregisterRESTfulHandler(io.netty.handler.codec.http.HttpMethod, java.lang.String)
	 */
	@Override
	protected void unregisterHttpHandler(String method, String path) {

		routeMatcher.removeHandler(method, path);

	}

	protected void registerFilterChain(Annotation annotation,
			ServiceFilterConfig serviceConfig, Filter firstFilter) {
		
		Handler<HttpServerRequest> requestHandler = new FilterInvokationHandler(firstFilter, reportingService);
		
		String method = ((HttpMethod) annotation).method().getValue();
		
		String path = ((HttpMethod) annotation).path();
		
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
