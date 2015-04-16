/**
 *  Copyright mcplissken.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cradle.platform.gateway.spi;

import java.io.File;
import java.util.List;

import org.cradle.platform.gateway.BasicHttpHandler;
import org.cradle.platform.gateway.HttpAdapter;
import org.cradle.platform.gateway.restful.MultipartRequestHandler;
import org.cradle.platform.gateway.restful.RESTfulRequest;
import org.cradle.platform.gateway.restful.RESTfulResponse;
import org.cradle.platform.gateway.restful.ResponseObject;
import org.cradle.platform.gateway.restful.exception.BadRequestException;
import org.cradle.platform.gateway.restful.exception.RESTfulException;
import org.cradle.platform.gateway.restful.exception.UnauthorizedException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 20, 2015
 */
public abstract  class MultipartIOHttpHandler extends BasicHttpHandler implements MultipartRequestHandler{

	/* (non-Javadoc)
	 * @see org.cradle.gateway.BasicHttpHandler#service(org.cradle.gateway.HttpAdapter, org.cradle.gateway.restful.RESTfulRequest, org.cradle.gateway.restful.RESTfulResponse)
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
	 * @see org.cradle.gateway.restful.MultipartRequestHandler#handle(org.cradle.gateway.HttpAdapter, java.lang.Object, java.util.List)
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
