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
package org.cradle.platform.vertx;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.cradle.localization.LocalizationService;
import org.cradle.platform.CradlePlatform;
import org.cradle.platform.gateway.HttpGateway;
import org.cradle.platform.gateway.HttpWebService;
import org.cradle.platform.gateway.restful.client.AsynchronousReadingHttpClient;
import org.cradle.platform.gateway.restful.document.DocumentReader;
import org.cradle.platform.gateway.restful.document.DocumentWriter;
import org.cradle.platform.gateway.restful.document.JsonDocumentReaderWriter;
import org.cradle.platform.gateway.restful.filter.RESTfulFilterFactory;
import org.cradle.platform.gateway.vertx.VertxHttpGateway;
import org.cradle.platform.gateway.vertx.restful.client.VertxReadingHttpClient;
import org.cradle.platform.vertx.eventbus.JsonEventbusHandler;
import org.cradle.platform.vertx.eventbus.TextEventBusHandler;
import org.cradle.platform.vertx.eventbus.VertxEventBusService;
import org.cradle.reporting.SystemReportingService;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.file.FileSystem;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.PlatformLocator;
import org.vertx.java.platform.PlatformManager;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 14, 2014
 */
public class VertxCradlePlatform implements CradlePlatform, VertxEventBusService, HttpWebService{

	private static final String VERTX_HOME = "vertx.home";
	private static final String VERTX_MODS = "vertx.mods";
	private static final String CLUSTER_FACTORY = "vertx.clusterManagerFactory";

	private static VertxCradlePlatform cradlePlatform;

	private PlatformManager platform;
	private Vertx vertx;
	private EventBus eventBus;
	private VertxHttpGateway vertxGateway;
	private String homePath;
	private String modsPath;
	private String fileSystemPath;
	private String clusterManagerFactory;
	private String host;
	private int port;
	private LocalizationService localizationService;
	private SystemReportingService reportingService;
	private Map<String, DocumentReader> documentReaders;
	private Map<String, DocumentWriter> documentWriters;


	public static VertxCradlePlatform createDefaultInstance(){

		if(cradlePlatform != null){

			cradlePlatform.destroy();

			cradlePlatform = null;
		}

		JsonDocumentReaderWriter jdrw = new JsonDocumentReaderWriter();
		jdrw.init();

		Hashtable<String, DocumentReader> documentReaders = new Hashtable<String, DocumentReader>();
		documentReaders.put("application/json", jdrw);

		Hashtable<String, DocumentWriter> documentWriters = new Hashtable<String, DocumentWriter>();
		documentWriters.put("application/json", jdrw);

		cradlePlatform = new VertxCradlePlatform(
				"vertx/home", 
				"vertx/mods", 
				"vertx_temp", 
				"org.vertx.java.spi.cluster.impl.hazelcast.HazelcastClusterManagerFactory",
				"localhost", 
				9090, 
				documentReaders, 
				documentWriters,
				null,
				null
				);

		cradlePlatform.init();

		return cradlePlatform;
	}


	/**
	 * @param homePath
	 * @param modsPath
	 * @param fileSystemPath
	 * @param clusterManagerFactory
	 * @param host
	 * @param port
	 * @param documentReaders
	 * @param documentWriters
	 */
	public VertxCradlePlatform(
			String homePath, 
			String modsPath,
			String fileSystemPath, 
			String clusterManagerFactory, 
			String host,
			int port, 
			Map<String, DocumentReader> documentReaders,
			Map<String, DocumentWriter> documentWriters,
			LocalizationService localizationService,
			SystemReportingService reportingService
			) {

		this.homePath = homePath;
		this.modsPath = modsPath;
		this.fileSystemPath = fileSystemPath;
		this.clusterManagerFactory = clusterManagerFactory;
		this.host = host;
		this.port = port;
		this.documentReaders = documentReaders;
		this.documentWriters = documentWriters;
		this.localizationService = localizationService;
		this.reportingService = reportingService;
	}


	/**
	 * @param fileSystemPath the fileSystemPath to set
	 */
	public void setFileSystemPath(String fileSystemPath) {
		this.fileSystemPath = fileSystemPath;
	}

	/**
	 * @param localizationService the localizationService to set
	 */
	public void setLocalizationService(LocalizationService localizationService) {
		this.localizationService = localizationService;
	}

	public void setReportingService(SystemReportingService reportingService) {
		this.reportingService = reportingService;
	}

	public void setDocumentReaders(Map<String, DocumentReader> documentReaders) {
		this.documentReaders = documentReaders;
	}


	public void setDocumentWriters(Map<String, DocumentWriter> documentWriters) {
		this.documentWriters = documentWriters;
	}

	/**
	 * @param homePath the homePath to set
	 */
	public void setHomePath(String homePath) {
		this.homePath = homePath;
	}

