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

import org.cradle.platform.spi.CradleProvider;
import org.cradle.platform.vertx.VertxCradlePlatform;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 15, 2015
 */
@RunWith(MockitoJUnitRunner.class)
public class StandaloneGatewayTest {

	private CradleProvider httpGateway;
	private CradleProvider websocketGateway;
	private VertxCradlePlatform platform;

	@Before
	public void setup(){

		platform = VertxCradlePlatform.createDefaultInstance();

		httpGateway = platform.httpGateway();
		
		websocketGateway = platform.websocketGateway();

	}

	@Test
	public void testHandlerResgistration() throws InterruptedException{

		HelloWorldController controller = new HelloWorldController();
		
		httpGateway.registerController(controller);
		
		websocketGateway.registerController(controller);
		
		Thread.sleep(5 * 60 * 1000);
	}

	@After
	public void teardown(){

		platform.shutdown();
	}
}
