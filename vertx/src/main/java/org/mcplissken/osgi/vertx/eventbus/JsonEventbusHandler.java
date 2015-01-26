/**
 * 
 */
package org.mcplissken.osgi.vertx.eventbus;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 22, 2014
 */
public abstract class JsonEventbusHandler implements Handler<Message<JsonObject>>{

	protected VertxEventBusService eventBusService;

	/**
	 * @param eventBusService the eventBusService to set
	 */
	public void setEventBusService(VertxEventBusService eventBusService) {
		this.eventBusService = eventBusService;
	}

	/* (non-Javadoc)
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(Message<JsonObject> event) {
		
		String address = event.address();
		
		JsonObject body = event.body();
		
		recieve(address, body);
	}
	
	
	protected void subscribe(String address){
		
		eventBusService.subscribe(address, this);
	}
	
	protected void unsubscribe(String address){
		
		eventBusService.unsubscribe(address, this);
	}
	
	public abstract void recieve(String address, JsonObject message);

}
