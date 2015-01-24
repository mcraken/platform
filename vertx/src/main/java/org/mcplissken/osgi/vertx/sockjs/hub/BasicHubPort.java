/**
 * 
 */
package org.mcplissken.osgi.vertx.sockjs.hub;


/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 23, 2014
 */
public abstract class BasicHubPort  {

	protected HubSockJsAgent agent;
	
	protected String portName;

	public BasicHubPort(HubSockJsAgent agent, String portName) {
		
		this.agent = agent;
		
		this.portName = portName;
	}

	protected void portResponse(Object document){
		
		agent.documentPortResponse(portName, document);
	}
	
	protected void portResponse(byte[] buff) {
		
		agent.rawPortResponse(buff);
	}
}