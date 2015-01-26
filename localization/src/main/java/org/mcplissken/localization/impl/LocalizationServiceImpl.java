/**
 * 
 */
package org.mcplissken.localization.impl;

import java.util.ArrayList;
import java.util.List;

import org.mcplissken.cache.CacheService;
import org.mcplissken.localization.LocalizationException;
import org.mcplissken.localization.LocalizationService;
import org.mcplissken.localization.Localized;
import org.mcplissken.repository.ModelRepository;
import org.mcplissken.repository.index.IndexDocumentObjectFactory;
import org.mcplissken.repository.index.QueryException;

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
	 * @see org.mcplissken.localization.LocalizationService#localize(java.lang.String, java.util.List)
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
					
					localized = repository
							.queryAdapter(localizedCore, new IndexDocumentObjectFactory<Localized>() {

								@Override
								public Localized createDocument() {
									return new Localized();
								}
							}, 
							new String[]{"id", "desc"})
							.queryAll()
							.filter("id")
							.eq(id)
							.and()
							.result().
							get(0);
					
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
