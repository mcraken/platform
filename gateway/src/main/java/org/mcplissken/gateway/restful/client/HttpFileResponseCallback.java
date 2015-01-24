/**
 * 
 */
package org.mcplissken.gateway.restful.client;

import java.io.File;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 12, 2014
 */
public interface HttpFileResponseCallback {
	
	public void response(File result);
	
	public void error(Throwable e);
}
