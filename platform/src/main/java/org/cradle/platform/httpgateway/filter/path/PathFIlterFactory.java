/**
 * 
 */
package org.cradle.platform.httpgateway.filter.path;

import org.cradle.platform.httpgateway.filter.Filter;
import org.cradle.platform.httpgateway.filter.FilterFactory;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 10, 2014
 */
public class PathFIlterFactory implements FilterFactory {

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.RESTfulFilterFactory#createFilter()
	 */
	@Override
	public Filter createFilter() {
		return new PathFilter();
	}

}
