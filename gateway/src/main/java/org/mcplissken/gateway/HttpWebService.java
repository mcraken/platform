/**
 * 
 */
package org.mcplissken.gateway;

import org.mcplissken.gateway.restful.client.AsynchronousReadingHttpClient;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 8, 2014
 */
public interface HttpWebService {
	
	public AsynchronousReadingHttpClient createReadingHttpClient();
}
