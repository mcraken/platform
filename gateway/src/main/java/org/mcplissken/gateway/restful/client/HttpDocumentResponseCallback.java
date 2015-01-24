/**
 * 
 */
package org.mcplissken.gateway.restful.client;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 27, 2014
 */
public interface HttpDocumentResponseCallback {
	
	public void response(Object result);
	
	public void error(Throwable e);
}
