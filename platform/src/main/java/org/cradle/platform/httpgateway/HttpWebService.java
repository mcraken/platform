/**
 * 
 */
package org.cradle.platform.httpgateway;

import org.cradle.platform.httpgateway.client.AsynchronousReadingHttpClient;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 8, 2014
 */
public interface HttpWebService {
	
	public AsynchronousReadingHttpClient createReadingHttpClient();
}
