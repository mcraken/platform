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
package org.cradle.gateway.vertx.test;

import java.util.HashMap;
import java.util.Hashtable;

import org.cradle.gateway.restful.document.DocumentReader;
import org.cradle.gateway.restful.document.DocumentWriter;
import org.cradle.gateway.restful.document.JsonDocumentReaderWriter;
import org.cradle.gateway.restful.filter.RESTfulFilterFactory;
import org.cradle.gateway.vertx.VertxGateway;
import org.cradle.localization.LocalizationService;
import org.cradle.osgi.vertx.VertxFactory;
import org.cradle.reporting.SystemReportingService;
import org.cradle.repository.ModelRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 15, 2015
 */
@RunWith(MockitoJUnitRunner.class)
public class StandaloneGatewayTest {
	
	private @Mock ModelRepository repository;
	private @Mock SystemReportingService reportingService;
	private @Mock LocalizationService localizationService;
	
	private  VertxGateway vertxGateway;
	
	@Before
	public void setup(){
		
		JsonDocumentReaderWriter jdrw = new JsonDocumentReaderWriter();
		
		jdrw.init();
		
		Hashtable<String, DocumentReader> documentReaders = new Hashtable<String, DocumentReader>();
		documentReaders.put("application/json", jdrw);
		
		Hashtable<String, DocumentWriter> documentWriters = new Hashtable<String, DocumentWriter>();
		documentWriters.put("application/json", jdrw);
		
		VertxFactory vertxFactory = new VertxFactory();
		
		vertxFactory.setHomePath("vertx/home");
		vertxFactory.setModsPath("vertx/home");
		vertxFactory.setClusterManagerFactory("org.vertx.java.spi.cluster.impl.hazelcast.HazelcastClusterManagerFactory");
		vertxFactory.setHost("localhost");
		vertxFactory.setPort(9090);
		vertxFactory.setFileSystemPath("vertx_temp");
		vertxFactory.setLocalizationService(localizationService);
		vertxFactory.setReportingService(reportingService);
		vertxFactory.setDocumentReaders(documentReaders);
		vertxFactory.setDocumentWriters(documentWriters);
		
		vertxFactory.init();
		
		vertxGateway = vertxFactory.vertxGateway("localhost", 8080, "root", "/test", "^/test/.*", repository, "/forex/content/", "^/forex/content/.*", "", new HashMap<String, RESTfulFilterFactory>());
		
	}
	
	@Test
	public void testHandlerResgistration() throws InterruptedException{
		
		vertxGateway.registerHandler(new TestHttpHandler());
		
		Thread.sleep(2 * 60 * 1000);
	}
	
	@After
	public void teardown(){
		vertxGateway.stop();
	}
}
