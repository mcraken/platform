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
package org.cradle.platform.vertx.sockjsgateway;

import java.util.Map;

import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.sockjsgateway.SockJsAgentFactory;
import org.cradle.platform.sockjsgateway.SockJsGateway;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 19, 2015
 */
public class VertxSockJsGateway implements SockJsGateway {

	private SockJSServer sockJSServer;
	protected Map<String, DocumentWriter> documentWriters;

	/**
	 * @param sockJSServer
	 * @param documentWriters
	 */
	public VertxSockJsGateway(SockJSServer sockJSServer,
			Map<String, DocumentWriter> documentWriters) {
		this.sockJSServer = sockJSServer;
		this.documentWriters = documentWriters;
	}

	private JsonObject createSockJsConfig(String path) {
		JsonObject config = new JsonObject().putString("prefix", path);
		return config;
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.SockJsGateway#registerAgentApplication(java.lang.String, java.lang.Object)
	 */
	@Override
	public void registerAgentApplication(String path, String contentType, SockJsAgentFactory factory) {

		JsonObject config = createSockJsConfig(path);

		DocumentWriter writer = documentWriters.get(contentType);

		sockJSServer.installApp(config, new VertxSockJsApplication(factory, writer));

	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.sockjsgateway.SockJsGateway#stop()
	 */
	@Override
	public void stop() {

		if(sockJSServer != null){
			sockJSServer.close();
		}
	}

}
