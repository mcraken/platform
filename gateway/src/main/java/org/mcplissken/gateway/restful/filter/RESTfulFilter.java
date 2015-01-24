/**
 * 
 */
package org.mcplissken.gateway.restful.filter;

import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.gateway.restful.exception.BadRequestException;
import org.mcplissken.gateway.restful.exception.UnauthorizedException;
import org.mcplissken.gateway.restful.exception.UnknownResourceException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 25, 2014
 */
public interface RESTfulFilter {
	
	public void filter(HttpAdapter httpAdapter) 
			throws BadRequestException, UnauthorizedException, UnknownResourceException;
	
	public void next(HttpAdapter httpAdapter)
			throws BadRequestException, UnauthorizedException, UnknownResourceException;
	
	public void setNextFilter(RESTfulFilter nextFilter);
	
}