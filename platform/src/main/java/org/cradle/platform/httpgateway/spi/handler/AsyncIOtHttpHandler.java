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
package org.cradle.platform.httpgateway.spi.handler;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.exception.UnauthorizedException;
import org.cradle.platform.httpgateway.spi.GatewayRequest;
import org.cradle.platform.httpgateway.spi.GatewayResponse;
import org.cradle.platform.httpgateway.spi.ResponseObject;


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 10, 2014
 */
public abstract  class AsyncIOtHttpHandler extends BasicHttpHandler{


	/* (non-Javadoc)
	 * @see org.cradle.gateway.BasicHttpHandler#service(org.cradle.gateway.HttpAdapter, org.cradle.gateway.restful.RESTfulRequest, org.cradle.gateway.restful.RESTfulResponse)
	 */
	@Override
	public void service(
			final HttpAdapter httpAdapter, 
			final GatewayRequest request,
			final GatewayResponse response) throws BadRequestException,
			UnauthorizedException {

		request.readDocumentObjectAsynchronously(new AsynchronusRequestHandler() {

			@Override
			public void handleDocument(Object document) {

				try {
					
					ResponseObject responseObject = execute(httpAdapter, document);
					
					writeServiceResponse(httpAdapter, response, responseObject, false);
					
				} catch (HttpException e) {
					
					httpAdapter.exception(e);
				}

				
			}
		}, getDocumentType());

	}

	protected abstract ResponseObject execute(HttpAdapter adapter, Object document) throws HttpException;

	public abstract Class<?> getDocumentType();

}