	/**
	 * @param modsPath the modsPath to set
	 */
	public void setModsPath(String modsPath) {
		this.modsPath = modsPath;
	}

	/**
	 * @param clusterManagerFactory the clusterManagerFactory to set
	 */
	public void setClusterManagerFactory(String clusterManagerFactory) {
		this.clusterManagerFactory = clusterManagerFactory;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	private void init(){

		createVertxEnviornmentVariables();

		platform = PlatformLocator.factory.createPlatformManager(port, host);

		vertx = platform.vertx();

		eventBus = vertx.eventBus();

	}


	private void destroy(){

		eventBus.close(new Handler<AsyncResult<Void>>() {

			@Override
			public void handle(AsyncResult<Void> event) {
				
				if(reportingService != null){
					reportingService.info(this.getClass().getSimpleName(), SystemReportingService.CONSOLE, "Event bus closed");
				}
			}
		});

		vertxGateway.stop();

		platform.stop();
	}

	private void createVertxEnviornmentVariables() {

		System.setProperty(VERTX_HOME, homePath);

		System.setProperty(VERTX_MODS, modsPath);

		System.setProperty(CLUSTER_FACTORY, clusterManagerFactory);
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.CradlePlatform#gateway()
	 */
	@Override
	public HttpGateway gateway() {

		if(vertxGateway == null){

			vertxGateway = new VertxHttpGateway(
					vertx, 
					documentReaders, 
					documentWriters
					);
			
			vertxGateway.start();
		}

		return vertxGateway;
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.VertxService#vertxGateway(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.util.Map, java.util.Map)
	 */
	@Override
	public HttpGateway gateway(
			String host, 
			int port, 
			String fileRoot,
			String webRoot, 
			Map<String, RESTfulFilterFactory> filtersFactoryMap
			) {

		if(vertxGateway == null){

			vertxGateway = new VertxHttpGateway(
					vertx, 
					documentReaders, 
					documentWriters, 
					fileRoot, 
					fileSystemPath,
					webRoot, 
					host, 
					port,
					filtersFactoryMap,
					localizationService,
					reportingService);

			vertxGateway.start();
		}

		return vertxGateway;
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.VertxEventBusService#subscribe(java.lang.String, org.cradle.osgi.vertx.EventBusHandler)
	 */
	@Override
	public void subscribe(String address, TextEventBusHandler handler) {

		eventBus.registerHandler(address, handler);
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.VertxEventBusService#unsubscribe(java.lang.String, org.cradle.osgi.vertx.EventBusHandler)
	 */
	@Override
	public void unsubscribe(String address, TextEventBusHandler handler) {

		eventBus.unregisterHandler(address, handler);
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.VertxService#deployModule(java.lang.String, java.util.HashMap)
	 */
	public void deployModule(String moduleName, Map<String, Object> configMap) {

		JsonObject config = new JsonObject(configMap);

		platform.deployModule(moduleName, config, 1, new DeploymentReporter(moduleName, reportingService));
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.eventbus.VertxEventBusService#subscribe(java.lang.String, org.cradle.osgi.vertx.eventbus.JsonEventbusHandler)
	 */
	@Override
	public void subscribe(String address, JsonEventbusHandler handler) {
		eventBus.registerHandler(address, handler);
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.eventbus.VertxEventBusService#unsubscribe(java.lang.String, org.cradle.osgi.vertx.eventbus.JsonEventbusHandler)
	 */
	@Override
	public void unsubscribe(String address, JsonEventbusHandler handler) {
		eventBus.unregisterHandler(address, handler);

	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.eventbus.VertxEventBusService#publish(java.lang.String, org.vertx.java.core.json.JsonObject)
	 */
	@Override
	public void publish(String address, String message) {
		eventBus.publish(address, message);
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.eventbus.VertxEventBusService#publish(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public void publish(String address, Object message, String contentType) {

		DocumentWriter documentWriter = documentWriters.get(contentType);

		StringBuffer output = new StringBuffer();

		Map<String, Object> eventBusMessage = new HashMap<>();

		eventBusMessage.put(address, message);

		documentWriter.write(eventBusMessage, output);

		eventBus.publish(address, output.toString());
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.HttpGateway#createReadingHttpClient()
	 */
	@Override
	public AsynchronousReadingHttpClient createReadingHttpClient() {

		HttpClient httpClient = vertx.createHttpClient();

		FileSystem fileSystem = vertx.fileSystem();

		VertxReadingHttpClient vertxReadingHttpClient = new VertxReadingHttpClient(httpClient, documentReaders, fileSystemPath, fileSystem);

		return vertxReadingHttpClient;
	}


	/* (non-Javadoc)
	 * @see org.cradle.platform.CradlePlatform#shutdown()
	 */
	@Override
	public void shutdown() {
		destroy();
	}

}
