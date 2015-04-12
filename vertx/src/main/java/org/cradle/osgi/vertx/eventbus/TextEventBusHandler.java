/**
 * 
 */
package org.cradle.osgi.vertx.eventbus;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 14, 2014
 */
public abstract class TextEventBusHandler implements Handler<Message<String>>{
	
	protected VertxEventBusService eventBusService;

	/**
	 * @param eventBusService the eventBusService to set
	 */
	public void setEventBusService(VertxEventBusService eventBusService) {
		this.eventBusService = eventBusService;
	}

	protected void publish(String address, String message){

		eventBusService.publish(address, message);
	}

	/* (non-Javadoc)
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(Message<String> event) {
		
		String message  = event.body();
		
		recieve(message);
	}

	public void subscribe(String address){
		
		eventBusService.subscribe(address, this);
	}
	
	public void unsubscribe(String address){
		
		eventBusService.unsubscribe(address, this);
	}

	
	public abstract void recieve(String message);

}
