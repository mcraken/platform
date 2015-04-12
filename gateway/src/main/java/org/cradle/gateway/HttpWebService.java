/**
 * 
 */
package org.cradle.gateway;

import org.cradle.gateway.restful.client.AsynchronousReadingHttpClient;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 8, 2014
 */
public interface HttpWebService {
	
	public AsynchronousReadingHttpClient createReadingHttpClient();
}
