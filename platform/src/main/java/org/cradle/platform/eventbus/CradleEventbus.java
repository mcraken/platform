/**
 * 
 */
package org.cradle.platform.eventbus;

import org.cradle.platform.spi.CradleProvider;



/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 14, 2014
 */
public interface CradleEventbus extends CradleProvider{
		
	public void publish(String address, String message);
	
	public  <T>void publish(String address, T message);
}
