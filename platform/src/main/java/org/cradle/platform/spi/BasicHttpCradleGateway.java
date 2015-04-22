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

import java.lang.annotation.Annotation;
import java.util.Map;

import org.cradle.localization.LocalizationService;
import org.cradle.platform.document.DocumentReader;
import org.cradle.platform.document.DocumentWriter;
import org.cradle.platform.httpgateway.BasicHttpHandler;
import org.cradle.platform.httpgateway.filter.Filter;
import org.cradle.platform.httpgateway.filter.FilterFactory;
import org.cradle.platform.httpgateway.filter.ServiceFilter;
import org.cradle.platform.httpgateway.filter.ServiceFilterConfig;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 22, 2015
 */
public abstract class BasicHttpCradleGateway extends BasicCradleGateway{
	
	private Map<String, DocumentReader> documentReaders;
	private Map<String, DocumentWriter> documentWriters;
	
	private Map<String, FilterFactory> filtersFactoryMap;
	private LocalizationService localizationService;
	
	/**
	 * @param principalChain
	 * @param documentReaders
	 * @param documentWriters
	 * @param filtersFactoryMap
	 * @param localizationService
	 * @param tempFolder
	 */
	public BasicHttpCradleGateway(RegistrationPrincipal principalChain,
			Map<String, DocumentReader> documentReaders,
			Map<String, DocumentWriter> documentWriters,
			Map<String, FilterFactory> filtersFactoryMap,
			LocalizationService localizationService) {
		
		super(principalChain);
		
		this.documentReaders = documentReaders;
		this.documentWriters = documentWriters;
		this.filtersFactoryMap = filtersFactoryMap;
		this.localizationService = localizationService;
	}

	/**
	 * @param contentType
	 * @return
	 */
	protected DocumentWriter getDocumentWriter(String contentType) {
		return documentWriters.get(contentType);
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.BasicCradleGateway#registerHandler(java.lang.annotation.Annotation, java.lang.Object)
	 */
	@Override
	protected <T> void registerHandler(Annotation annotation, T handler) {
		
		ServiceFilterConfig serviceConfig = new ServiceFilterConfig();
		
		BasicHttpHandler httpHandler = (BasicHttpHandler) handler;
		
		ServiceFilter vertxFilter = new ServiceFilter(httpHandler, documentWriters, documentReaders);

		Filter firstFilter = serviceConfig.buildChain(vertxFilter, filtersFactoryMap);

		httpHandler.setLocalizationService(localizationService);

		registerFilterChain(annotation, serviceConfig, firstFilter);
		
	}

	protected abstract void registerFilterChain(Annotation annotation, ServiceFilterConfig serviceConfig, Filter firstFilter);
	
	protected abstract void unregisterHttpHandler(String method, String path);
}
