/**
 * 
 */
package org.cradle.platform.vertx.eventbus;

import org.cradle.platform.eventbus.TypeEventbusHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 22, 2014
 */
public class VertxJsonEventbusHandler<T> implements Handler<Message<JsonObject>>{
	
	private TypeEventbusHandler<T> jsonEventbusHandler;
	
	/**
	 * @param jsonEventbusHandler
	 */
	public VertxJsonEventbusHandler(TypeEventbusHandler<T> jsonEventbusHandler) {
		this.jsonEventbusHandler = jsonEventbusHandler;
	}

	/* (non-Javadoc)
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(Message<JsonObject> event) {
		
		String address = event.address();
		
		JsonObject body = event.body();
		
		jsonEventbusHandler.recieve(address, body.encode());
	}

}
