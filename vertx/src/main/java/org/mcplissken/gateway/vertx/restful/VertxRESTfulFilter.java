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
package org.mcplissken.gateway.vertx.restful;

import java.util.Map;

import org.mcplissken.gateway.BasicHttpHandler;
import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.gateway.restful.RESTfulRequest;
import org.mcplissken.gateway.restful.RESTfulResponse;
import org.mcplissken.gateway.restful.document.DocumentReader;
import org.mcplissken.gateway.restful.document.DocumentWriter;
import org.mcplissken.gateway.restful.exception.BadRequestException;
import org.mcplissken.gateway.restful.exception.UnauthorizedException;
import org.mcplissken.gateway.restful.exception.UnknownResourceException;
import org.mcplissken.gateway.restful.filter.BasicRESTfulFilter;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 20, 2015
 */
public class VertxRESTfulFilter extends BasicRESTfulFilter {

	private BasicHttpHandler handler;
	private Map<String, DocumentReader> documentReaders;
	private Map<String, DocumentWriter> documentWriters;
	private String tempFolder;
	
	public VertxRESTfulFilter(
			BasicHttpHandler handler,
			Map<String, DocumentWriter> documentWriters,
			Map<String, DocumentReader> documentReaders, 
			String tempFolder) {

		this.handler = handler;

		this.documentReaders = documentReaders;

		this.documentWriters = documentWriters;

		this.tempFolder = tempFolder;
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.restful.filter.BasicRESTfulFilter#doFilter(org.mcplissken.gateway.HttpAdapter)
	 */
	@Override
	protected void doFilter(HttpAdapter httpAdapter)
			throws BadRequestException, UnauthorizedException,
			UnknownResourceException {
		
		RESTfulResponse response = new RESTfulResponse(httpAdapter, documentWriters);

		RESTfulRequest request = new VertxAsynchronusRESTfulRequest(httpAdapter, documentReaders, tempFolder);
		
		handler.service(httpAdapter, request, response);
	}

}
