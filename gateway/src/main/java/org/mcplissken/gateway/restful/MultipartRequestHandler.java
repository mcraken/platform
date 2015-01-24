/**
 * 
 */
package org.mcplissken.gateway.restful;

import java.io.File;
import java.util.List;

import org.mcplissken.gateway.HttpAdapter;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 4, 2015
 */
public interface MultipartRequestHandler {
	
	public Object createFormInstance();
	
	public void handle(HttpAdapter httpAdapter, RESTfulRequest request, RESTfulResponse response, Object form, List<File> uploads);
}
