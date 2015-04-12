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
package org.cradle.osgi.vertx.sockjs.hub;

import java.util.Map;

import org.cradle.osgi.vertx.sockjs.SockJsAgent;
import org.vertx.java.core.json.JsonObject;

import scala.collection.mutable.HashMap;

/**
 * @author Sherief Shawky
 * @email mcrakens@gmail.com
 * @date Sep 21, 2014
 */
public class HubSockJsAgent extends SockJsAgent {

	private Map<String, HubPort> ports;

	/**
	 * @param ports
	 *            the ports to set
	 */
	public void setPorts(Map<String, HubPort> ports) {
		this.ports = ports;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.cradle.osgi.vertx.sockjs.SockJsAgent#socketMessageReceived(byte[])
	 */
	@Override
	protected void socketMessageReceived(byte[] message) {

		JsonObject joMessage = new JsonObject(new String(message));

		String[] targetPorts = joMessage.getFieldNames().toArray(new String[] {});

		for (String port : targetPorts) {

			HubPort hubPort = ports.get(port);

			hubPort.portRequest(joMessage.getObject(port).toString());
		}

	}

	protected synchronized void documentPortResponse(String port, Object document) {

		HashMap<String, Object> portResponse = new HashMap<>();

		portResponse.put(port, document);

		writeDocument(document);
	}

	protected synchronized void rawPortResponse(byte[] buff) {
		writeMessage(buff);
	}

}
