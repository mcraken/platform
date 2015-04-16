/**
 * 
 */
package org.cradle.platform.gateway.restful.client;

import java.util.Map;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 27, 2014
 */
public interface AsynchronousReadingHttpClient {
	
	public void configure(String host, int port, int poolSize);
	
	
	public void read(
			String uri, 
			String documentType,
			Class<?> documentClass,
			HttpDocumentResponseCallback callback
			);
	
	public void read(
			String uri, 
			Map<String, String> headers, 
			String documentType, 
			Class<?> documentClass,
			HttpDocumentResponseCallback callback
			);
	
	public void download(String uri, String name, HttpFileResponseCallback callback);
}
