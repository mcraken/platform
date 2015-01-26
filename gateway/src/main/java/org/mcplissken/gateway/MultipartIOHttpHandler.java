/**
 * 
 */
package org.mcplissken.gateway;

import java.io.File;
import java.util.List;

import org.mcplissken.gateway.restful.MultipartRequestHandler;
import org.mcplissken.gateway.restful.RESTfulRequest;
import org.mcplissken.gateway.restful.RESTfulResponse;
import org.mcplissken.gateway.restful.ResponseObject;
import org.mcplissken.gateway.restful.exception.BadRequestException;
import org.mcplissken.gateway.restful.exception.RESTfulException;
import org.mcplissken.gateway.restful.exception.UnauthorizedException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 20, 2015
 */
public abstract  class MultipartIOHttpHandler extends BasicHttpHandler implements MultipartRequestHandler{

	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.BasicHttpHandler#service(org.mcplissken.gateway.HttpAdapter, org.mcplissken.gateway.restful.RESTfulRequest, org.mcplissken.gateway.restful.RESTfulResponse)
	 */
	@Override
	public void service(
			final HttpAdapter httpAdapter, 
			final RESTfulRequest request,
			final RESTfulResponse response) throws BadRequestException,
			UnauthorizedException {

		request.readMultipartRequest(this, response);

	}

	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.restful.MultipartRequestHandler#handle(org.mcplissken.gateway.HttpAdapter, java.lang.Object, java.util.List)
	 */
	@Override
	public void handle(HttpAdapter httpAdapter, RESTfulRequest request, RESTfulResponse response,  Object form, List<File> uploads) {
		
		try {
			
			ResponseObject responseObject = execute(httpAdapter, form, uploads);
			
			writeServiceResponse(httpAdapter, response, responseObject, true);
			
		} catch (RESTfulException e) {
			
			httpAdapter.exception(e);
		}
		
	}

	protected abstract ResponseObject execute(HttpAdapter httpAdapter, Object form, List<File> uploads) throws RESTfulException;
	
}
