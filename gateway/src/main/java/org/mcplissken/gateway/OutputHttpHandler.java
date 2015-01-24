/**
 * 
 */
package org.mcplissken.gateway;

import org.mcplissken.gateway.restful.RESTfulRequest;
import org.mcplissken.gateway.restful.RESTfulResponse;
import org.mcplissken.gateway.restful.ResponseObject;
import org.mcplissken.gateway.restful.exception.BadRequestException;
import org.mcplissken.gateway.restful.exception.RESTfulException;
import org.mcplissken.gateway.restful.exception.RedirectException;
import org.mcplissken.gateway.restful.exception.UnauthorizedException;


/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 10, 2014
 */
public abstract class OutputHttpHandler extends BasicHttpHandler {

	/* (non-Javadoc)
	 * @see com.mubasher.gateway.BasicHttpHandler#service(com.mubasher.gateway.HttpAdapter, com.mubasher.gateway.restful.RESTfulRequest, com.mubasher.gateway.restful.RESTfulResponse)
	 */
	@Override
	public void service(HttpAdapter httpAdapter, RESTfulRequest request,
			RESTfulResponse response) throws BadRequestException,
			UnauthorizedException {
		
		try {
			
			ResponseObject responseObject = execute(httpAdapter);
			
			writeServiceResponse(httpAdapter, response, responseObject, false);
			
		} catch (RedirectException e) {
			
			httpAdapter.sendRedirect(e.getUrl());
			
		} catch (RESTfulException e) {
			
			httpAdapter.exception(e);
		}
		
	}

	protected abstract ResponseObject execute(HttpAdapter httpAdapter) throws RedirectException, RESTfulException;
}
