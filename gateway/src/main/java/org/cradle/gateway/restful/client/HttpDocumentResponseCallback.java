/**
 * 
 */
package org.cradle.gateway.restful.client;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 27, 2014
 */
public interface HttpDocumentResponseCallback {
	
	public void response(Object result);
	
	public void error(Throwable e);
}
