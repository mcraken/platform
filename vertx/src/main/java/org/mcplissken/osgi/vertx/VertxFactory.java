/**
 * 
 */
package org.mcplissken.osgi.vertx;

import java.util.HashMap;
import java.util.Map;

import org.mcplissken.gateway.HttpWebService;
import org.mcplissken.gateway.restful.client.AsynchronousReadingHttpClient;
import org.mcplissken.gateway.restful.document.DocumentReader;
import org.mcplissken.gateway.restful.document.DocumentWriter;
import org.mcplissken.gateway.restful.filter.RESTfulFilterFactory;
import org.mcplissken.gateway.vertx.VertxGateway;
import org.mcplissken.gateway.vertx.restful.client.VertxReadingHttpClient;
import org.mcplissken.localization.LocalizationService;
import org.mcplissken.osgi.vertx.eventbus.JsonEventbusHandler;
import org.mcplissken.osgi.vertx.eventbus.TextEventBusHandler;
import org.mcplissken.osgi.vertx.eventbus.VertxEventBusService;
import org.mcplissken.reporting.SystemReportingService;
import org.mcplissken.repository.ModelRepository;
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
public class VertxFactory implements VertxService, VertxEventBusService, HttpWebService{

	private static final String VERTX_HOME = "vertx.home";
	private static final String VERTX_MODS = "vertx.mods";
	private static final String CLUSTER_FACTORY = "vertx.clusterManagerFactory";

	private PlatformManager platform;
	private Vertx vertx;
	private EventBus eventBus;
	private VertxGateway vertxGateway;
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

	public void init(){

		createVertxEnviornmentVariables();
		
		platform = PlatformLocator.factory.createPlatformManager(port, host);
		
		vertx = platform.vertx();

		eventBus = vertx.eventBus();
		
	}
	
	
	public void destroy(){
		
		eventBus.close(new Handler<AsyncResult<Void>>() {

			@Override
			public void handle(AsyncResult<Void> event) {
				reportingService.info(this.getClass().getSimpleName(), SystemReportingService.CONSOLE, "Event bus closed");
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
	 * @see org.mcplissken.osgi.vertx.VertxService#vertxGateway(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.util.Map, java.util.Map)
	 */
	@Override
	public VertxGateway vertxGateway(
			String host, 
			int port, 
			String fileRoot,
			String webRoot, 
			String webRootRegEx,
			ModelRepository repository,
			String contentRoot,
			String contentRootRegEx,
			String oauthRootRegEx,
			Map<String, RESTfulFilterFactory> filtersFactoryMap
			) {
		
		if(vertxGateway == null){
			
			vertxGateway = new VertxGateway(
					vertx, 
					documentReaders, 
					documentWriters, 
					fileRoot, 
					fileSystemPath,
					webRoot, 
					webRootRegEx,
					repository,
					contentRoot,
					contentRootRegEx,
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
	 * @see org.mcplissken.osgi.vertx.VertxEventBusService#subscribe(java.lang.String, org.mcplissken.osgi.vertx.EventBusHandler)
	 */
	@Override
	public void subscribe(String address, TextEventBusHandler handler) {
		
		eventBus.registerHandler(address, handler);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.osgi.vertx.VertxEventBusService#unsubscribe(java.lang.String, org.mcplissken.osgi.vertx.EventBusHandler)
	 */
	@Override
	public void unsubscribe(String address, TextEventBusHandler handler) {

		eventBus.unregisterHandler(address, handler);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.osgi.vertx.VertxService#deployModule(java.lang.String, java.util.HashMap)
	 */
	@Override
	public void deployModule(String moduleName, Map<String, Object> configMap) {
		
		JsonObject config = new JsonObject(configMap);
		
		platform.deployModule(moduleName, config, 1, new DeploymentReporter(moduleName, reportingService));
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.osgi.vertx.eventbus.VertxEventBusService#subscribe(java.lang.String, org.mcplissken.osgi.vertx.eventbus.JsonEventbusHandler)
	 */
	@Override
	public void subscribe(String address, JsonEventbusHandler handler) {
		eventBus.registerHandler(address, handler);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.osgi.vertx.eventbus.VertxEventBusService#unsubscribe(java.lang.String, org.mcplissken.osgi.vertx.eventbus.JsonEventbusHandler)
	 */
	@Override
	public void unsubscribe(String address, JsonEventbusHandler handler) {
		eventBus.unregisterHandler(address, handler);
		
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.osgi.vertx.eventbus.VertxEventBusService#publish(java.lang.String, org.vertx.java.core.json.JsonObject)
	 */
	@Override
	public void publish(String address, String message) {
		eventBus.publish(address, message);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.osgi.vertx.eventbus.VertxEventBusService#publish(java.lang.String, java.lang.Object, java.lang.String)
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
	 * @see org.mcplissken.gateway.HttpGateway#createReadingHttpClient()
	 */
	@Override
	public AsynchronousReadingHttpClient createReadingHttpClient() {
		
		HttpClient httpClient = vertx.createHttpClient();
		
		FileSystem fileSystem = vertx.fileSystem();
		
		VertxReadingHttpClient vertxReadingHttpClient = new VertxReadingHttpClient(httpClient, documentReaders, fileSystemPath, fileSystem);
		
		return vertxReadingHttpClient;
	}

}
