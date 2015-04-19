/**
 * 
 */
package org.cradle.platform.eventbus;



/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 14, 2014
 */
public interface EventBusService {
	
	public void subscribe(String address, TextEventbusHandler handler);
	
	public <T>void subscribe(String address, JsonEventbusHandler<T> handler);
	
	public <T>void unsubscribe(String address, JsonEventbusHandler<T> handler);
	
	public void unsubscribe(String address, TextEventbusHandler handler);
	
	public void publish(String address, String message);
	
	public void publish(String address, Object message, String contentType);
}
