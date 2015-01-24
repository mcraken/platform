/**
 * 
 */
package org.mcplissken.osgi.vertx.sockjs.eventbus;

import org.mcplissken.osgi.vertx.eventbus.TextEventBusHandler;
import org.mcplissken.osgi.vertx.eventbus.VertxEventBusService;
import org.mcplissken.osgi.vertx.sockjs.SockJsAgent;
import org.mcplissken.osgi.vertx.sockjs.SockJsApplication;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 7, 2014
 */
public abstract class EventbusSockJsApplication extends SockJsApplication{

	private VertxEventBusService eventBusService;
	private String address;

	
	public void setEventBusService(VertxEventBusService eventBusService) {
		this.eventBusService = eventBusService;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/* (non-Javadoc)
	 * @see com.mubasher.osgi.vertx.sockjs.BasicSockJsApplication#createAgent(org.vertx.java.core.sockjs.SockJSSocket)
	 */
	@Override
	protected SockJsAgent createAgent(){

		final EventbusSockJsAgent agent = createEventbusAgent();

		eventBusService.subscribe(address, new TextEventBusHandler() {
			
			@Override
			public void recieve(String message) {
				agent.eventBusMessageReceived(message);
			}
		});
		

		return agent;
	}

	/**
	 * @return
	 */
	protected abstract EventbusSockJsAgent createEventbusAgent();

}
