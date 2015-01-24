/**
 * 
 */
package org.mcplissken.gateway.restful.filter.path;

import org.mcplissken.gateway.restful.filter.RESTfulFilter;
import org.mcplissken.gateway.restful.filter.RESTfulFilterFactory;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 10, 2014
 */
public class RESTfulPathFIlterFactory implements RESTfulFilterFactory {

	/* (non-Javadoc)
	 * @see com.mubasher.gateway.restful.filter.RESTfulFilterFactory#createFilter()
	 */
	@Override
	public RESTfulFilter createFilter() {
		return new RESTfulPathFilter();
	}

}
