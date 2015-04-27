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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.exception.UnauthorizedException;
import org.cradle.platform.httpgateway.spi.GatewayRequest;
import org.cradle.platform.httpgateway.spi.GatewayResponse;
import org.cradle.platform.httpgateway.spi.handler.BasicHttpHandler;

import com.google.common.collect.Multimap;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 26, 2015
 */
public class FilterInvokationHandler implements Filter{

	private String path;
	
	private Map<String, DocumentReader> documentReaders;
	private Map<String, DocumentWriter> documentWriters;

	private BasicHttpHandler handler;
	private Multimap<String, PrecedenceFilter> filters;
	
	private ArrayList<PrecedenceFilter> operationalFilters;
	
	private boolean refreshFilters;
	

	/**
	 * @param documentReaders
	 * @param documentWriters
	 * @param handler
	 * @param filters
	 */
	public FilterInvokationHandler(
			String path,
			Map<String, DocumentReader> documentReaders,
			Map<String, DocumentWriter> documentWriters,
			BasicHttpHandler handler, 
			Multimap<String, PrecedenceFilter> filters) {
		
		this.path = path;
		this.documentReaders = documentReaders;
		this.documentWriters = documentWriters;
		this.handler = handler;
		this.filters = filters;
		
		operationalFilters = new ArrayList<PrecedenceFilter>();
		
		refreshFilters = true;
	}

	public void filter(HttpAdapter httpAdapter) 
			throws HttpException{
		
		if(refreshFilters){
			
			refreshFilters();
		}
		
		if(operationalFilters != null){
			
			for(Filter filter : operationalFilters)
				filter.filter(httpAdapter);
		}

		executeHttpHandler(httpAdapter);

	}

	/**
	 * 
	 */
	private void refreshFilters() {
		
		String patterns[] = filters.keySet().toArray(new String[]{});
		
		for(String pattern : patterns){
			
			if(path.matches(pattern)){	
				operationalFilters.addAll(filters.get(pattern));
			}
		}
		
		operationalFilters.sort(new Comparator<PrecedenceFilter>() {

			@Override
			public int compare(PrecedenceFilter o1, PrecedenceFilter o2) {
				return o1.compare(o2);
			}
		});
		
		refreshFilters = false;
	}

	/**
	 * @param httpAdapter
	 * @param handler
	 * @throws BadRequestException
	 * @throws UnauthorizedException
	 */
	private void executeHttpHandler(HttpAdapter httpAdapter) throws BadRequestException,
			UnauthorizedException {

		GatewayResponse response = new GatewayResponse(httpAdapter, documentWriters);

		GatewayRequest request = httpAdapter.createGatewayRequest(documentReaders);

		handler.service(httpAdapter, request, response);
	}

}
