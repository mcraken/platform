/**
 * 
 */
package org.mcplissken.osgi.vertx.sockjs.hub;

import java.util.Map;

import org.mcplissken.osgi.vertx.sockjs.SockJsAgent;
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
	 * org.mcplissken.osgi.vertx.sockjs.SockJsAgent#socketMessageReceived(byte[])
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
