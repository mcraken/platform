/**
 * 
 */
package org.mcplissken.osgi.vertx.sockjs.eventbus;

import org.mcplissken.osgi.vertx.sockjs.SockJsAgent;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 7, 2014
 */
public abstract class EventbusSockJsAgent extends SockJsAgent{
	
	
	public abstract void eventBusMessageReceived(String message);
}
