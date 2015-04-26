/**
 * 
 */
package org.cradle.platform.httpgateway.filter;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.HttpException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 25, 2014
 */
public interface Filter {
	
	public void filter(HttpAdapter httpAdapter) throws HttpException;
	
}