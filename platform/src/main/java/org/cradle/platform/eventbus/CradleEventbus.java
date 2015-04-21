/**
 * 
 */
package org.cradle.platform.eventbus;



/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 14, 2014
 */
public interface CradleEventbus {
	
	public void subscribe(String address, TextEventbusHandler handler);
	
	public <T>void subscribe(String address, TypeEventbusHandler<T> handler);
	
	public <T>void unsubscribe(String address, TypeEventbusHandler<T> handler);
	
	public void unsubscribe(String address, TextEventbusHandler handler);
	
	public void publish(String address, String message);
	
	public  <T>void publish(String address, T message, String contentType);
	
	public void shutdown();
}
