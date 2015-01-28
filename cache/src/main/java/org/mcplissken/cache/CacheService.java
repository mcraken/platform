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
package org.mcplissken.cache;

import java.util.List;

import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.InvalidCriteriaException;
import org.mcplissken.repository.key.exception.InvalidCriteriaSyntaxException;
import org.mcplissken.repository.key.exception.UnknowModelException;
import org.mcplissken.repository.models.RestModel;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 4, 2014
 */
public interface CacheService {

	public <T> void regitserCache(String name, boolean eternal, List<CacheAttributeExtractor<T>> extractors);
	
	public Object read(String modelName, Object key);
	
	public Object read(RestSearchKey key) throws InvalidCriteriaSyntaxException, InvalidCriteriaException, UnknowModelException;
	
	public KeySelectionAdapter createKeySelectionAdapter(String cacheName);
	
	public ValueSelectionAdapter createValueSelectionAdapter(String cacheName);
	
	public void write(String cacheName, Object key, Object modelObject); 

	public void writeThrough(String cacheName, Object key, Object modelObject);

	public void write(String cacheName, RestModel[] models);
	
	public boolean remove(String cacheName, Object key);

}