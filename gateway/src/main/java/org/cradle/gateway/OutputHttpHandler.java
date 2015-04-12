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
package org.cradle.gateway;

import org.cradle.gateway.restful.RESTfulRequest;
import org.cradle.gateway.restful.RESTfulResponse;
import org.cradle.gateway.restful.ResponseObject;
import org.cradle.gateway.restful.exception.BadRequestException;
import org.cradle.gateway.restful.exception.RESTfulException;
import org.cradle.gateway.restful.exception.RedirectException;
import org.cradle.gateway.restful.exception.UnauthorizedException;


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 10, 2014
 */
public abstract class OutputHttpHandler extends BasicHttpHandler {

	/* (non-Javadoc)
	 * @see org.cradle.gateway.BasicHttpHandler#service(org.cradle.gateway.HttpAdapter, org.cradle.gateway.restful.RESTfulRequest, org.cradle.gateway.restful.RESTfulResponse)
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
