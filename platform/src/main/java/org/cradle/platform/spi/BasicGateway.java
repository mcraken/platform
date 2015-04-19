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
package org.cradle.platform.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Method;
import java.util.Map;

import org.cradle.localization.LocalizationService;
import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.httpgateway.BasicHttpHandler;
import org.cradle.platform.httpgateway.CradleGateway;
import org.cradle.platform.httpgateway.filter.Filter;
import org.cradle.platform.httpgateway.filter.FilterFactory;
import org.cradle.platform.httpgateway.filter.ServiceFilter;
import org.cradle.platform.httpgateway.filter.ServiceFilterConfig;
/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 15, 2015
 */
public abstract class BasicGateway implements CradleGateway {

	private RegistrationPrincipal principalChain;
	private RegistrationAgent registrationAgent;
	
	private Map<String, DocumentReader> documentReaders;
	private Map<String, DocumentWriter> documentWriters;
	
	private Map<String, FilterFactory> filtersFactoryMap;
	private LocalizationService localizationService;
	private String tempFolder;
	
	public BasicGateway(
			RegistrationPrincipal principalChain,
			Map<String, DocumentReader> documentReaders,
			Map<String, DocumentWriter> documentWriters,
			Map<String, FilterFactory> filtersFactoryMap,
			LocalizationService localizationService,
			String tempFolder
			) {
		
		this.documentReaders = documentReaders;
		this.documentWriters = documentWriters;
		this.filtersFactoryMap = filtersFactoryMap;
		this.localizationService = localizationService;
		this.tempFolder = tempFolder;
		this.principalChain = principalChain;
		
		registrationAgent = new RegistrationAgent() {
			
			@Override
			public void register(String method, String path,
					BasicHttpHandler httpHandler) {
				
				registerHttpHandler(method, path, httpHandler, new ServiceFilterConfig());
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.HttpGateway#registerHandler(java.lang.Object)
	 */
	@Override
	public void registerHandler(Object handler) {

		checkNotNull(handler);
		
		for (Method method : handler.getClass().getMethods())
		{
			principalChain.execute(registrationAgent, handler, method);
		}
	}
	
	/**
	 * @param contentType
	 * @return
	 */
	protected DocumentWriter getDocumentWriter(String contentType) {
		return documentWriters.get(contentType);
	}
	
	protected void registerHttpHandler(
			String method, 
			String path,
			BasicHttpHandler httpHandler,
			ServiceFilterConfig serviceConfig) {

		ServiceFilter vertxFilter = new ServiceFilter(httpHandler, documentWriters, documentReaders, tempFolder);

		Filter firstFilter = serviceConfig.buildChain(vertxFilter, filtersFactoryMap);

		httpHandler.setLocalizationService(localizationService);

		registerFilterChain(method, path, serviceConfig, firstFilter);
	}
	
	protected abstract void registerFilterChain(String method, String path,
			ServiceFilterConfig serviceConfig, Filter firstFilter);
	
	protected abstract void unregisterHttpHandler(String method, String path);
}
