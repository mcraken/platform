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

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.cradle.localization.LocalizationService;
import org.cradle.platform.CradlePlatform;
import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.document.JsonDocumentReaderWriter;
import org.cradle.platform.eventbus.CradleEventbus;
import org.cradle.platform.httpgateway.HttpWebService;
import org.cradle.platform.httpgateway.client.AsynchronousReadingHttpClient;
import org.cradle.platform.httpgateway.filter.FilterFactory;
import org.cradle.platform.spi.CradleGateway;
import org.cradle.platform.vertx.eventbus.VertxEventbusService;
import org.cradle.platform.vertx.httpgateway.RemovableRouteMatcher;
import org.cradle.platform.vertx.httpgateway.VertxHttpGateway;
import org.cradle.platform.vertx.httpgateway.client.VertxReadingHttpClient;
import org.cradle.platform.vertx.sockjsgateway.VertxSockJsGateway;
import org.cradle.reporting.SystemReportingService;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.file.FileSystem;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.platform.PlatformLocator;
import org.vertx.java.platform.PlatformManager;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 14, 2014
 */
public class VertxCradlePlatform implements CradlePlatform, HttpWebService{

	private static final String VERTX_HOME = "vertx.home";
	private static final String VERTX_MODS = "vertx.mods";
	private static final String VERTX_TEMP = "vertx_temp";
	private static final String CLUSTER_FACTORY = "vertx.clusterManagerFactory";

	private static VertxCradlePlatform cradlePlatform;

	private PlatformManager platform;
	private Vertx vertx;
	private EventBus eventBus;
	private VertxHttpGateway vertxHttpGateway;
	private VertxSockJsGateway sockJsGateway;
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
	private VertxEventbusService eventbusSevice;

	private HttpServer httpServer;
	private SockJSServer sockJSServer;
	private RemovableRouteMatcher routeMatcher;


	public static VertxCradlePlatform createDefaultInstance(){

		if(cradlePlatform != null){

			cradlePlatform.shutdown();

			cradlePlatform = null;
		}

		JsonDocumentReaderWriter jdrw = new JsonDocumentReaderWriter();
		jdrw.init();

		Hashtable<String, DocumentReader> documentReaders = new Hashtable<String, DocumentReader>();
		documentReaders.put("application/json", jdrw);

		Hashtable<String, DocumentWriter> documentWriters = new Hashtable<String, DocumentWriter>();
		documentWriters.put("application/json", jdrw);
		
		File tempFolder = new File(VERTX_TEMP);
		
		if(!tempFolder.exists() && !tempFolder.mkdir()){
			
			throw new RuntimeException("Could not create vertx temp folder");
		}
		
		cradlePlatform = new VertxCradlePlatform(
				"vertx/home", 
				"vertx/mods", 
				VERTX_TEMP, 
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

		routeMatcher = new RemovableRouteMatcher();

		httpServer = vertx.createHttpServer();

		httpServer.requestHandler(routeMatcher);

		sockJSServer = vertx.createSockJSServer(httpServer);

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
	public CradleGateway httpGateway() {

		if(vertxHttpGateway == null){

			vertxHttpGateway = new VertxHttpGateway(
					httpServer,
					routeMatcher,
					documentReaders, 
					documentWriters,
					"root",
					VERTX_TEMP,
					"/cradle",
					"localhost",
					8080,
					new HashMap<String, FilterFactory>(),
					localizationService,
					reportingService
					);

			vertxHttpGateway.start();
		}

		return vertxHttpGateway;
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.VertxService#vertxGateway(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.util.Map, java.util.Map)
	 */
	@Override
	public CradleGateway httpGateway(
			String host, 
			int port, 
			String fileRoot,
			String webRoot, 
			Map<String, FilterFactory> filtersFactoryMap
			) {

		if(vertxHttpGateway == null){

			vertxHttpGateway = new VertxHttpGateway(
					httpServer,
					routeMatcher,
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

			vertxHttpGateway.start();
		}

		return vertxHttpGateway;
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.CradlePlatform#eventbus()
	 */
	@Override
	public CradleEventbus eventbus() {

		if(eventbusSevice == null){
			eventbusSevice = new VertxEventbusService(eventBus, documentWriters, reportingService);
		}

		return eventbusSevice;
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.CradlePlatform#sockJsGateway()
	 */
	@Override
	public CradleGateway sockJsGateway() {

		if(sockJsGateway == null){

			sockJsGateway = new VertxSockJsGateway(
					documentReaders, 
					documentWriters, 
					localizationService,  
					sockJSServer);
		}

		return sockJsGateway;
	}

	/* (non-Javadoc)
	 * @see org.cradle.osgi.vertx.VertxService#deployModule(java.lang.String, java.util.HashMap)
	 */
	public void deployModule(String moduleName, Map<String, Object> configMap) {

		JsonObject config = new JsonObject(configMap);

		platform.deployModule(moduleName, config, 1, new DeploymentReporter(moduleName, reportingService));
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

		if(eventbusSevice != null){
			eventbusSevice.shutdown();
		}

		if(sockJsGateway != null){
			sockJsGateway.stop();
		}

		if(vertxHttpGateway != null){
			vertxHttpGateway.stop();
		}

		platform.stop();
	}

}
