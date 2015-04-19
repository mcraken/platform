/**
 * 
 */
package org.cradle.platform.vertx.eventbus;

import org.cradle.platform.eventbus.TextEventbusHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 14, 2014
 */
public class VertxTextEventbusHandler implements Handler<Message<String>>{
	
	private TextEventbusHandler textEventbusHandler;
	
	/**
	 * @param textEventbusHandler
	 */
	public VertxTextEventbusHandler(TextEventbusHandler textEventbusHandler) {
		this.textEventbusHandler = textEventbusHandler;
	}

	/* (non-Javadoc)
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(Message<String> event) {
		
		String message  = event.body();
		
		textEventbusHandler.recieve(message);
	}

}
