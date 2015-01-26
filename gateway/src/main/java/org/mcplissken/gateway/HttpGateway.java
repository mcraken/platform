/**
 * 
 */
package org.mcplissken.gateway;

import org.mcplissken.gateway.restful.filter.RESTfullServiceFilterConfig;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 5, 2014
 */
public interface HttpGateway {

	public void unregisterHttpHandler(String method, String path);
	
	public void registerHttpHandler(String method, String path, BasicHttpHandler httpHandler, RESTfullServiceFilterConfig serviceConfig);
}
