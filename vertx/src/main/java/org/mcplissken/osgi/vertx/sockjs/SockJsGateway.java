/**
 * 
 */
package org.mcplissken.osgi.vertx.sockjs;


/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 14, 2014
 */
public interface SockJsGateway {
	
	public void registerAgentApplication(String path, String contentType,SockJsApplication app);
}	
