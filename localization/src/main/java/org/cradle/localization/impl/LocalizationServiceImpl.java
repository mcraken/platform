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
package org.cradle.localization.impl;

import java.util.ArrayList;
import java.util.List;

import org.cradle.cache.CacheService;
import org.cradle.localization.LocalizationException;
import org.cradle.localization.LocalizationService;
import org.cradle.localization.Localized;
import org.cradle.repository.ModelRepository;
import org.cradle.repository.index.IndexQueryAdapter;
import org.cradle.repository.index.QueryException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 21, 2015
 */
public class LocalizationServiceImpl implements LocalizationService {

	private CacheService cacheService;

	private ModelRepository repository;

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(ModelRepository repository) {
		this.repository = repository;
	}

	/**
	 * @param cacheService the cacheService to set
	 */
	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.localization.LocalizationService#localize(java.lang.String, java.util.List)
	 */
	@Override
	public List<Localized> localize(String language, List<String> ids) throws LocalizationException{

		Localized localized = null; 

		String cacheName = "localized";

		String localizedCore = cacheName + "_" + language;

		ArrayList<Localized> localizationResult = new ArrayList<>();

		for(String id : ids){

			localized = (Localized) cacheService.read("localized", id + "." + language);

			if(localized == null){

				try {

					IndexQueryAdapter<Localized> queryAdapter = repository.queryAdapter(localizedCore);

					localized = queryAdapter.queryAll()
							.filter("id")
							.eq(id)
							.and()
							.result()
							.get(0);

					localized.setLanguage(language);

					cacheService.write(cacheName, id, localized);

				} catch (QueryException e) {

					throw new LocalizationException(e);
				}

			}

			localizationResult.add(localized);

		}

		return localizationResult;
	}

}
