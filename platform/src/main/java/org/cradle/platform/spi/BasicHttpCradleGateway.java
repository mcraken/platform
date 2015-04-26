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
import org.cradle.platform.httpgateway.HttpFilter;
import org.cradle.platform.httpgateway.HttpMethod;
import org.cradle.platform.httpgateway.filter.FilterInvokationHandler;
import org.cradle.platform.httpgateway.filter.PrecedenceFilter;
import org.cradle.platform.httpgateway.spi.BasicHttpHandler;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 22, 2015
 */
public abstract class BasicHttpCradleGateway extends BasicCradleProvider{

	private Map<String, DocumentReader> documentReaders;
	private Map<String, DocumentWriter> documentWriters;

	private LocalizationService localizationService;

	private Multimap<String, PrecedenceFilter> filters;

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
			LocalizationService localizationService) {

		super(principalChain);

		this.documentReaders = documentReaders;
		this.documentWriters = documentWriters;
		this.localizationService = localizationService;

		this.filters = HashMultimap.create();
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

		Class<? extends Annotation> annotationType = annotation.annotationType();

		if(annotationType == HttpMethod.class){
			
			HttpMethod methodAnntoation = (HttpMethod) annotation;
			
			BasicHttpHandler httpHandler = (BasicHttpHandler) handler;

			httpHandler.setLocalizationService(localizationService);

			registerHttpHandler(methodAnntoation, 
					new FilterInvokationHandler(methodAnntoation.path(), documentReaders, documentWriters, httpHandler, filters));
			
		} else if(annotationType == HttpFilter.class){
			
			HttpFilter filterAnnotation = (HttpFilter) annotation;
			
			PrecedenceFilter filter = (PrecedenceFilter) handler;
			
			filters.put(filterAnnotation.pattern(), filter);
			
		} 

	}

	protected abstract void registerHttpHandler(HttpMethod annotation, FilterInvokationHandler invokkationHandler);

	protected abstract void unregisterHttpHandler(String method, String path);
}
