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
package org.cradle.platform.httpgateway.filter;

import java.util.Map;

import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.httpgateway.BasicHttpHandler;
import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.UnauthorizedException;
import org.cradle.platform.httpgateway.exception.UnknownResourceException;
import org.cradle.platform.httpgateway.spi.GatewayRequest;
import org.cradle.platform.httpgateway.spi.GatewayResponse;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 20, 2015
 */
public class ServiceFilter extends BasicFilter {

	private BasicHttpHandler handler;
	private Map<String, DocumentReader> documentReaders;
	private Map<String, DocumentWriter> documentWriters;
	
	public ServiceFilter(
			BasicHttpHandler handler,
			Map<String, DocumentWriter> documentWriters,
			Map<String, DocumentReader> documentReaders
			) {

		this.handler = handler;

		this.documentReaders = documentReaders;

		this.documentWriters = documentWriters;

	}
	
	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.BasicRESTfulFilter#doFilter(org.cradle.gateway.HttpAdapter)
	 */
	@Override
	protected void doFilter(HttpAdapter httpAdapter)
			throws BadRequestException, UnauthorizedException,
			UnknownResourceException {
		
		GatewayResponse response = new GatewayResponse(httpAdapter, documentWriters);

		GatewayRequest request = httpAdapter.createGatewayRequest(documentReaders);
		
		handler.service(httpAdapter, request, response);
	}

}
